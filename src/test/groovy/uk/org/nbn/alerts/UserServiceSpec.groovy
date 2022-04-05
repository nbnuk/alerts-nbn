package uk.org.nbn.alerts

import au.org.ala.alerts.Frequency
import au.org.ala.alerts.Notification
import au.org.ala.alerts.Query
import au.org.ala.alerts.User
import au.org.ala.web.AuthService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(UserService)
@Mock([User, Notification, Query, Frequency])
class UserServiceSpec extends Specification {



    def "test getUserAlertsConfig"() {
        setup:
        new Frequency(name:"hourly", periodInSeconds:60).save(flush: true, failOnError: true)
        new Frequency(name:"daily", periodInSeconds:3600).save(flush: true, failOnError: true)

        User u1 = new User(userId: 1, "email": "a@test.com").save(flush: true, failOnError: true)

        def queries = [
                new Query(name: "New images", custom: false, resourceName: "1", updateMessage: "1", baseUrl: "1", baseUrlForUI: "1", queryPathForUI: "1", queryPath: "1").save(flush: true, failOnError: true),
                new Query(name: "Citizen science records with images", custom: false, resourceName: "1", updateMessage: "1", baseUrl: "1", baseUrlForUI: "1", queryPathForUI: "1", queryPath: "1").save(flush: true, failOnError: true),
                new Query(name: "Blogs and News", custom: false, resourceName: "1", updateMessage: "1", baseUrl: "1", baseUrlForUI: "1", queryPathForUI: "1", queryPath: "1").save(flush: true, failOnError: true),
                new Query(name: "Blogs and News", custom: false, resourceName: "1", updateMessage: "1", baseUrl: "1", baseUrlForUI: "1", queryPathForUI: "1", queryPath: "1").save(flush: true, failOnError: true),
                new Query(name: "Query1", custom: false, resourceName: "1", updateMessage: "1", baseUrl: "1", baseUrlForUI: "1", queryPathForUI: "1", queryPath: "1").save(flush: true, failOnError: true),

                new Query(name: "New images", custom: true, resourceName: "1", updateMessage: "1", baseUrl: "1", baseUrlForUI: "1", queryPathForUI: "1", queryPath: "1").save(flush: true, failOnError: true),
                new Query(name: "Citizen science records with images", custom: true, resourceName: "1", updateMessage: "1", baseUrl: "1", baseUrlForUI: "1", queryPathForUI: "1", queryPath: "1").save(flush: true, failOnError: true),
                new Query(name: "Blogs and News", custom: true, resourceName: "1", updateMessage: "1", baseUrl: "1", baseUrlForUI: "1", queryPathForUI: "1", queryPath: "1").save(flush: true, failOnError: true),
                new Query(name: "Blogs and News", custom: true, resourceName: "1", updateMessage: "1", baseUrl: "1", baseUrlForUI: "1", queryPathForUI: "1", queryPath: "1").save(flush: true, failOnError: true),
                new Query(name: "Query2", custom: true, resourceName: "1", updateMessage: "1", baseUrl: "1", baseUrlForUI: "1", queryPathForUI: "1", queryPath: "1").save(flush: true, failOnError: true)
        ]
                .each {
                    new Notification(user: u1, query: it).save(flush: true, failOnError: true)
                }

        when:
        def map = service.getUserAlertsConfig(u1)

        then:
        map.disabledQueries == []
        map.enabledQueries == [queries[0],queries[1],queries[4]]
        map.customQueries == [queries[5],queries[6],queries[9]]
    }

    def "test get user when not logged in"() {
        setup:
        service.authService = Mock(AuthService)
        service.authService.userDetails() >> null

        when:
        def user = service.getUser()

        then:
        user == null
    }

    def "test get user when user logged in and already on alerts system"() {
        setup:
        def userDetails = [userId: 1]
        User u1 = new User(userId: 1, "email": "a@test.com").save(flush: true, failOnError: true)
        Query q1 =new Query(name: "Blogs and News", resourceName: "1", updateMessage: "1", baseUrl: "1", baseUrlForUI: "1", queryPathForUI: "1", queryPath: "1").save(flush: true, failOnError: true)
        new Notification(user: u1, query: q1).save(flush: true, failOnError: true)
        Query q2 =new Query(name: "New images", resourceName: "1", updateMessage: "1", baseUrl: "1", baseUrlForUI: "1", queryPathForUI: "1", queryPath: "1").save(flush: true, failOnError: true)
        new Notification(user: u1, query: q2).save(flush: true, failOnError: true)
        service.authService = Mock(AuthService)
        service.authService.userDetails() >> userDetails

        when:
        def user = service.getUser()

        then:
        def notifications =  Notification.findAllByUser(user)
        notifications.size() == 1
        notifications.get(0).query == q2

    }

    def "test get user when user is not on alerts system"() {
        setup:
        def userDetails = [userId: 1, email:"1@a.com"]
        service.authService = Mock(AuthService)
        service.authService.userDetails() >> userDetails

        when:
        def user = service.getUser()

        then:
        User.findByUserId(1)
        Notification.findAllByUser(user).size() == 0

    }
}
