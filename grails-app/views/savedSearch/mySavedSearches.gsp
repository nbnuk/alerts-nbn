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
        <asset:stylesheet href="mySavedSearches.css"/>
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
                    <button id="addNewSearch" class="btn btn-success mb-4">
                        <i class="fas fa-plus"></i> Add New Search
                    </button>

                    <g:if test="${!savedSearches}">
                        <div class="empty-state">
                            <h3>No Saved Searches Yet</h3>
                            <p class="text-muted">Create your first saved search to get started!</p>
                            <button id="addFirstSearch" class="btn btn-primary mt-3">
                                <i class="fas fa-plus"></i> Create First Search
                            </button>
                        </div>
                    </g:if>
                    <g:else>
                        <div class="table-responsive">
                            <table class="table table-hover table-custom-width">
                                <thead class="thead-light">
                                    <tr>
                                        <th>Name</th>
                                        <th>Description</th>
                                        <th>Query</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody id="savedSearches">
                                <g:each in="${savedSearches}" status="i" var="search">
                                    <tr class="search-card" id='search-${search.id}'>
                                        <td data-label="Name">
                                            <strong>${search.name}</strong>
                                        </td>
                                        <td data-label="Description">
                                            ${search.description}
                                        </td>
                                        <td data-label="Query">
                                            <textarea rows="3" readonly
                                                class="form-control query-textarea"
                                                >${search.searchRequestQueryUI}</textarea>
                                        </td>
                                        <td data-label="Actions" class="action-buttons">
                                            <div class="btn-group">
                                                <g:link class="btn btn-outline-primary btn-sm"
                                                        controller="savedSearch"
                                                        action="edit"
                                                        id="${search.id}"
                                                        params="[id: search.id, userId: params.userId]">
                                                    <i class="fas fa-edit"></i> Edit
                                                </g:link>
                                                <button class="btn btn-outline-danger btn-sm deleteSearch"
                                                        data-id="${search.id}">
                                                    <i class="fas fa-trash"></i> Delete
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                </g:each>
                                </tbody>
                            </table>
                        </div>
                    </g:else>
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
