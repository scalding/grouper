/**
 * 
 */
package edu.internet2.middleware.grouper.ws.soap;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.ws.util.GrouperServiceUtils;

/**
 * Result of one group being deleted.  The number of
 * these result objects will equal the number of groups sent in to the method
 * to be deleted
 * 
 * @author mchyzer
 */
public class WsGroupDeleteResult {

  /** logger */
  private static final Log LOG = LogFactory.getLog(WsGroupDeleteResult.class);

  /**
   * empty constructor
   */
  public WsGroupDeleteResult() {
    //nothing to do
  }

  /**
   * @param wsGroupLookup is the group lookup to assign
   */
  public WsGroupDeleteResult(WsGroupLookup wsGroupLookup) {
    this.groupLookup = wsGroupLookup;
  }

  /** the group that was looked up */
  private WsGroupLookup groupLookup;

  /**
   * @return the groupLookup
   */
  public WsGroupLookup getGroupLookup() {
    return this.groupLookup;
  }

  /**
   * @param groupLookup1 the groupLookup to set
   */
  public void setGroupLookup(WsGroupLookup groupLookup1) {
    this.groupLookup = groupLookup1;
  }

  /**
   * create a result based on group
   * @param group
   * @param includeDetail
   */
  public void assignGroup(Group group, boolean includeDetail) {
    this.setWsGroup(new WsGroup(group, includeDetail));
  }

  /**
   * group to be deleted
   */
  private WsGroup wsGroup;

  /**
   * metadata about the result
   */
  private WsResultMeta resultMetadata = new WsResultMeta();

  /**
   * result code of a request
   */
  public enum WsGroupDeleteResultCode {

    /** in gorup lookup, the uuid doesnt match name */
    GROUP_UUID_DOESNT_MATCH_NAME,

    /** successful addition (lite status code 200) */
    SUCCESS,

    /** invalid query, can only happen if simple query */
    INVALID_QUERY,

    /** the group was not found */
    GROUP_NOT_FOUND,

    /** problem with deleting */
    EXCEPTION,

    /** user not allowed */
    INSUFFICIENT_PRIVILEGES,

    /** transaction rolled back */
    TRANSACTION_ROLLED_BACK,

    /** if parent stem cant be found */
    PARENT_STEM_NOT_FOUND;

    /**
     * if this is a successful result
     * @return true if success
     */
    public boolean isSuccess() {
      return this == SUCCESS || this == GROUP_NOT_FOUND;
    }

  }

  /**
   * assign the code from the enum
   * @param groupDeleteResultCode
   */
  public void assignResultCode(WsGroupDeleteResultCode groupDeleteResultCode) {
    this.getResultMetadata().assignResultCode(
        groupDeleteResultCode == null ? null : groupDeleteResultCode.name());
    this.getResultMetadata().assignSuccess(
        GrouperServiceUtils.booleanToStringOneChar(groupDeleteResultCode.isSuccess()));
  }

  /**
   * @return the wsGroup
   */
  public WsGroup getWsGroup() {
    return this.wsGroup;
  }

  /**
   * @param wsGroupResult1 the wsGroup to set
   */
  public void setWsGroup(WsGroup wsGroupResult1) {
    this.wsGroup = wsGroupResult1;
  }

  /**
   * assign a resultcode of exception, and process/log the exception
   * @param e
   * @param wsGroupLookup
   */
  public void assignResultCodeException(Exception e, WsGroupLookup wsGroupLookup) {
    this.assignResultCode(WsGroupDeleteResultCode.EXCEPTION);
    this.getResultMetadata().setResultMessage(ExceptionUtils.getFullStackTrace(e));
    LOG.error(wsGroupLookup + ", " + e, e);
  }

  /**
   * @return the resultMetadata
   */
  public WsResultMeta getResultMetadata() {
    return this.resultMetadata;
  }

}
