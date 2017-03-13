/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tweb.search.control;

import java.io.IOException;
import org.tweb.search.SearchData;
import org.tweb.search.entity.SearchResult;

/**
 *
 * @author jonas
 */
public interface SearchRepo {

    SearchResult searchContent(String str) throws IOException, SearchException;

    void writeDocument(SearchData data) throws IOException;

    void clear();

    void clearDocuments(SearchData data) throws IOException;
}
