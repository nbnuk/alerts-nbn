package uk.org.nbn.alerts

import au.org.ala.alerts.Notification
import au.org.ala.alerts.Query
import au.org.ala.alerts.User
import grails.converters.JSON
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.apache.http.HttpStatus
import spock.lang.Specification


@TestFor(WebserviceController)
@Mock([User, Notification, Query])
class WebserviceControllerSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test get User Alerts when userID is null"() {
        when:
        controller.getUserAlertsOutsideCAS()

        then:
        response.status == HttpStatus.SC_BAD_REQUEST
        response.getErrorMessage() == "userId is a required parameter"
    }


    void "test get User Alerts when user not found"() {

        setup:
        User.findByUserId(1) >> null

        when:
        params.userId = 1
        controller.getUserAlertsOutsideCAS()

        then:
        response.status == HttpStatus.SC_BAD_REQUEST
        response.getErrorMessage() == "userId not found"
    }

    void "test get User Alerts when user is found"() {

        setup:
        User u1 = new User(userId: 1, "email": "a@test.com").save(flush: true, failOnError: true)
        Query q1 = new Query(name: "q1", resourceName: "1", updateMessage: "1", baseUrl: "1", baseUrlForUI: "1", queryPathForUI: "1", queryPath: "1").save(flush: true, failOnError: true)
        Query q2 = new Query(name: "q2", resourceName: "1", updateMessage: "1", baseUrl: "1", baseUrlForUI: "1", queryPathForUI: "1", queryPath: "1").save(flush: true, failOnError: true)
        Notification n1 = new Notification(user: u1, query: q1).save(flush: true, failOnError: true)
        Notification n2 = new Notification(user: u1, query: q2).save(flush: true, failOnError: true)

        when:
        params.userId = 1
        controller.getUserAlertsOutsideCAS()

        then:
        response.status == HttpStatus.SC_OK
        controller.render(["q1", "q2"] as JSON)

    }
}
