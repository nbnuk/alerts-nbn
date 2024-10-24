package uk.org.nbn.alerts

import grails.persistence.Entity
import groovy.transform.ToString

class SavedSearch {
    String userId //ALA CAS ID
    String name
    String description
    String searchRequestQueryUI


    static constraints = {
        name nullable: false, sqlType: 'text'
        description nullable: true, sqlType: 'text'
        searchRequestQueryUI nullable: false, sqlType: 'text'
    }

    static mapping = {
        table 'nbn_saved_search'
    }

    public String toString() { userId + " - " + name + " - " + description + " - " + searchRequestQueryUI }
}
