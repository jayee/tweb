package org.tweb.storage.properties.stats;

import org.tweb.search.SearchData;
import org.tweb.summary.boundary.LanguageSummary;

import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchableProperties extends Properties {
    private final SearchData indexer;

    /**
     * Use instead of Properties to collect statistics and build search index
     * @param defaultFilename The path id of the default language file (excluding root path)
     * @param lang The language
     */
    public SearchableProperties(String defaultFilename, String lang) {
        indexer = new SearchData(defaultFilename, lang);
    }

    public SearchData searchIndex() {
        return indexer;
    }

    @Override
    public synchronized Object put(Object key, Object value) {
        String strKey = (String) key;
        String strValue = (String) value;
        
        indexer.addKeyValue(strKey, strValue);

        return super.put(key, value);
    }
}
