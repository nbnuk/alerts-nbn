package uk.org.nbn.alerts

import au.org.ala.alerts.Query
import au.org.ala.alerts.PropertyPath

class BootStrap extends au.org.ala.alerts.BootStrap{


    def init = { servletContext ->
        log.info("NBN Running bootstrap queries.")
        preloadQueries()
        log.info("NBN Done bootstrap queries.")
    }

    private void preloadQueries() {
        log.info("NBN start of preloadQueries")

        def queries = Query.findAllByNameAndDescription('Annotations','Notify me when annotations are made on any record.')

        queries.each {
            if (!it.queryPath.indexOf("fq=user_assertions:*")) {
                it.setQueryPath('/occurrences/search?fq=user_assertions:*&q=user_assertions:true AND last_assertion_date:[___DATEPARAM___%20TO%20*]&sort=last_assertion_date&dir=desc&pageSize=20&facets=basis_of_record')
                it.setQueryPathForUI('/occurrences/search?fq=user_assertions:*&q=last_assertion_date:[___DATEPARAM___%20TO%20*]&sort=last_assertion_date&dir=desc')
                it.save(flush: true)
            }
        }

        log.info("NBN end of preloadQueries")
    }

    def destroy = {}
}
