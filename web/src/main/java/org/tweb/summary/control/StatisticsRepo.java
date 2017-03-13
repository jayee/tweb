/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tweb.summary.control;

import java.util.Collection;
import java.util.List;
import org.tweb.summary.boundary.LanguageSummary;

/**
 *
 * @author jonas
 */
public interface StatisticsRepo {

    LanguageSummary readLanguageSummary(String filename, String lang);

    void saveLanguageSummary(LanguageSummary summary);

    Collection<LanguageSummary> summarizeLanguage(String lang);
    
}
