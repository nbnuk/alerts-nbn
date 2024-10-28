package uk.org.nbn.alerts

import au.org.ala.alerts.User
import grails.gorm.transactions.Transactional

class SavedSearchService {

    def createSavedSearch(String userId, String name, String description, String searchRequestQueryUI) {

            // Trim and validate input parameters
            name = name?.trim()
            description = description?.trim()
            searchRequestQueryUI = searchRequestQueryUI?.trim()

            // TODO do we want to enforce this ? check if a saved search with the same name already exists for this user
            // if (SavedSearch.findByUserIdAndName(userId, name)) {
            //     throw new IllegalArgumentException("A saved search with this name already exists")
            // }

            def savedSearch = new SavedSearch(
                userId: userId,
                name: name,
                description: description,
                searchRequestQueryUI: searchRequestQueryUI
            )

            savedSearch.save(flush: true, failOnError: true)
            log.info("Saved search created for user ${userId}")
            return savedSearch
    }

    def getSavedSearch(String userId) {
            SavedSearch.findAllByUserId(userId)
    }

    def updateSavedSearch(String id, String userId, String name, String description, String searchRequestQueryUI) {
        def savedSearch = SavedSearch.findByIdAndUserId(id, userId)
        if (!savedSearch) {
            throw new IllegalArgumentException("Saved search not found")
        }

        savedSearch.name = name
        savedSearch.description = description?.trim()
        savedSearch.searchRequestQueryUI = searchRequestQueryUI?.trim()

        savedSearch.save(flush: true, failOnError: true)
        log.info("Saved search updated for user ${userId}")
        return savedSearch
    }

    def deleteSavedSearch(String id, String userId) {
        def savedSearch = SavedSearch.findByIdAndUserId(id, userId)
        if (savedSearch) {
            savedSearch.delete(flush: true)
        } else {
            log.info("Saved search not found for user ${userId}")
        }
        log.info("Saved search deleted for user ${userId}")
    }

    def getSavedSearchById(String id, String userId) {
        SavedSearch.findByIdAndUserId(id, userId)
    }
}
