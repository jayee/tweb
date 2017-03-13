/*
 */
package org.tweb.search.boundary;

import org.tweb.search.control.SearchException;
import org.tweb.search.control.SearchRepoFactory;
import org.tweb.search.entity.SearchResult;

import javax.inject.Inject;
import java.io.IOException;


/**
 * @author jonas
 */
class Search {

    @Inject
    SearchRepoFactory searchRepoFactory;

    public SearchResult search(String str) throws IOException, SearchException {
        return searchRepoFactory.getRepo().searchContent(str);
    }
}
