package uk.org.nbn.alerts

import grails.converters.JSON
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(UserController)
class UserControllerSpec extends Specification  {

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
