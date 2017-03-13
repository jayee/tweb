/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tweb.summary.boundary;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author jonas
 */
public class LanguageSummary {

    public final String filename;
    public final String lang;

    public final List<String> parameterMismatches = new ArrayList<>();
    public Set<String> missingKeys;

    public String parseInfo;

    public LanguageSummary(String filename, String lang) {
        this.filename = filename;
        this.lang = lang;
    }

}
