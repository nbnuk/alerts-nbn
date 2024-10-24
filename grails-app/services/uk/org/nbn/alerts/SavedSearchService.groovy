package uk.org.nbn.alerts

import au.org.ala.alerts.Notification
import au.org.ala.alerts.Query
import au.org.ala.alerts.User
import au.org.ala.web.UserDetails
import grails.gorm.transactions.Transactional

@Transactional
class SavedSearchService {

    def createSavedSearch(String userId, String name, String description, String searchRequestQueryUI) {
        if (!userId) {
            log.error("Attempt to create saved search with null userId")
            throw new IllegalArgumentException("userId cannot be null")
        }

        try {
            def savedSearch = new SavedSearch(
                userId: userId,
                name: name,
                description: description?.trim(),
                searchRequestQueryUI: searchRequestQueryUI?.trim()
            )

            if (!savedSearch.validate()) {
                log.error("Invalid saved search: ${savedSearch.errors}")
                throw new IllegalArgumentException("Invalid saved search data")
            }

            savedSearch.save(flush: true, failOnError: true)
            log.info("Saved search created for user ${userId}")
            return savedSearch
        } catch (Exception e) {
            log.error("Error creating saved search for user ${userId}", e)
            throw new RuntimeException("Failed to create saved search", e)
        }
    }

    def getSavedSearch(String userId) {
        if (userId) {
            try {
                return SavedSearch.findAllByUserId(userId)
            } catch (Exception e) {
                log.error("Error retrieving saved searches for user ${userId}", e)
                return []
            }
        } else {
            log.warn("Attempt to retrieve saved searches with null userId")
            return []
        }
    }

    def updateSavedSearch(String id, String userId, String name, String description, String searchRequestQueryUI) {
        def savedSearch = SavedSearch.findByIdAndUserId(id, userId)
        if (!savedSearch) {
            throw new IllegalArgumentException("Saved search not found")
        }

        savedSearch.name = name
        savedSearch.description = description?.trim()
        savedSearch.searchRequestQueryUI = searchRequestQueryUI?.trim()

        if (!savedSearch.validate()) {
            log.error("Invalid saved search update: ${savedSearch.errors}")
            throw new IllegalArgumentException("Invalid saved search data")
        }

        savedSearch.save(flush: true, failOnError: true)
        log.info("Saved search updated for user ${userId}")
        return savedSearch
    }

    def deleteSavedSearch(String id, String userId) {
        def savedSearch = SavedSearch.findByIdAndUserId(id, userId)
        if (!savedSearch) {
            throw new IllegalArgumentException("Saved search not found")
        }

        savedSearch.delete(flush: true)
        log.info("Saved search deleted for user ${userId}")
    }

    def getSavedSearchById(String id, String userId) {
        return SavedSearch.findByIdAndUserId(id, userId)
    }
}
