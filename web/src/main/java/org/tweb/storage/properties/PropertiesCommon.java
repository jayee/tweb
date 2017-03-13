/*
 */
package org.tweb.storage.properties;

import java.util.regex.Pattern;

/**
 *
 * @author jonas
 */
public class PropertiesCommon {

    // Pattern for matching filename
    static Pattern filepattern = Pattern.compile("^(.+?)_(..)(_.+)*\\.properties$");

    static Pattern propMatchRegexp = Pattern.compile("([^=:]*[^\\\\])[=:](.*)");
    static Pattern commentEmptyLineRegexp = Pattern.compile("^\\s*[#!]|^\\s*$");
    static Pattern matchMultiLineRegexp = Pattern.compile("^\\s*(.*?)[\\\\]*$");

}
