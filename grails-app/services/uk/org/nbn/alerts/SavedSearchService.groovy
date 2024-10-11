package uk.org.nbn.alerts

import au.org.ala.alerts.Notification
import au.org.ala.alerts.Query
import au.org.ala.alerts.User
import au.org.ala.web.UserDetails
import grails.gorm.transactions.Transactional

@Transactional
class SavedSearchService {


    def createSavedSearch(String userId, String description, String searchRequestQueryUI) {
        def savedSearch = new SavedSearch(userId:userId, description: description, searchRequestQueryUI: searchRequestQueryUI)
        savedSearch.save(flush:true, failOnError:true)
    }

    def getSavedSearch(String userId) {
        return SavedSearch.findAllByUserId(userId)
    }

}
