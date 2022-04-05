package uk.org.nbn.alerts

import grails.test.mixin.TestFor
import org.apache.http.HttpStatus
import spock.lang.Specification

@TestFor(AdminController)
class AdminControllerSpec extends Specification {

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
