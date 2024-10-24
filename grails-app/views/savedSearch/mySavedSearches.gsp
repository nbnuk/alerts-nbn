<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="au.org.ala.alerts.SavedSearch" %>
<!doctype html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="${grailsApplication.config.skin.layout}" />
        <meta name="breadcrumb" content="My saved searches" />
        <meta name="breadcrumbParent" content="${grailsApplication.config.security.cas.casServerName}/userdetails/myprofile, My profile" />
        <g:set var="userPrefix" value="${adminUser ? user.email : 'My' }"/>
        <title>${userPrefix} saved searches | ${grailsApplication.config.skin.orgNameLong}</title>
        <asset:stylesheet href="alerts.css"/>
    </head>
    <body>
      <div id="content">
          <header id="page-header">
            <div class="inner row-fluid">
              <h1>${userPrefix} saved searches</h1>
            </div>
          </header>
          <g:if test="${flash.message}">
              <div class="alert alert-info">${flash.message}</div>
          </g:if>
          <g:if test="${flash.errorMessage}">
              <div class="alert alert-danger">${flash.errorMessage}</div>
          </g:if>
          <div id="page-body" role="main">
            <div class="row">
                <div class="col-md-12">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>Search Name</th>
                                <th>Description</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody id="savedSearches">
                        <g:each in="${savedSearches}" status="i" var="search">
                            <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" id='search-${search.id}'>
                                <td>${search.name}</td>
                                <td>${search.description}</td>
                                <td class="searchActions">
                                    <button class="btn btn-primary btn-sm editSearch" data-id="${search.id}">Edit</button>
                                    <button class="btn btn-danger btn-sm deleteSearch" data-id="${search.id}">Delete</button>
                                </td>
                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                    <button id="addNewSearch" class="btn btn-success">Add New Search</button>
                 </div>
            </div>
          </div>
      </div>
      <asset:javascript src="alerts.js"/>
      <asset:script type="text/javascript">
        $(document).ready(function() {
            const baseUrl = '${createLink(controller: "savedSearch", action: "")}';

            function refreshTable() {
                $.get(baseUrl + '/list', function(data) {
                    $('#savedSearches').html(data);
                });
            }

            $('#addNewSearch').click(function() {
                // Open a modal or redirect to a new page for adding a search
                window.location.href = baseUrl + '/create';
            });

            $(document).on('click', '.editSearch', function() {
                const searchId = $(this).data('id');
                window.location.href = baseUrl + '/edit/' + searchId;
            });

            $(document).on('click', '.deleteSearch', function() {
                const searchId = $(this).data('id');
                if (confirm('Are you sure you want to delete this saved search?')) {
                    $.ajax({
                        url: baseUrl + '/delete/' + searchId,
                        method: 'POST',
                        success: function(response) {
                            if (response.success) {
                                $('#search-' + searchId).remove();
                            } else {
                                alert('Error deleting saved search: ' + response.message);
                            }
                        },
                        error: function() {
                            alert('An error occurred while deleting the saved search.');
                        }
                    });
                }
            });

            // Refresh the table periodically (optional)
            // setInterval(refreshTable, 30000);
        });
      </asset:script>
    </body>
</html>
