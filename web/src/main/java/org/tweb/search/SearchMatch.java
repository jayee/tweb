/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tweb.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 *
 * @author jonas
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SearchMatch {

    public String filename;
    public String lang;
    public String key;
    public String value;
    public String before;
    public String match;
    public String after;
    public boolean keyMatch;

    SearchMatch(String str) {
        this.match = str;
    }
}
