/*
 */
package org.tweb.storage.properties;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;

/**
 *
 * @author jonas
 */
public class PropertiesWriter {

    @Inject
    PropertyCoder coder;

    private List<String> content = new ArrayList<>();
    private String lastKey = null;
    private String lastKeyWritten = "";
    private Map<String, String> newProps;

    void mergeContent(Stream<String> lines, Map<String, String> theProps) {
        newProps = new LinkedHashMap<>(theProps);
        lines.forEachOrdered(this::parseLine);
        newProps.forEach(this::addProps);
    }

    void writeContent(BufferedWriter writer) throws IOException {
        for (String line : content) {
            writer.append(line);
        }
    }

    private void parseLine(String line) {
        if (PropertiesCommon.commentEmptyLineRegexp.matcher(line).find()) {
            addLine(line);
            return;
        }

        Matcher matcher = PropertiesCommon.propMatchRegexp.matcher(line);

        // New property, single or multiline
        if (matcher.find()) {
            String value;

            lastKey = matcher.group(1).trim();

            if (newProps.containsKey(lastKey)) {
                value = encode(newProps.get(lastKey));
                newProps.remove(lastKey);
                lastKeyWritten = lastKey;
            } else {
                value = matcher.group(2);
            }
            addPropsEncoded(lastKey, value);
        } else {
            // Continue of multiline property value
            if (!lastKey.contentEquals(lastKeyWritten)) {
                // Only add if not the changed value
                addLine(line);
            }
        }

    }

    private void addProps(String key, String value) {
        addPropsEncoded(key, encode(value));
    }

    private void addPropsEncoded(String key, String value) {
        addLine(String.format("%s=%s", key, value));
    }

    private String encode(String val) {
        return Arrays.stream(val.split("\n"))
                .map(coder::encode)
                .collect(Collectors.joining("\\\n   "));
    }

    private void addLine(String line) {
        if (line.endsWith("\n")) {
            content.add(line);
        } else {
            content.add(line + "\n");
        }
    }

}
