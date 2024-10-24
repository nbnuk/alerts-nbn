<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="au.org.ala.alerts.Notification" %>
<!doctype html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="${grailsApplication.config.skin.layout}" />
  <meta name="breadcrumb" content="My alerts" />
  <meta name="breadcrumbParent" content="${grailsApplication.config.security.cas.casServerName}/userdetails/myprofile, My profile" />
  <g:set var="userPrefix" value="${adminUser ? user.email : 'My' }"/>
  <title>${userPrefix} email alerts | ${grailsApplication.config.skin.orgNameLong}</title>
  <asset:stylesheet href="alerts.css"/>
</head>
<body>
<div id="content">
  <header id="page-header">
    <div class="inner row-fluid">
      <h1>My saved searches</h1>
    </div>
  </header>
  <g:if test="${flash.message}">
    <div class="alert alert-info">${flash.message}</div>
  </g:if>
  <g:if test="${flash.errorMessage}">
    <div class="alert alert-danger">${flash.errorMessage}</div>
  </g:if>
  <div id="page-body" role="main">
    <div class="list">
      <table>
        <thead>
        <tr>
          <th>Saved Search</th>
          <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${savedSearches}" status="i" var="savedSearch">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
            <td>${fieldValue(bean: savedSearch, field: "description")}
            <div class="scroll"><a href="${savedSearch.baseUrlForUI}${savedSearch.queryPathForUI}">${fieldValue(bean: savedSearch, field: "baseUrlForUI")}${fieldValue(bean: savedSearch, field: "queryPathForUI")}</a></div></td>
            </td>
            <td>delete</td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
  </div>
</div>


</body>
</html>