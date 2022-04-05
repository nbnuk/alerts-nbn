package uk.org.nbn.alerts

import au.org.ala.alerts.Notification
import au.org.ala.alerts.Query
import au.org.ala.alerts.User
import grails.converters.JSON
import org.apache.http.HttpStatus

class WebserviceController extends au.org.ala.alerts.WebserviceController{

    @Override
    def createBiocacheNewAnnotationsAlert() {
        log.debug("Create biocache new annotations alert for " + params.resourceName ?: "all resources")
        //biocacheWebserviceQueryPath, String biocacheUIQueryPath, String queryDisplayName
        def error = ""
        if (params.webserviceQuery && params.uiQuery && params.queryDisplayName) {
            //region + species group
            Query newQuery = queryService.createBioCacheAnnotationQuery(params.webserviceQuery, params.uiQuery, params.queryDisplayName, params.baseUrlForWS, params.baseUrlForUI, params.resourceName)
            if (params.userId) {
                def user = User.findByUserId(params.userId)
                if (!user) {
                    //response.sendError(400)
                    error = """Error: could not find user to subscribe for record annotations alerts"""
                } else {
                    if (authService.userInRole("ROLE_ADMIN") && (grailsApplication.config.security?.adminManageAlertsForOthers?:"false").toBoolean()) {
                        queryService.createQueryForUserIfNotExists(newQuery, user)
                    } else {
                        //the config needs to explicitly permit this
                        //response.sendError(403)
                        error = """Error: either you are not an administrator or Alerts have not been configured to allow this"""
                    }
                }
            } else {
                queryService.createQueryForUserIfNotExists(newQuery, userService.getUser())
            }
            if (error == "") {
                redirectIfSupplied(params)
            } else {
                render(view: 'index', model: [message: error])
            }
        } else {
            response.sendError(400)
        }
    }


    def getUserAlertsOutsideCAS() {
        if (!params.userId) {
            response.status = HttpStatus.SC_BAD_REQUEST
            response.sendError(HttpStatus.SC_BAD_REQUEST, "userId is a required parameter")
        } else {
            def user = User.findByUserId(params.userId)
            if (!user) {
                response.status = HttpStatus.SC_BAD_REQUEST
                response.sendError(HttpStatus.SC_BAD_REQUEST, "userId not found")
            } else {
                response.status = HttpStatus.SC_OK
                def notificationInstanceList = Notification.findAllByUser(user)
                def enabledQueries = notificationInstanceList.collect { it.query?.name }
                render(enabledQueries as JSON)
            }
        }
    }


}
