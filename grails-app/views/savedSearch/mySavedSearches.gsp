<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="uk.org.nbn.alerts.SavedSearch" %>
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
        <style>
            .table-custom-width {
                width: 100%;
            }
            .table-custom-width th:nth-child(1),
            .table-custom-width th:nth-child(2),
            .table-custom-width th:nth-child(3) {
                width: 26.67%; /* 80% / 3 */
            }
            .table-custom-width th:nth-child(4) {
                width: 20%;
            }
        </style>
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
                    <div class="table-responsive">
                        <table class="table table-striped table-custom-width">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Description</th>
                                    <th>Query</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody id="savedSearches">
                            <g:each in="${savedSearches}" status="i" var="search">
                                <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" id='search-${search.id}'>
                                    <td data-label="Name">${search.name}</td>
                                    <td data-label="Description">${search.description}</td>
                                    <td data-label="Query">
                                        <textarea rows="3" readonly class="form-control">${search.searchRequestQueryUI}</textarea>
                                    </td>
                                    <td data-label="Actions" class="searchActions">
                                        <div class="d-flex flex-column flex-sm-row">
                                            <g:link class="btn btn-primary btn-sm mb-2 mb-sm-0 me-sm-2"
                                                    controller="savedSearch"
                                                    action="edit"
                                                    id="${search.id}"
                                                    params="[id: search.id, userId: params.userId]">
                                                Edit
                                            </g:link>
                                            <button class="btn btn-danger btn-sm deleteSearch" data-id="${search.id}">Delete</button>
                                        </div>
                                    </td>
                                </tr>
                            </g:each>
                            </tbody>
                        </table>
                    </div>
                    <button id="addNewSearch" class="btn btn-success">Add New Search</button>
                 </div>
            </div>
          </div>
      </div>
      <asset:javascript src="alerts.js"/>
      <asset:script type="text/javascript">
        $(document).ready(function() {
            // Use absolute URL path for delete
            const deleteUrl = '${createLink(absolute: true, controller: "savedSearch", action: "delete")}';
            const createUrl = '${createLink(absolute: true, controller: "savedSearch", action: "create", params: [userId: params.userId])}';  // Add userId here
            const listUrl = '${createLink(absolute: true, controller: "savedSearch", action: "list")}';

            function refreshTable() {
                $.get(listUrl, function(data) {
                    $('#savedSearches').html(data);
                });
            }

            $('#addNewSearch').click(function() {
                window.location.href = createUrl;  // This will now include the userId
            });

            $(document).on('click', '.deleteSearch', function() {
                const searchId = $(this).data('id');
                if (confirm('Are you sure you want to delete this saved search?')) {
                    $.ajax({
                        url: deleteUrl + '/' + searchId,
                        method: 'POST',
                        data: {
                            userId: '${params.userId}',
                            id: searchId
                        },
                        success: function(response) {
                            if (response.success) {
                                $('#search-' + searchId).remove();
                                // Add flash message
                                const flashMessage = '<div class="alert alert-info">' + response.message + '</div>';
                                // Remove any existing flash messages
                                $('.alert').remove();
                                // Add the new flash message after the header
                                $('#page-header').after(flashMessage);
                            } else {
                                alert('Error deleting saved search: ' + response.message);
                            }
                        },
                        error: function(xhr, status, error) {
                            console.error('Delete error:', xhr.responseText);
                            alert('An error occurred while deleting the saved search: ' + error);
                        }
                    });
                }
            });
        });
      </asset:script>
    </body>
</html>
