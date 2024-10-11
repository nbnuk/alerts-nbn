package uk.org.nbn.alerts

class SavedSearch {
    String userId //ALA CAS ID
    String description
    String searchRequestQueryUI


    static constraints = {
        description nullable: true, sqlType: 'text'
        searchRequestQueryUI sqlType: 'text'
    }

    static mapping = {
        table 'nbn_saved_search'
    }
}
