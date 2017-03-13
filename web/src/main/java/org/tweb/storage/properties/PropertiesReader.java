/*
 */
package org.tweb.storage.properties;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javax.inject.Inject;

/**
 *
 * @author jonas
 */

public class PropertiesReader {

    @Inject
    PropertyCoder coder;

    String lastKey = null;
    private Map<String, String> props;

    public void addLinesToTranslation(Stream<String> lines, Map<String, String> properties) {
        props = properties;
        lines.forEachOrdered(this::parseLine);
    }

    private void parseLine(String line) {
        if (PropertiesCommon.commentEmptyLineRegexp.matcher(line).find()) {
            return;
        }

        Matcher matcher = PropertiesCommon.propMatchRegexp.matcher(line);

        // New property, single or multiline
        if (matcher.find()) {
            lastKey = matcher.group(1).trim();
            props.put(lastKey, parseValue(matcher.group(2)));
        } else {
            // Continue of multiline property value
            props.put(lastKey, props.get(lastKey) + "\n" + parseValue(line));
        }
    }

    /**
     * Removes the last \ if any (multi line marker) and trims beginning spaces.
     * It also decodes the value.
     * 
     * @param val
     * @return parsed value
     */
    String parseValue(String val) {
        String ret = val;
        Matcher matcher = PropertiesCommon.matchMultiLineRegexp.matcher(val);
        if (matcher.find()) {
            ret = matcher.group(1);
        } else {
            throw new RuntimeException("Logic error in matchMultiLineRegexp expression");
        }

        return coder.decode(ret);
    }
    
}
