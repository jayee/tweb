/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tweb.summary.control;

import com.typesafe.config.Config;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.tweb.summary.boundary.LanguageSummary;

/**
 *
 * @author jonas
 */
class StatisticsInMemRepo implements StatisticsRepo {

    @Inject
    Logger logger;

    @Inject
    Config config;

    // Filename -> Language Map
    Map<String, LanguageMap> filenameLangMap;
    
    // Language -> Filename Map
    Map<String, FilenameMap> langFilenameMap;

    static class LanguageMap extends Hashtable<String, LanguageSummary> {
        final String filename;

        private LanguageMap(String filename) {
            super();
            this.filename = filename;
        }
    }

    static class FilenameMap extends Hashtable<String, LanguageSummary>{
        final String lang;
        
        private FilenameMap(String lang) {
            super();
            this.lang = lang;
        }
    }
    
    @PostConstruct
    public void init() {
        logger.info("Constructing the statistics in memory repo");
        filenameLangMap = new Hashtable<>();
        langFilenameMap = new Hashtable<>();
    }

    @Override
    synchronized public void saveLanguageSummary(LanguageSummary summary) {
        LanguageMap langMap = filenameLangMap.get(summary.filename);
        if (langMap == null) {
            langMap = new LanguageMap(summary.filename);
            filenameLangMap.put(summary.filename, langMap);
        }
        langMap.put(summary.lang, summary);
        
        FilenameMap fileMap = langFilenameMap.get(summary.lang);
        if (fileMap == null) {
            fileMap = new FilenameMap(summary.lang);
            langFilenameMap.put(summary.lang, fileMap);
        }
        fileMap.put(summary.filename, summary);
    }

    @Override
    public LanguageSummary readLanguageSummary(String filename, String lang) {
        LanguageMap langMap = filenameLangMap.get(filename);
        if (langMap == null) {
            return null;
        }
        
        return langMap.get(lang);
    }

    @Override
    public Collection<LanguageSummary> summarizeLanguage(String lang) {
        FilenameMap filenameMap = langFilenameMap.get(lang);
        if (filenameMap == null) {
            throw new LanguageNotFoundException();
        }
        return filenameMap.values();
    }        



}
