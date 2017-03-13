/*
 */
package org.tweb.storage.properties;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.tweb.testutil.MockTest;

/**
 *
 * @author jonas
 */
public class PropertiesWriterTest extends MockTest {
    @Mock
    PropertyCoder coder;

    @InjectMocks
    PropertiesWriter writer;

    @Before
    public void setup() {
        when(writer.coder.encode(anyString())).then(returnsFirstArg());
    }
    

    @Test
    public void testEdgeCase() throws IOException {
        List<String> lines = new ArrayList<String>();

        LinkedHashMap<String, String> props = new LinkedHashMap<>();
        props.put("one.prop", "one\ntwo");

        writer.mergeContent(lines.stream(), props);
        StringWriter sw = new StringWriter();
        BufferedWriter bw = new BufferedWriter(sw);
        writer.writeContent(bw);

        bw.close();
        String result = sw.toString();
        Assert.assertEquals("one.prop=one\\\n   two\n", result);
    }

    @Test
    public void testReadWrite() throws IOException {
        List<String> lines = new ArrayList<String>();
        lines.add("one = one and two");
        lines.add("three=one\\");
        lines.add("  two\\");
        lines.add("  three");
        lines.add("# comment");
        lines.add("double=single:single ");

        writer.mergeContent(lines.stream(), new LinkedHashMap<>());
        StringWriter sw = new StringWriter();
        BufferedWriter bw = new BufferedWriter(sw);
        writer.writeContent(bw);

        bw.close();
        String result = sw.toString();
        Assert.assertTrue(result.contains("one= one and two"));
        Assert.assertTrue(result.contains("three=one\\\n  two\\\n  three\n"));
        Assert.assertTrue(result.contains("# comment"));
        Assert.assertTrue(result.contains("double=single:single \n"));
    }


    @Test
    public void testMergeContentIT() throws IOException {
        // Set real encoder to also test proper encoding
        writer.coder = new PropertyCoder();

        List<String> lines = new ArrayList<String>();
        lines.add("one.prop = one and two");
        lines.add("three=one\\");
        lines.add("  two\\");
        lines.add("  three");
        lines.add("# c\\u00F6mment");
        lines.add("double=single:single ");

        LinkedHashMap<String, String> props = new LinkedHashMap<>();
        props.put("one.prop", "one ä ");
        props.put("new.prop", "a new pröperty");
        props.put("three", "öne\ntwo\nthree \nfour");

        writer.mergeContent(lines.stream(), props);
        StringWriter sw = new StringWriter();
        BufferedWriter bw = new BufferedWriter(sw);
        writer.writeContent(bw);

        bw.close();
        String result = sw.toString();
        Assert.assertTrue(result.contains("one.prop=one \\u00e4 "));
        Assert.assertTrue(result.contains("three=\\u00f6ne\\\n   two\\\n   three \\\n   four"));
        Assert.assertTrue(result.contains("new.prop=a new pr\\u00f6perty"));
        Assert.assertTrue(result.contains("# c\\u00F6mment"));
        Assert.assertTrue(result.contains("double=single:single "));
    }


}
