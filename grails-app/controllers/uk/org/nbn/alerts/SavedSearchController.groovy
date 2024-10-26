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

    static allowedMethods = [list: "GET", save: "POST", update: "POST", delete: "POST"]

    def savedSearchService
    def authService

    /**
     * List all saved searches for the current user
     *
     * The method requires that the user is logged on, so that must be enforced
     * eg by using annotation or however else the application enforces authentication
     */
    @RequireApiKey
    def list() {
        if (!params.userId) {
            flash.errorMessage = "userId is a required parameter"
            redirect(action: "index")
            return
        }

        def user = User.findByUserId(params.userId)
        if (!user) {
            flash.errorMessage = "User not found"
            redirect(action: "index")
            return
        }

        def savedSearches = savedSearchService.getSavedSearch(params.userId)
        render savedSearches as JSON
    }

    @RequireApiKey
    def save() {
        if (!params.userId) {
            response.status = HttpStatus.SC_BAD_REQUEST
            render([error: "userId is a required parameter to save a search"] as JSON)
            return
        }

        def user = User.findByUserId(params.userId)
        if (!user) {
            response.status = HttpStatus.SC_BAD_REQUEST
            render([error: "userId not found"] as JSON)
            return
        }

        def savedSearch = savedSearchService.createSavedSearch(user.userId, params.name, params.description, params.searchRequestQueryUI)
        render([success: true, id: savedSearch.id] as JSON)
    }



    @RequireApiKey
    def update() {
        if (!params.id || !params.userId) {
            response.status = HttpStatus.SC_BAD_REQUEST
            render([error: "id and userId are required parameters"] as JSON)
            return
        }

        def updatedSearch = savedSearchService.updateSavedSearch(params.id, params.userId, params.name, params.description, params.searchRequestQueryUI)
        render([success: true, id: updatedSearch.id] as JSON)

    }

    @RequireApiKey
    def delete() {
        if (!params.id || !params.userId) {
            response.status = HttpStatus.SC_BAD_REQUEST
            render([error: "id and userId are required parameters"] as JSON)
            return
        }

        savedSearchService.deleteSavedSearch(params.id, params.userId)
        render([success: true] as JSON)

    }

 @RequireApiKey
    def edit() {
        if (!params.id || !params.userId) {
            response.status = HttpStatus.SC_BAD_REQUEST
            render([error: "id and userId are required parameters"] as JSON)
            return
        }

            def savedSearch = savedSearchService.getSavedSearchById(params.id, params.userId)
            if (savedSearch) {
                render(savedSearch as JSON)
            } else {
                response.status = HttpStatus.SC_NOT_FOUND
                render([error: "Saved search not found"] as JSON)
            }

    }

    @RequireApiKey
    def create() {
        // This action is for rendering the create form
        // The actual creation is handled by the 'save' action
        render(view: "create")
    }
    
}
