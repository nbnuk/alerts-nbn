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


    static allowedMethods = [
        list: "GET",
        save: "POST",
        update: ["POST", "PUT"],
        delete: "POST",
        mySavedSearches: "GET"
    ]

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

    @RequireApiKey
    def save() {
        if (!params.userId) {
            flash.errorMessage = "userId is a required parameter to save a search"
            redirect(action: "mySavedSearches", params: [userId: params.userId])  // Add userId to redirect
            return
        }

        def currentUser = User.findByUserId(params.userId)
        if (!currentUser) {
            flash.errorMessage = "User not found"
            redirect(action: "mySavedSearches", params: [userId: params.userId])  // Add userId to redirect
            return
        }


        def savedSearch = savedSearchService.createSavedSearch(
            params.userId,
            params.name,
            params.description,
            params.searchRequestQueryUI
        )

        flash.message = "Search saved successfully"
        redirect(action: "mySavedSearches", params: [userId: params.userId])  // This ensures we go back to My Saved Searches

    }


    @RequireApiKey
    def delete() {
        if (!params.id || !params.userId) {
            response.status = HttpStatus.SC_BAD_REQUEST
            render([error: "id and userId are required parameters", success: false] as JSON)
            return
        }

        savedSearchService.deleteSavedSearch(params.id, params.userId)
        render([success: true, message: "Search deleted successfully"] as JSON)
    }


    @RequireApiKey
    def edit() {
        if (!params.id || !params.userId) {
            response.status = HttpStatus.SC_BAD_REQUEST
            render([error: "id and userId are required parameters"] as JSON)
            return
        }

        def currentUser = User.findByUserId(params.userId)
        if (!currentUser) {
            flash.errorMessage = "Please log in to edit saved searches"
            redirect(action: "mySavedSearches")
            return
        }

        def savedSearch = savedSearchService.getSavedSearchById(params.id, params.userId)

        // Check if the search exists
        if (!savedSearch) {
            flash.errorMessage = "Saved search not found"
            redirect(action: "mySavedSearches")
            return
        }

        // Check if the current user owns this search
        if (savedSearch.userId != currentUser.userId) {
            flash.errorMessage = "You don't have permission to edit this saved search"
            redirect(action: "mySavedSearches")
            return
        }

        [savedSearch: savedSearch]
    }

    def update(Long id) {
        def currentUser = User.findByUserId(params.userId)
        if (!currentUser) {
            flash.errorMessage = "Please log in to update saved searches"
            redirect(action: "mySavedSearches", params: [userId: params.userId])
            return
        }

        def savedSearch = SavedSearch.get(id)

        if (!savedSearch) {
            flash.errorMessage = "Saved search not found"
            redirect(action: "mySavedSearches", params: [userId: params.userId])
            return
        }

        if (savedSearch.userId != currentUser.userId) {
            flash.errorMessage = "You don't have permission to edit this saved search"
            redirect(action: "mySavedSearches", params: [userId: params.userId])
            return
        }

        // Only update allowed fields
        savedSearch.name = params.name
        savedSearch.description = params.description
        savedSearch.searchRequestQueryUI = params.searchRequestQueryUI

        if (savedSearch.save(flush: true)) {
            flash.message = "Saved search updated successfully"
            redirect(action: "mySavedSearches", params: [userId: params.userId])
        } else {
            flash.errorMessage = "Error updating saved search"
            render(view: "edit", model: [savedSearch: savedSearch])
        }
    }

    def mySavedSearches() {
        def currentUser = User.findByUserId(params.userId)
        if (!currentUser) {
            flash.errorMessage = "User not found"
            redirect(uri: "/")
            return
        }

        def savedSearches = savedSearchService.getSavedSearch(params.userId)

        [
            savedSearches: savedSearches,
            user: currentUser,
            adminUser: false  // Set this based on your requirements
        ]
    }

    def create() {
        if (!params.userId) {
            flash.errorMessage = "userId is required"
            redirect(uri: "/")
            return
        }

        def currentUser = User.findByUserId(params.userId)
        if (!currentUser) {
            flash.errorMessage = "User not found"
            redirect(uri: "/")
            return
        }

        render(view: "create", model: [userId: params.userId])
    }
}
