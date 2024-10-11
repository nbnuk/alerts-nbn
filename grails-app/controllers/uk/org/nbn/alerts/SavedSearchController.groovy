package uk.org.nbn.alerts

import org.apache.http.HttpStatus

class SavedSearchController {

    static allowedMethods = [list: "GET", create: "POST"]

    def savedSearchService
    def authService

    /**
     * List all saved searches for the current user
     * /savedSearch/list
     * The method requires that the user is logged on, so that must be enforced
     * eg by using annotation or however else the application enforces authentication
     */
    def list() {
        def userDetails = authService.userDetails()
        ["savedSearches", savedSearchService.getSavedSearch(userDetails.userId)]
    }


    @RequireApiKey
    def create() {
        def user = authService.getUser(prams.userId)
        if (user) {
            savedSearchService.createSavedSearch(user.userId, params.description, params.searchRequestQueryUI)
            response.status = HttpStatus.SC_OK
        } else {
            response.sendError(HttpStatus.SC_NOT_FOUND, "Unable to find user with userId ${params.userId}")
        }

    }

}
