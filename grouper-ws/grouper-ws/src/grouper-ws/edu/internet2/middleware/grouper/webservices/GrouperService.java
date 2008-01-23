package edu.internet2.middleware.grouper.webservices;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.GroupAnyAttributeFilter;
import edu.internet2.middleware.grouper.GroupAttributeFilter;
import edu.internet2.middleware.grouper.GroupFinder;
import edu.internet2.middleware.grouper.GroupNotFoundException;
import edu.internet2.middleware.grouper.GrouperQuery;
import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.InsufficientPrivilegeException;
import edu.internet2.middleware.grouper.Member;
import edu.internet2.middleware.grouper.SessionException;
import edu.internet2.middleware.grouper.Stem;
import edu.internet2.middleware.grouper.StemFinder;
import edu.internet2.middleware.grouper.StemNotFoundException;
import edu.internet2.middleware.grouper.Stem.Scope;
import edu.internet2.middleware.grouper.webservices.WsAddMemberResults.WsAddMemberResultCode;
import edu.internet2.middleware.grouper.webservices.WsFindGroupsResults.WsFindGroupsResultCode;
import edu.internet2.middleware.grouper.webservices.WsSubjectLookup.SubjectFindResult;
import edu.internet2.middleware.subject.Subject;

/**
 * <pre>
 * All public methods in this class are available in the web service
 * as both SOAP and REST.
 * 
 * booleans can either be T, F, true, false (case-insensitive)
 * 
 * get wsdl from: http://localhost:8090/grouper/services/GrouperService?wsdl
 * 
 * generate client (after wsdl copied): C:\mchyzer\isc\dev\grouper\axisJar2>wsdl2java -p edu.internet2.middleware.grouper.webservicesClient -t -uri GrouperService.wsdl
 * 
 * @author mchyzer
 * </pre>
 */
public class GrouperService {

    /** logger */
    private static final Log LOG = LogFactory.getLog(GrouperService.class);

    /**
     * find a group or groups
     * @param groupName search by group name (must match exactly), cannot use other params with this
     * @param stemName will return groups in this stem
     * @param stemNameScope if searching by stem, ONE_LEVEL is for one level, 
     * ALL_IN_SUBTREE will return all in sub tree.  Required if searching by stem
     * @param groupUuid search by group uuid (must match exactly), cannot use other params with this
     * @param queryTerm if searching by query, this is a term that will be matched to 
     * name, extension, etc
     * @param querySearchFromStemName if a stem name is put here, that will narrow the search
     * @param queryScope NAME is searching by name, EXTENSION is display extension, and DISPLAY_NAME is display name.
     * This is required if a query search
	 * @param actAsSubjectId optional: is the subject id of subject to act as (if proxying).
	 * Only pass one of actAsSubjectId or actAsSubjectIdentifer
	 * @param actAsSubjectIdentifier optional: is the subject identifier of subject
	 * to act as (if proxying).  Only pass one of actAsSubjectId or actAsSubjectIdentifer
	 * @param paramName0 reserved for future use
	 * @param paramValue0 reserved for future use
	 * @param paramName1 reserved for future use
	 * @param paramValue1 reserved for future use
     * @return the groups, or no groups if none found
     */
    public WsFindGroupsResults findGroupsSimple(String groupName, String stemName, 
    		String stemNameScope,
    		String groupUuid, String queryTerm, String querySearchFromStemName, 
    		String queryScope, 	String actAsSubjectId,
			String actAsSubjectIdentifier,
			String paramName0, String paramValue0,
			String paramName1, String paramValue1) {
    	
		WsSubjectLookup actAsSubjectLookup = new WsSubjectLookup(actAsSubjectId, actAsSubjectIdentifier);

		String[][] params = GrouperServiceUtils.params(paramName0, paramValue0,
				paramName1, paramValue1);
		String[] paramNames = params[0];
		String[] paramValues = params[1];
		
		//pass through to the more comprehensive method
		WsFindGroupsResults wsFindGroupsResults = findGroups(groupName, stemName, 
				stemNameScope, groupUuid, queryTerm, querySearchFromStemName, 
				queryScope, actAsSubjectLookup, paramNames, paramValues);
		
		return wsFindGroupsResults;
    }
    	
