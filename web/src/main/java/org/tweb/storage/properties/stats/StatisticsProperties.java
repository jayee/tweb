package org.tweb.storage.properties.stats;

import org.tweb.summary.boundary.LanguageSummary;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StatisticsProperties extends SearchableProperties {

    private StringBuilder parseInfo = new StringBuilder();
    private final Properties defaultLanguage;
    private final LanguageSummary langSummary;
    private final Set<String> missingKeys;

    /**
     * Use instead of Properties to collect statistics and build search index
     * @param defaults The properties for the default language
     * @param defaultFilename The path id of the default language file (excluding root path)
     * @param lang The language
     */
    public StatisticsProperties(Properties defaults, String defaultFilename, String lang) {
        super(defaultFilename, lang);
        defaultLanguage = defaults;
        missingKeys = defaults.stringPropertyNames();
        langSummary = new LanguageSummary(defaultFilename, lang);
    }

    public LanguageSummary summarize() {
        langSummary.parseInfo = parseInfo.toString();
        langSummary.missingKeys = missingKeys;
        
        return langSummary;
    }

    @Override
    public synchronized Object put(Object key, Object value) {
        String strKey = (String) key;
        String strValue = (String) value;
        
        checkDuplicate(strKey, strValue);
        if (checkIsKeyInUse(strKey)) {
            checkMissingParameters(strKey, strValue);
        }

        missingKeys.remove(strKey);

        return super.put(key, value);
    }

    void checkDuplicate(String key, String value) {
        if (containsKey(key)) {
            if (get(key).equals(value)) {
                parseInfo.append("Duplicate key, same value: " + key + "\n");
                parseInfo.append("-> Value: " + value);
            } else {
                parseInfo.append("Duplicate key, different value: " + key + "\n");
                parseInfo.append("-> Old value: " + get(key));
                parseInfo.append("-> New value: " + value);
            }
        }
    }

    void checkMissingParameters(String key, String value) {
        // Check if different number of parameter placeholder strings {x}
        String defaultValue = (String) defaultLanguage.getProperty(key);

        Pattern pattern = Pattern.compile("\\{\\d\\}");
        Matcher defaultStrMatcher = pattern.matcher(defaultValue);
        Matcher strMatcher = pattern.matcher(value);

        int defaultCount = 0;
        while (defaultStrMatcher.find()) {
            defaultCount++;
        }

        int paramCount = 0;
        while (strMatcher.find()) {
            paramCount++;
        }

        if (defaultCount != paramCount) {
            langSummary.parameterMismatches.add(key);
        }
    }

    boolean checkIsKeyInUse(String key) {
        return defaultLanguage.containsKey(key);
    }

}
