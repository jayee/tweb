/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tweb.search;

import java.util.List;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jonas
 */
public class SearchDataTest {

    private String content;

    public SearchDataTest() {
    }

    @Before
    public void setUp() {
        content = "||$key1||@some text here||$key2||@more text\n"
                + "much more||$key3||@not many||$key4||@||$key5||@";
    }

    @Test
    public void testSplit() {
        String s = "abcd";

        String[] split = s.split("a", 2);
        Assert.assertEquals(2, split.length);
        Assert.assertEquals("", split[0]);
        Assert.assertEquals("bcd", split[1]);

        split = s.split("d", 2);
        Assert.assertEquals(2, split.length);
        Assert.assertEquals("abc", split[0]);
        Assert.assertEquals("", split[1]);

        split = s.split("bc", 2);
        Assert.assertEquals(2, split.length);
        Assert.assertEquals("a", split[0]);
        Assert.assertEquals("d", split[1]);

    }

    /**
     * Test of AddKey method, of class SearchData.
     */
    @Test
    public void testAddKeyValue() {
        SearchData cut = new SearchData("doc", "sv");
        cut.addKeyValue("key", "value");
        Assert.assertEquals("||$key||@value", cut.getContent().toString());

        cut = new SearchData("doc", "sv", "||$key||@value");
        cut.addKeyValue("key2", "");
        cut.addKeyValue("key3", "value3");
        Assert.assertEquals("||$key||@value||$key2||@||$key3||@value3", cut.getContent().toString());
    }

    /**
     * Test of collectMatches method, of class SearchData.
     */
    @Test
    public void testCollectMatchesOneKey() {
        SearchData cut = new SearchData("doc", "sv", content);
        List<SearchMatch> matches = cut.collectMatches("key1");
        Assert.assertEquals(1, matches.size());
        SearchMatch match = matches.get(0);
        Assert.assertEquals("key1", match.key);
        Assert.assertEquals("some text here", match.value);
        Assert.assertTrue(match.keyMatch);
    }

    @Test
    public void testCollectMatchesManyKeys() {
        SearchData cut = new SearchData("doc", "sv", content);
        List<SearchMatch> matches = cut.collectMatches("key");
        Assert.assertEquals(5, matches.size());
        SearchMatch match = matches.get(4);
        Assert.assertEquals("key5", match.key);
        Assert.assertEquals("", match.value);
        Assert.assertTrue(match.keyMatch);
    }

    @Test
    public void testCollectMatchesTwoValues() {
        SearchData cut = new SearchData("doc", "sv", content);
        List<SearchMatch> matches = cut.collectMatches("text");
        Assert.assertEquals(2, matches.size());
        
        SearchMatch firstMatch = matches.get(0);
        Assert.assertEquals("key1", firstMatch.key);
        Assert.assertEquals("some text here", firstMatch.value);
        Assert.assertEquals("some ", firstMatch.before);
        Assert.assertEquals(" here", firstMatch.after);
        Assert.assertEquals("text", firstMatch.match);
        
        SearchMatch secondMatch = matches.get(1);
        Assert.assertEquals("key2", secondMatch.key);
        Assert.assertEquals("more text\nmuch more", secondMatch.value);
        Assert.assertEquals("more ", secondMatch.before);
        Assert.assertEquals("\nmuch more", secondMatch.after);
        Assert.assertEquals("text", secondMatch.match);
    }
    
    @Test
    public void testCollectMatchesEdge1() {
        SearchData cut = new SearchData("doc", "sv", content);
        List<SearchMatch> matches = cut.collectMatches("not");
        Assert.assertEquals(1, matches.size());
        
        SearchMatch match = matches.get(0);
        Assert.assertEquals("key3", match.key);
        Assert.assertEquals("not many", match.value);
        Assert.assertEquals("", match.before);
        Assert.assertEquals(" many", match.after);
        Assert.assertEquals("not", match.match);
    }

    @Test
    public void testCollectMatchesEdge2() {
        SearchData cut = new SearchData("doc", "sv", content);
        List<SearchMatch> matches = cut.collectMatches("many");
        Assert.assertEquals(1, matches.size());
        
        SearchMatch match = matches.get(0);
        Assert.assertEquals("key3", match.key);
        Assert.assertEquals("not many", match.value);
        Assert.assertEquals("not ", match.before);
        Assert.assertEquals("", match.after);
        Assert.assertEquals("many", match.match);
    }

}
