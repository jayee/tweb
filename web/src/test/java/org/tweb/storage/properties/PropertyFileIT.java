/*
 */
package org.tweb.storage.properties;

import com.typesafe.config.ConfigFactory;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.tweb.application.util.ConfigExposer;
import org.tweb.application.util.LoggerExposer;
import org.tweb.translation.entity.Translation;

/**
 *
 * @author jonas
 */
@RunWith(Arquillian.class)
public class PropertyFileIT {
    
    @Inject
    PropertyFile cut;
    
    @Before
    public void setup() {
        System.setProperty("root_path", "src/test/props");
        ConfigFactory.invalidateCaches();
        cut.config = ConfigFactory.defaultReference();
    }
    
    @Deployment
    public static WebArchive create() {
        return ShrinkWrap.create(WebArchive.class).
                addClasses(PropertyFile.class,
                        ConfigExposer.class,
                        PathUtil.class,
                        PropertiesReader.class,
                        PropertiesWriter.class,
                        PropertyCoder.class,
                        LoggerExposer.class).
                addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    @Test
    public void testLoad() {
        cut.init("folder/prop.properties", null);
        Translation trans = cut.load();
        Assert.assertTrue(trans.getProps().containsKey("prop1"));
        Assert.assertEquals("value1", trans.getProps().get("prop1"));
        Assert.assertTrue(trans.getProps().containsKey("prop2"));
        Assert.assertEquals("value2", trans.getProps().get("prop2"));
        Assert.assertTrue(trans.getProps().containsKey("prop3"));
        Assert.assertTrue(trans.getProps().get("prop3").contains("multi"));
        Assert.assertTrue(trans.getProps().containsKey("prop4"));
        Assert.assertEquals("value4", trans.getProps().get("prop4"));
        String prop3 = trans.getProps().get("prop3");
    }

    @Test
    public void testLoadSv() {
        cut.init("folder/prop.properties", "sv");
        Translation trans = cut.load();
        Assert.assertTrue(trans.getProps().containsKey("prop1"));
        Assert.assertEquals("värde1", trans.getProps().get("prop1"));
        Assert.assertTrue(trans.getProps().containsKey("prop2"));
        Assert.assertTrue(trans.getProps().containsKey("prop3"));
        Assert.assertTrue(trans.getProps().containsKey("prop4"));
        String prop3 = trans.getProps().get("prop3");
    }

}
