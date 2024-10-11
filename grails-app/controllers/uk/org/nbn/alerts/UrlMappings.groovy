package uk.org.nbn.alerts

class UrlMappings {


    static mappings = {
        "/wsopen/alerts/user/$userId"(controller: 'webservice', action: [GET: 'getUserAlertsOutsideCAS'])
        "/api/savedSearch/create"(controller: 'savedSearch', action: [POST: 'create'])
        "/savedSearch/list"(controller: 'savedSearch', action: [GET: 'list'])

    }
}
