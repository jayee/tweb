/*
 */
package org.tweb.storage.properties;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

/**
 *
 * @author jonas
 */
public class PropertyCoder {
    
    @Inject
    Logger logger;
    
    public String decode(String encoded) {
        String result = encoded;
        Properties coderProps = new Properties();
        StringReader reader = new StringReader("key:" + encoded);
        try {
            coderProps.load(reader);
            result = coderProps.getProperty("key");
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Can not decode property: " + encoded, ex);
        }
        
        return result;
    }
    
    public String encode(String decoded) {
        String result = decoded;
        Properties coderProps = new Properties();
        StringWriter writer = new StringWriter();
        // Deal with the properties format via class Properties
        coderProps.setProperty("key", decoded);
        
        try {
            coderProps.store(writer, null);
            StringBuilder b = new StringBuilder();
            String[] headerAndContent = writer.toString().split("\n", 2);
            for (char c : headerAndContent[1].toCharArray()) {
                if (c >= 128) {
                    b.append("\\u").append(String.format("%04x", (int) c));
                } else {
                    b.append(c);
                }
            }
            result = b.toString().substring(4, b.length()-1);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Can not encode property: " + decoded, ex);
        }
        
        return result;
    }
}
