<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="${grailsApplication.config.skin.layout}" />
        <meta name="breadcrumb" content="Create saved search" />
        <meta name="breadcrumbParent" content="${createLink(action: 'mySavedSearches')}, My saved searches" />
        <title>Create saved search | ${grailsApplication.config.skin.orgNameLong}</title>
        <asset:stylesheet href="alerts.css"/>
    </head>
    <body>
        <div id="content">
            <header id="page-header">
                <div class="inner row-fluid">
                    <h1>Create saved search</h1>
                </div>
            </header>
            <div id="page-body" role="main">
                <div class="row mt-4">
                    <div class="col-md-10 col-md-offset-1">
                        <g:if test="${flash.message}">
                            <div class="alert alert-info">${flash.message}</div>
                        </g:if>
                        <g:if test="${flash.errorMessage}">
                            <div class="alert alert-danger">${flash.errorMessage}</div>
                        </g:if>

                        <g:form action="save" method="POST" class="form-horizontal">

                            <div class="form-group mb-3">
                                <label for="name" class="form-label">Name</label>
                                <g:textField name="name" value="${savedSearch?.name}" class="form-control" required=""/>
                            </div>

                            <div class="form-group mb-3">
                                <label for="description" class="form-label">Description</label>
                                <g:textArea name="description" value="${savedSearch?.description}" class="form-control" rows="3"/>
                            </div>

                            <div class="form-group mb-3">
                                <label for="searchRequestQueryUI" class="form-label">Query</label>
                                <g:textArea name="searchRequestQueryUI" value="${savedSearch?.searchRequestQueryUI}" class="form-control" rows="3" required=""/>
                            </div>

                            <div class="form-group">
                                <div class="d-flex gap-2">
                                    <g:submitButton name="create" class="btn btn-primary" value="Create"/>
                                    <g:link action="mySavedSearches" params="[userId: params.userId]" class="btn btn-secondary">Cancel</g:link>
                                </div>
                            </div>
                        </g:form>
                    </div>
                </div>
            </div>
        </div>
        <asset:javascript src="alerts.js"/>
    </body>
</html>
