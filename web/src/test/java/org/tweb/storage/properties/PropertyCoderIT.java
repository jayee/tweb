/*
 */
package org.tweb.storage.properties;

import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.tweb.application.util.LoggerExposer;

/**
 *
 * @author jonas
 */
@RunWith(Arquillian.class)
public class PropertyCoderIT {
    
    @Inject
    PropertyCoder coder;
    
    @Deployment
    public static WebArchive create() {
        return ShrinkWrap.create(WebArchive.class).
                addClasses(PropertyCoder.class, LoggerExposer.class).
                addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @Test
    public void testDecode() {
        String result = coder.decode("\\=v\\u00e4rde1");
        assertEquals("=värde1", result);
    }

    @Test
    public void testEncode() {
        String result = coder.encode("=värde1");
        assertEquals("\\=v\\u00e4rde1", result);
    }
    
}
