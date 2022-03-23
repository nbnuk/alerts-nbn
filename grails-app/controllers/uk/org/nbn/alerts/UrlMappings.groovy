package uk.org.nbn.alerts

class UrlMappings {


    static mappings = {
        "/wsopen/alerts/user/$userId"(controller: 'webservice', action: [GET: 'getUserAlertsOutsideCAS'])
    }
}