    /**
     * find a group or groups
     * @param groupName search by group name (must match exactly), cannot use other params with this
     * @param stemName will return groups in this stem
     * @param stemNameScope if searching by stem, ONE_LEVEL is for one level, 
     * ALL_IN_SUBTREE will return all in sub tree.  Required if searching by stem
     * @param groupUuid search by group uuid (must match exactly), cannot use other params with this
     * @param queryTerm if searching by query, this is a term that will be matched to 
     * name, extension, etc
     * @param querySearchFromStemName if a stem name is put here, that will narrow the search
     * @param queryScope NAME is searching by name, EXTENSION is display extension, DISPLAY_NAME is display name, and 
     * DISPLAY_EXTENSION is searching by display extension
     * This is required if a query search
     * @param actAsSubjectLookup 
	 * @param paramNames optional: reserved for future use
	 * @param paramValues optional: reserved for future use
     * @return the groups, or no groups if none found
     */
    @SuppressWarnings("unchecked")
	public WsFindGroupsResults findGroups(String groupName, String stemName, 
    		String stemNameScope,
    		String groupUuid, String queryTerm, String querySearchFromStemName, 
    		String queryScope, WsSubjectLookup actAsSubjectLookup,
			String[] paramNames, String[] paramValues) {
    	
		GrouperSession session = null;
		WsFindGroupsResults wsFindGroupsResults = new WsFindGroupsResults();

		boolean searchByName = StringUtils.isNotBlank(groupName);
		boolean searchByStem = StringUtils.isNotBlank(stemName);
		boolean searchByUuid = StringUtils.isNotBlank(groupUuid);
		boolean searchByQuery = StringUtils.isNotBlank(queryTerm);
		
		//TODO make sure size of params and values the same
		
		//count the search types
		int searchTypes = (searchByName ? 1 : 0) + (searchByStem ? 1 : 0)
			+ (searchByUuid ? 1 : 0) + (searchByQuery ? 1 : 0);
		//must only search by one type
		if (searchTypes != 1) {
			wsFindGroupsResults.assignResultCode(WsFindGroupsResultCode.INVALID_QUERY);
			wsFindGroupsResults.setResultMessage("Invalid query, only query on one thing, not multiple.  " +
					"Only search by name, stem, uuid, or query");
			return wsFindGroupsResults;
		}
		
		//assume success
		wsFindGroupsResults.assignResultCode(WsFindGroupsResultCode.SUCCESS);
		
		Subject actAsSubject = null;
		try {
			actAsSubject = GrouperServiceJ2ee.retrieveSubjectActAs(actAsSubjectLookup);
			
			if (actAsSubject == null) {
				throw new RuntimeException("Cant find actAs user: " + actAsSubjectLookup);
			}
			
			//use this to be the user connected, or the user act-as
			try {
				session = GrouperSession.start(actAsSubject);
			} catch (SessionException se) {
				throw new RuntimeException("Problem with session for subject: " + actAsSubject, se);
			}
			
			//simple search by name
			if (searchByName) {
				try {
					Group group = GroupFinder.findByName(session, groupName);
					wsFindGroupsResults.assignGroupResult(group);
				} catch (GroupNotFoundException gnfe) {
					//just ignore, the group results will be blank
				}
				return wsFindGroupsResults;
			}
			
			//simple search for uuid
			if (searchByUuid) {
				try {
					Group group = GroupFinder.findByUuid(session, groupUuid);
					wsFindGroupsResults.assignGroupResult(group);
				} catch (GroupNotFoundException gnfe) {
					//just ignore, the group results will be blank
				}
				return wsFindGroupsResults;
			}
			
			if (searchByStem) {
				boolean oneLevel = StringUtils.equals("ONE_LEVEL", stemNameScope);
				boolean allInSubtree = StringUtils.equals("ALL_IN_SUBTREE", stemNameScope);
				//get the stem
				Stem stem = null;
				try {
					stem = StemFinder.findByName(session, stemName);
				} catch (StemNotFoundException snfe) {
					//this isnt good, this is a problem
					wsFindGroupsResults.assignResultCode(WsFindGroupsResultCode.STEM_NOT_FOUND);
					wsFindGroupsResults.setResultMessage("Invalid query, cant find stem: '" + stemName + "'");
					return wsFindGroupsResults;
				}
				Set<Group> groups = null;
				if (oneLevel) {
					groups = stem.getChildGroups(Scope.ONE);
				} else if (allInSubtree) {
					groups = stem.getChildGroups(Scope.SUB);
				} else {
					throw new RuntimeException("Invalid stemNameScope: '" + stemNameScope + "' must be" +
							"one of: ONE_LEVEL, ALL_IN_SUBTREE");
				}
				//now set these to the response
				wsFindGroupsResults.assignGroupResult(groups);
				return wsFindGroupsResults;
			}
			
			if (searchByQuery) {
				
				//if empty string, that is the root stem
				querySearchFromStemName = StringUtils.trimToEmpty(querySearchFromStemName);

				//get the stem
				Stem querySearchFromStem = null;
				try {
					querySearchFromStem = StemFinder.findByName(session, querySearchFromStemName);
				} catch (StemNotFoundException snfe) {
					//this isnt good, this is a problem
					wsFindGroupsResults.assignResultCode(WsFindGroupsResultCode.STEM_NOT_FOUND);
					wsFindGroupsResults.setResultMessage("Invalid query, cant find stem: '" + querySearchFromStemName + "'");
					return wsFindGroupsResults;
				}

				GrouperQuery grouperQuery = null;
				
			    //see if there is a particular query scope
			    if (!StringUtils.isBlank(queryScope)) {
			    	//four scopes, the query is searches in group names, display names, extensions, or display extensions
			    	boolean searchScopeByName = StringUtils.equals("NAME", queryScope);
			    	boolean searchScopeByExtension = StringUtils.equals("EXTENSION", queryScope);
			    	boolean searchScopeByDisplayName = StringUtils.equals("DISPLAY_NAME", queryScope);
			    	boolean searchScopeByDisplayExtension = StringUtils.equals("DISPLAY_EXTENSION", queryScope);
			    	
			    	if (!searchScopeByName && !searchScopeByExtension && !searchScopeByDisplayName
			    			&& !searchScopeByDisplayExtension) {
			    		throw new RuntimeException("Invalid queryScope, must be one of: NAME is searching by name, " +
			    				"EXTENSION is display extension, DISPLAY_NAME is display name, AND " +
			    				"DISPLAY_EXTENSION is searching by display extension");
			    	}
			    	String attribute = null;
			    	attribute = searchScopeByName ? "name" : attribute;
			    	attribute = searchScopeByExtension ? "extension" : attribute;
			    	attribute = searchScopeByDisplayName ? "displayName" : attribute;
			    	attribute = searchScopeByDisplayExtension ? "displayExtension" : attribute;

				
					grouperQuery = GrouperQuery.createQuery(session,new GroupAttributeFilter(
							attribute,queryTerm,querySearchFromStem));
			    } else {
				    //not constrained to a particular scope
					grouperQuery = GrouperQuery.createQuery(session,new GroupAnyAttributeFilter(queryTerm,
							querySearchFromStem));
			    	
			    }
				Set<Group> results = grouperQuery.getGroups();
				wsFindGroupsResults.assignGroupResult(results);
				return wsFindGroupsResults;
			    
			}
			
			throw new RuntimeException("Cant find search strategy");
		} catch (Exception re) {
			wsFindGroupsResults.assignResultCode(WsFindGroupsResultCode.EXCEPTION);
			String theError = "Problem finding group: groupName: " + groupName
				+ ", stemName: " + stemName + ", stemNameScope: " + stemNameScope
				+ ", groupUuid: " + groupUuid + ", queryTerm: " + queryTerm
				+ ", querySearchFromStemName: " + querySearchFromStemName 	
				+ ", queryScope: " +  queryScope + ", actAsSubjectLookup: " + actAsSubjectLookup
				/* TODO add in param names and values */
				 + ".  ";
			wsFindGroupsResults.setResultMessage(theError);
			//this is sent back to the caller anyway, so just log, and not send back again
			LOG.error(theError + ", wsFindGroupsResults: " + GrouperServiceUtils.toStringForLog(wsFindGroupsResults
					+ ",\n" + ExceptionUtils.getFullStackTrace(re)), re);
		} finally {
			if (session != null) {
				try {
					session.stop();
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}
		
		if (!"T".equalsIgnoreCase(wsFindGroupsResults.getSuccess())) {
			
			LOG.error(wsFindGroupsResults.getResultMessage());
		}
		return wsFindGroupsResults;

    }
    
	/**
	 * add member to a group (if already a direct member, ignore)
	 * @param wsGroupLookup 
	 * @param subjectLookups subjects to be added to the group
	 * @param replaceAllExisting optional: T or F (default), if the 
	 * existing groups should be replaced 
	 * @param actAsSubjectLookup 
	 * @param paramNames optional: reserved for future use
	 * @param paramValues optional: reserved for future use
	 * @return the results
	 */
	@SuppressWarnings("unchecked")
	public WsAddMemberResults addMember(WsGroupLookup wsGroupLookup,
			WsSubjectLookup[] subjectLookups, 
			String replaceAllExisting, WsSubjectLookup actAsSubjectLookup,
			String[] paramNames, String[] paramValues) {
		
		GrouperSession session = null;
		WsAddMemberResults wsAddMemberResults = new WsAddMemberResults();
		int subjectLength = subjectLookups == null ? 0 : subjectLookups.length;
		if (subjectLength == 0) {
			wsAddMemberResults.assignResultCode(WsAddMemberResultCode.INVALID_QUERY);
			wsAddMemberResults.appendResultMessage("Subject length must be more than 1");
			return wsAddMemberResults;
		}
		
		//see if greater than the max (or default)
		Integer maxAddMember = GrouperWsConfig.getPropertyInteger(GrouperWsConfig.WS_ADD_MEMBER_SUBJECTS_MAX, 1000000);
		if (subjectLength > maxAddMember) {
			wsAddMemberResults.assignResultCode(WsAddMemberResultCode.INVALID_QUERY);
			wsAddMemberResults.appendResultMessage("Subject length must be less than max: " + maxAddMember + " (sent in " + subjectLength + ")");
			return wsAddMemberResults;
		}
		
		//TODO make sure size of params and values the same
		
		//assume success
		wsAddMemberResults.assignResultCode(WsAddMemberResultCode.SUCCESS);
		Subject actAsSubject = null;
		try {
			actAsSubject = GrouperServiceJ2ee.retrieveSubjectActAs(actAsSubjectLookup);
			
			if (actAsSubject == null) {
				throw new RuntimeException("Cant find actAs user: " + actAsSubjectLookup);
			}
			
			//use this to be the user connected, or the user act-as
			try {
				session = GrouperSession.start(actAsSubject);
			} catch (SessionException se) {
				throw new RuntimeException("Problem with session for subject: " + actAsSubject, se);
			}
			wsGroupLookup.retrieveGroupIfNeeded(session);
			wsAddMemberResults.setResults(new WsAddMemberResult[subjectLength]);
			Group group = wsGroupLookup.retrieveGroup();
			
			if (group == null) {
				wsAddMemberResults.assignResultCode(WsAddMemberResultCode.INVALID_QUERY);
				wsAddMemberResults.appendResultMessage("Cant find group: " + wsGroupLookup + ".  ");
				return wsAddMemberResults;
			}
			
			int resultIndex = 0;
			
			//TODO keep data in transaction?
			boolean replaceAllExistingBoolean = GrouperWsUtils.booleanValue(replaceAllExisting, false);
			Set<String> newSubjectIds = new HashSet<String>();
			
			for (WsSubjectLookup wsSubjectLookup : subjectLookups) {
				WsAddMemberResult wsAddMemberResult = new WsAddMemberResult();
				wsAddMemberResults.getResults()[resultIndex] = wsAddMemberResult;
				try {
					//default to non-success
					wsAddMemberResult.setSuccess("F");
	
					wsAddMemberResult.setSubjectId(wsSubjectLookup.getSubjectId());
					wsAddMemberResult.setSubjectIdentifier(wsSubjectLookup.getSubjectIdentifier());
	
					Subject subject = wsSubjectLookup.retrieveSubject();
					
					//make sure the subject is there
					if (subject == null) {
						//see why not
						SubjectFindResult subjectFindResult = wsSubjectLookup.retrieveSubjectFindResult();
						String error = "Subject: " + wsSubjectLookup + " had problems: " + subjectFindResult;
						wsAddMemberResult.setResultMessage(error);
						throw new NullPointerException(error);
					} 
	
					//these will probably match, but just in case
					if (StringUtils.isBlank(wsAddMemberResult.getSubjectId())) {
						wsAddMemberResult.setSubjectId(subject.getId());
					}
	
					//keep track
					if (replaceAllExistingBoolean) {
						newSubjectIds.add(subject.getId());
					}
					
					try {
						//dont fail if already a direct member
						if (!group.hasImmediateMember(subject)) {
							group.addMember(subject);
						}
						wsAddMemberResult.setSuccess("T");
						wsAddMemberResult.setResultCode("SUCCESS");
						
					} catch (InsufficientPrivilegeException ipe) {
						wsAddMemberResult.setResultCode("INSUFFICIENT_PRIVILEGES");
						wsAddMemberResult.setResultMessage(ExceptionUtils.getFullStackTrace(ipe));
					}
				} catch (Exception e) {
					wsAddMemberResult.setResultCode("EXCEPTION");
					wsAddMemberResult.setResultMessage(ExceptionUtils.getFullStackTrace(e));
					LOG.error(wsSubjectLookup + ", " + e, e);
				}
				resultIndex++;
			}
			
			//after adding all these, see if we are removing:
			if (replaceAllExistingBoolean) {
				
				//see who is there
				Set<Member> members = group.getImmediateMembers();
				
				for (Member member : members) {
					String subjectId = member.getSubjectId();
					Subject subject = null;
					
					if (!newSubjectIds.contains(subjectId)) {
						try {
							subject = member.getSubject();
							group.deleteMember(subject);
						} catch (Exception e) {
							String theError = "Error deleting subject: " + ObjectUtils.defaultIfNull(subject, subjectId) 
								+ " from group: " + group + ", " + e + ".  ";
							LOG.error(theError, e);

							wsAddMemberResults.appendResultMessage(theError + ExceptionUtils.getFullStackTrace(e));
							wsAddMemberResults.assignResultCode(WsAddMemberResultCode.PROBLEM_DELETING_MEMBERS);
						}
					}
				}
			}
		} catch (RuntimeException re) {
			wsAddMemberResults.assignResultCode(WsAddMemberResultCode.EXCEPTION);
			String theError = "Problem adding member to group: wsGroupLookup: " + wsGroupLookup
				+ ", subjectLookups: " + GrouperServiceUtils.toStringForLog(subjectLookups)
				+ ", replaceAllExisting: " +  replaceAllExisting +  ", actAsSubject: " + actAsSubject
				 + ".  ";
			wsAddMemberResults.appendResultMessage(theError);
			//this is sent back to the caller anyway, so just log, and not send back again
			LOG.error(theError + ", wsAddMemberResults: " + GrouperServiceUtils.toStringForLog(wsAddMemberResults), re);
		} finally {
			if (session != null) {
				try {
					session.stop();
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}
		
		if (wsAddMemberResults.getResults() != null) {
			//check all entries
			int successes = 0;
			int failures = 0;
			for (WsAddMemberResult wsAddMemberResult : wsAddMemberResults.getResults()) {
				boolean success = "T".equalsIgnoreCase(wsAddMemberResult.getSuccess());
				if (success) {
					successes++;
				} else {
					failures++;
				}
			}
			if (failures > 0) {
				wsAddMemberResults.appendResultMessage("There were " + successes + " successes and " + failures 
						+ " failures of users added to the group.   ");
				wsAddMemberResults.assignResultCode(WsAddMemberResultCode.PROBLEM_WITH_ASSIGNMENT);
			} else {
				wsAddMemberResults.assignResultCode(WsAddMemberResultCode.SUCCESS);
			}
		}
		if (!"T".equalsIgnoreCase(wsAddMemberResults.getSuccess())) {
			
			LOG.error(wsAddMemberResults.getResultMessage());
		}
		return wsAddMemberResults;
	}
		
	/**
	 * add member to a group (if already a direct member, ignore)
	 * @param groupName 
	 * @param groupUuid 
	 * @param subjectId 
	 * @param subjectIdentifier 
	 * @param actAsSubjectId optional: is the subject id of subject to act as (if proxying).
	 * Only pass one of actAsSubjectId or actAsSubjectIdentifer
	 * @param actAsSubjectIdentifier optional: is the subject identifier of subject
	 * to act as (if proxying).  Only pass one of actAsSubjectId or actAsSubjectIdentifer
	 * @param paramName0 reserved for future use
	 * @param paramValue0 reserved for future use
	 * @param paramName1 reserved for future use
	 * @param paramValue1 reserved for future use
	 * @return the result of one member add
	 */
	public WsAddMemberResult addMemberSimple(String groupName,
			String groupUuid,
			String subjectId, 
			String subjectIdentifier,
			String actAsSubjectId,
			String actAsSubjectIdentifier,
			String paramName0,
			String paramValue0,
			String paramName1,
			String paramValue1) {
		
		//setup the group lookup
		WsGroupLookup wsGroupLookup = new WsGroupLookup(groupName, groupUuid);
		
		//setup the subject lookup
		WsSubjectLookup[] subjectLookups = new WsSubjectLookup[1];
		subjectLookups[0] = new WsSubjectLookup(subjectId, subjectIdentifier);
		WsSubjectLookup actAsSubjectLookup = new WsSubjectLookup(actAsSubjectId, actAsSubjectIdentifier);
		
		String[][] params = GrouperServiceUtils.params(paramName0, paramValue0,
				paramName1, paramValue1);
		String[] paramNames = params[0];
		String[] paramValues = params[1];
		
		WsAddMemberResults wsAddMemberResults = addMember(wsGroupLookup, 
				subjectLookups, "F", actAsSubjectLookup, paramNames, paramValues);
		
		WsAddMemberResult[] results = wsAddMemberResults.getResults();
		if (results != null && results.length > 0) {
			return results[0];
		}
		//didnt even get that far to where there is a subject result
		WsAddMemberResult wsAddMemberResult = new WsAddMemberResult();
		wsAddMemberResult.setResultMessage(wsAddMemberResults.getResultMessage());
		wsAddMemberResult.setResultCode(wsAddMemberResults.getResultCode());
		wsAddMemberResult.setSubjectId(subjectId);
		wsAddMemberResult.setSubjectIdentifier(subjectIdentifier);
		
		//definitely not a success
		wsAddMemberResult.setSuccess("F");
		
		return wsAddMemberResult;
			
	}
	
//	/**
//	 * web service wrapper for find all subjects based on query
//	 * @param query
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public WsSubject[] findAll(String query) {
//		Set<JDBCSubject> subjectSet = SubjectFinder.findAll(query);
//		if (subjectSet == null || subjectSet.size() == 0) {
//			return null;
//		}
//		//convert the set to a list
//		WsSubject[] results = new WsSubject[subjectSet.size()];
//		int i=0;
//		for (JDBCSubject jdbcSubject : subjectSet) {
//			WsSubject wsSubject = new WsSubject(jdbcSubject);
//			results[i++] = wsSubject;
//		}
//		return results;
//	}
	
}
