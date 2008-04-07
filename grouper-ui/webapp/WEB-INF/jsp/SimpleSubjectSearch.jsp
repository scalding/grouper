<%-- @annotation@
		Tile which displays the simple subject search form  - allows any configured source to be selected
--%><%--
  @author Gary Brown.
  @version $Id: SimpleSubjectSearch.jsp,v 1.6 2008-04-07 07:54:15 mchyzer Exp $
--%>
<%@include file="/WEB-INF/jsp/include.jsp"%>
<grouper:recordTile key="Not dynamic" tile="${requestScope['javax.servlet.include.servlet_path']}">

<div class="searchSubjects">
<grouper:subtitle key="find.heading.search" />
<c:if test="${mediaMap['allow.self-subject-summary'] == 'true'}">
<div class="subjectAsSelfLink"><html:link page="/populateSubjectSummary.do" name="AuthSubject">
					<grouper:message bundle="${nav}" key="subject.view.yourself"/>
		</html:link></div>
</c:if>
<!--<p><a class="underline" href="<c:out value="${pageUrlMinusQueryString}"/>?advancedSearch=true"><grouper:message bundle="${nav}" key="find.action.select.groups-advanced-search"/></a></p>
-->
 <html:form styleId="SearchFormBean" action="/doSearchSubjects" method="post">
 		<html:hidden property="searchInNameOrExtension"/>
		<html:hidden property="searchInDisplayNameOrExtension"/>
<fieldset>


<div class="formRow">
	<div class="formLeft">
	<label for="searchTerm"><grouper:message bundle="${nav}" key="find.search-term"/></label>
	</div>
	<div class="formRight">
	<input name="searchTerm" type="text" id="searchTerm"/>
	</div>
</div>

<tiles:insert definition="subjectSearchFragmentDef"/>


<div style="clear:left"><html:submit property="submit.group.member" value="${navMap['find.action.search']}"/></div>
<input type="hidden" name="newSearch" value="Y"/>
</fieldset>
</html:form>
</div>
<a name="endSearch" id="endSearch"></a>
</grouper:recordTile>