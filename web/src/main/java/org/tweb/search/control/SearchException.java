package org.tweb.search.control;

import org.apache.lucene.queryparser.classic.ParseException;

/**
 * Created by jonas on 1/31/17.
 */
public class SearchException extends Exception {

    public SearchException(Exception e) {
        super(e);
    }
}
