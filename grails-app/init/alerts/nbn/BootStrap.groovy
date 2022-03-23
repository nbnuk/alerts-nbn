package uk.org.nbn.alerts

import au.org.ala.alerts.Query
import au.org.ala.alerts.PropertyPath

class BootStrap extends au.org.ala.alerts.BootStrap{


    def init = { servletContext ->
        log.info("Running nbn bootstrap queries.")
        preloadQueries()
        log.info("Done nbn bootstrap queries.")
        super.preloadQueries()
    }

    private void preloadQueries() {
        if(Query.findAllByName('Annotations').isEmpty()){
            Query newAssertions = (new Query([
                    baseUrl: grailsApplication.config.biocacheService.baseURL,
                    baseUrlForUI: grailsApplication.config.biocache.baseURL,
                    resourceName:  grailsApplication.config.postie.defaultResourceName,
                    name: 'Annotations',
                    updateMessage: 'annotations.update.message',
                    description: 'Notify me when annotations are made on any record.',
                    queryPath: '/occurrences/search?fq=user_assertions:*&q=user_assertions:true AND last_assertion_date:[___DATEPARAM___%20TO%20*]&sort=last_assertion_date&dir=desc&pageSize=20&facets=basis_of_record',
                    queryPathForUI: '/occurrences/search?fq=user_assertions:*&q=last_assertion_date:[___DATEPARAM___%20TO%20*]&sort=last_assertion_date&dir=desc',
                    dateFormat: """yyyy-MM-dd'T'HH:mm:ss'Z'""",
                    emailTemplate: '/email/biocache',
                    recordJsonPath: '\$.occurrences[*]',
                    idJsonPath: 'uuid'
            ])).save()
            new PropertyPath([name: "totalRecords", jsonPath: "totalRecords", query: newAssertions, fireWhenNotZero: true]).save()
            new PropertyPath([name: "last_assertion_record", jsonPath: "occurrences[0].rowKey", query: newAssertions]).save()
        }

        log.info("end of preloadQueries")
    }

    def destroy = {}
}
