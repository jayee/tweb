/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tweb.search.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.tweb.search.SearchMatch;

/**
 *
 * @author jonas
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SearchResult {
    public List<SearchMatch> results;
    public final String searchString;

    public SearchResult(String searchStr) {
        searchString = searchStr;
        results = new ArrayList<SearchMatch>();
    }
    
}
