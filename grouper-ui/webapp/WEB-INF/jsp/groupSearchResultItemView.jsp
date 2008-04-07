<%-- @annotation@
		  Dynamic tile used to render a group as a search result
--%><%--
  @author Gary Brown.
  @version $Id: groupSearchResultItemView.jsp,v 1.4 2008-04-07 07:54:15 mchyzer Exp $
--%>
<%@include file="/WEB-INF/jsp/include.jsp"%>
<tiles:importAttribute ignore="true"/>
<c:if test="${empty groupSearchResultField}"><c:set var="groupSearchResultField" value="${mediaMap['search.group.result-field']}"/></c:if>
<img src="grouper/images/group.gif" <grouper:tooltip key="group.icon.tooltip"/> class="groupIcon" /><span class="<c:out value="${viewObject.subjectType}"/>Subject"><c:out value="${viewObject[groupSearchResultField]}" /></span>