/*
 */
package org.tweb.storage.properties;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.junit.Assert;
import org.tweb.testutil.MockTest;
import org.junit.Test;
import org.junit.Before;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

/**
 *
 * @author jonas
 */
public class PropertiesReaderTest extends MockTest {

    @Mock
    PropertyCoder coder;

    @InjectMocks
    PropertiesReader reader;

    @Before
    public void setup() {
        when(reader.coder.decode(anyString())).then(returnsFirstArg());
    }

    @Test
    public void testAddLinesToTranslation() {
        List lines = new ArrayList<String>();
        lines.add("one.prop = one and two");
        lines.add("three=one\\");
        lines.add("  two\\");
        lines.add("  three");
        lines.add("# comment");
        lines.add("double=single:single ");
        
        LinkedHashMap<String, String> map = new LinkedHashMap<String,String>();
        reader.addLinesToTranslation(lines.stream(), map);
        
        Assert.assertEquals(3, map.size());

        Assert.assertEquals("one and two", map.get("one.prop"));
        Assert.assertEquals("one\ntwo\nthree", map.get("three"));
        Assert.assertEquals("single:single ", map.get("double"));
    }


    @Test
    public void testParseValue() {
        Assert.assertEquals("hello hey ", reader.parseValue(" hello hey "));
        Assert.assertEquals("hello ", reader.parseValue("hello \\"));
    }

}
