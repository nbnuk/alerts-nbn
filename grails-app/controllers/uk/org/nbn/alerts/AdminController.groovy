package uk.org.nbn.alerts

import org.apache.http.HttpStatus


class AdminController extends au.org.ala.alerts.AdminController{

    @Override
    def repairNotificationsWithoutUnsubscribeToken() {
        render status: HttpStatus.SC_NOT_FOUND
    }

    @Override
    def repairUsersWithoutUnsubscribeToken() {
        render status: HttpStatus.SC_NOT_FOUND
    }

}
