/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tweb.search;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jonas
 */
public class SearchData {

    private StringBuilder content = new StringBuilder();
    private String document;
    private String lang;
    
    public SearchData(String docName, String lang) {
        this.document = docName;
        this.lang = lang;
    }

    public SearchData(String docName, String lang, String content) {
        this.document = docName;
        this.lang = lang;
        this.content.append(content);
    }

    
    public void addKeyValue(String key, String value) {
        String emptyOrValue = value == null ? "" : value;
        this.content.append("||$").append(key).append("||@").append(emptyOrValue);
    }

    public StringBuilder getContent() {
        return content;
    }

    public String getDocument() {
        return document;
    }

    public String getLang() {
        return lang;
    }
    
    public List<SearchMatch> collectMatches(String str) {
        List<SearchMatch> matches = new ArrayList<>();

        String[] properties = content.toString().split("\\|\\|\\$");
        for (String prop : properties) {
            if (prop.length() == 0) {
                // Skip zero length matches (like first or last can be)
                continue;
            }
            
            SearchMatch match = new SearchMatch(str);
            boolean found = false;
            
            String[] pair = prop.split("\\|\\|@", 2);
            match.key = pair[0];
            match.value = pair[1];
            
            // We always have a key, and value (may be empty)
            // TODO: These cases can be refactored
            if (match.key.contains(str)) {
                match.keyMatch = true;
                String[] parts = match.key.split(str,2);
                match.before = parts[0];
                match.after = parts[1];
                found = true;
            } else if (pair.length == 2 && match.value.contains(str)) {
                String[] parts = pair[1].split(str,2);
                match.before = parts[0];
                match.after = parts[1];
                found = true;
            } else if (pair.length != 2) {
                throw new RuntimeException("Logic error; Invalid SearchData: " + prop);
            }

            if (found) {
                match.filename = document;
                match.lang = lang;
                matches.add(match);
            }
        }
        
        return matches;
    }

    
}
