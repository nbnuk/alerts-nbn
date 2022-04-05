package uk.org.nbn.alerts

import grails.converters.JSON

class UserController extends au.org.ala.alerts.UserController {
    /**
    * Test EhCache caching in UserService = check logs to see if userService.testEhCache()
    * method internals are run or not (5 min cache expiry).
    */
    @Override
    def testCache() {
        render ([response: true] as JSON)
    }
}
