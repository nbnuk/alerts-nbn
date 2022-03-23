package uk.org.nbn.alerts


class AdminController extends au.org.ala.alerts.AdminController{

    def repairNotificationsWithoutUnsubscribeToken() {
        render status: 404
    }

    def repairUsersWithoutUnsubscribeToken() {
        render status: 404
    }

}
