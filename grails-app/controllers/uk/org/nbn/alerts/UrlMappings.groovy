package uk.org.nbn.alerts

class UrlMappings {


    static mappings = {
        "/wsopen/alerts/user/$userId"(controller: 'webservice', action: [GET: 'getUserAlertsOutsideCAS'])


        group "/api/savedSearch", {
            "/list/$userId"(controller: 'savedSearch', action: 'listAsync', method: 'GET')
            "/save"(controller: 'savedSearch', action: 'saveAsync', method: 'POST')
            // other saved search endpoints...
        }

    }
}
