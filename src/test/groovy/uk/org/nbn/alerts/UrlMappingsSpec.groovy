package uk.org.nbn.alerts

import spock.lang.Specification
import grails.testing.web.UrlMappingsUnitTest

class UrlMappingsSpec extends Specification implements UrlMappingsUnitTest<UrlMappings>{

    void setup() {
        mockController(WebserviceController)
    }

    void "test webservice mappings"() {

        expect:
        verifyForwardUrlMapping("/wsopen/alerts/user/1", controller: 'webservice', action: 'getUserAlertsOutsideCAS', method: 'GET') {
            userId = '1'
        }

    }
}
