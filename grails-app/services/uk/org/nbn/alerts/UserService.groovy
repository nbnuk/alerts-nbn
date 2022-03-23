package uk.org.nbn.alerts

import au.org.ala.alerts.User
import au.org.ala.alerts.Query
import au.org.ala.alerts.Notification
import grails.gorm.transactions.Transactional

@Transactional
class UserService extends au.org.ala.alerts.UserService{

    def getUserAlertsConfig(User user) {

        def map = super.getUserAlertsConfig(user)
        map.disabledQueries.removeAll { it.name == 'New images' || it.name == 'Citizen science records with images' || it.name == 'Blogs and News' }
        map.enabledQueries.removeAll { it.name == 'Blogs and News' } //force remove this as it was turned on by default; other hidden entries that the user has previously turned on will be shown though
        map.customQueries.removeAll { it.name == 'Blogs and News' } //force remove this as it was turned on by default; other hidden entries that the user has previously turned on will be shown though
        map
    }

    User getUser(userDetailsParam = null) {
        def user = super.getUser(userDetailsParam)
        def query  = Query.findByName("Blogs and News")
        def notification = Notification.findByUserAndQuery(user,query);
        if (notification) {
            notification.delete(flush: true)
        }
        user
    }

}
