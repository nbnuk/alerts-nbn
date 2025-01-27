package uk.org.nbn.alerts

import au.ala.org.ws.security.RequireApiKey
import au.org.ala.alerts.Notification
import au.org.ala.alerts.User
import grails.converters.JSON
import org.apache.http.HttpStatus



/**
 * Controller for managing saved searches.
 */
class SavedSearchController {

    def userService

    static allowedMethods = [
        listAsync: "GET",
        saveAsync: "POST",
        create: "GET",
        save: "POST",
        edit: "GET",
        update: ["POST", "PUT"],
        delete: "POST",
        mySavedSearches: "GET"
    ]

    def savedSearchService
    def authService

    /**
     * List all saved searches for a user
     *
     * This is an internal call, secured by an api key, hence
     * the userId can be sent as a parameter
     *
     * /api/savedSearch/list/$userId
     */
    @RequireApiKey
    def listAsync() {
        if (!params.userId) {
            response.status = HttpStatus.SC_BAD_REQUEST
            render([error: "userId is a required parameter"] as JSON)
            return
        }

        def user = User.findByUserId(params.userId)
        if (!user) {
            response.status = HttpStatus.SC_NOT_FOUND
            render([error: "User not found"] as JSON)
            return
        }

        def savedSearches = savedSearchService.getSavedSearch(params.userId)
        render savedSearches as JSON
    }

    /**
     * Save a saved search for a user
     *
     * This is an internal call, secured by an api key,  hence
     * the userId can be sent as a parameter.
     *
     * It creates a saved search for a user
     *
     * /api/savedSearch/save
     */
    @RequireApiKey
    def saveAsync() {
        if (!params.userId) {
            return response.sendError(HttpStatus.SC_BAD_REQUEST, "userId is a required parameter")
        }

        def currentUser = User.findByUserId(params.userId)
        if (!currentUser) {
            return response.sendError(HttpStatus.SC_NOT_FOUND, "User not found")
        }


        def savedSearch = savedSearchService.createSavedSearch(
            params.userId,
            params.name,
            params.description,
            params.searchRequestQueryUI
        )

        render status: HttpStatus.SC_CREATED

    }


    /**
     * Delete a saved search
     *
     * (ajax endpoint)
     *
     */
    def delete() {
        User currentUser = userService.getUser()

        if (!currentUser) {
            return response.sendError(HttpStatus.SC_UNAUTHORIZED, "Please login again")
        }

        if (!params.id) {
            return response.sendError(HttpStatus.SC_BAD_REQUEST, "id is a required parameter")
        }

        savedSearchService.deleteSavedSearch(params.id, currentUser.userId)

        render([success: true, message: "Search deleted successfully"] as JSON)
    }

    //GET for edit/update form
    def edit() {

        if (!params.id) {
            return response.sendError(HttpStatus.SC_BAD_REQUEST, "id is a required parameter")
        }

        User currentUser = userService.getUser()

        def savedSearch = savedSearchService.getSavedSearchById(params.id, currentUser.userId)

        // Check if the search exists
        if (!savedSearch) {
            log.debug("Saved search not found for id: ${params.id} and userId: ${currentUser.userId}")
            flash.errorMessage = "Saved search not found"
            redirect(action: "mySavedSearches")
            return
        }


        [savedSearch: savedSearch]
    }

    /**
     * handle update from the edit form page. On success return to mySavedSearches web page
     */
    def update(Long id) {
        User currentUser = userService.getUser()

        savedSearchService.updateSavedSearch(
                id,
                currentUser.userId,
                params.name,
                params.description,
                cleanUpURL(params.searchRequestQueryUI)
        )


        flash.message = "Saved search updated successfully"
        redirect(action: "mySavedSearches")

    }

    /**
     * mySavedSearches web page
     */
    def mySavedSearches() {
        User currentUser = userService.getUser()

        def savedSearches = savedSearchService.getSavedSearch(currentUser.userId)

        [
            savedSearches: savedSearches,
            user: currentUser,
            adminUser: false
        ]
    }

    def create() {

    }


    def save() {

        User currentUser = userService.getUser()

        def savedSearch = savedSearchService.createSavedSearch(
                currentUser.userId,
                params.name,
                params.description,
                cleanUpURL(params.searchRequestQueryUI)
        )

        flash.message = "Search saved successfully"
        redirect(action: "mySavedSearches")  // This ensures we go back to My Saved Searches

    }

    private cleanUpURL(url){
        if (!url) {
            return url
        }
        def resultUrl = url;

        resultUrl = resultUrl.replace('?nbn_loading=true&', '?')
        resultUrl = resultUrl.replace('?nbn_loading=true', '')
        resultUrl = resultUrl.replace('&nbn_loading=true', '')

        //add fq if missing
        if (resultUrl && !resultUrl.contains('?fq=') && !resultUrl.contains('&fq=')) {
            // Determine whether to add `?` or `&` based on the URL
            if (url.contains('?')) {
                resultUrl = resultUrl += '&fq='
            } else {
                resultUrl = resultUrl += '?fq='
            }
        }
        return resultUrl;
    }
}
