package uk.org.nbn.alerts


import grails.testing.web.controllers.ControllerUnitTest
import org.apache.http.HttpStatus
import spock.lang.Specification


class AdminControllerSpec extends Specification implements ControllerUnitTest<AdminController> {

    def setup() {
    }

    def cleanup() {
    }

    void "test repairNotificationsWithoutUnsubscribeToken returns not found"() {
        when:
        controller.repairNotificationsWithoutUnsubscribeToken()

        then:
        response.status == HttpStatus.SC_NOT_FOUND
    }

    void "test repairUsersWithoutUnsubscribeToken returns not found"() {
        when:
        controller.repairUsersWithoutUnsubscribeToken()

        then:
        response.status == HttpStatus.SC_NOT_FOUND
    }
}
