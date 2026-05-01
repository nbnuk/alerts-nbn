package uk.org.nbn.alerts

import grails.converters.JSON
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification


class UserControllerSpec extends Specification implements ControllerUnitTest<UserController> {

    def setup() {
    }

    def cleanup() {
    }

    def "test testCache does nothing"() {
        when:
        controller.testCache

        then:
        controller.render([response: true] as JSON)
    }
}
