/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tweb.search.control;

import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.tweb.application.util.LoggerExposer;
import org.tweb.search.SearchData;
import org.tweb.search.SearchMatch;
import org.tweb.search.entity.SearchResult;

import java.util.Collection;

/**
 *
 * @author jonas
 */
@RunWith(Arquillian.class)
public class SearchRepoLuceneIT {

    @Inject
    private SearchRepo repo;

    private static final String content = "||$key1||@some text here||$key2||@more text\n"
            + "much more||$key3||@not many||$key4||@||$key5||@";

    @Deployment
    public static WebArchive create() {
        return ShrinkWrap.create(WebArchive.class).
                addClasses(SearchRepo.class,
                        SearchRepoLucene.class,
                        LoggerExposer.class).
                addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    /**
     * Test of searchContent method, of class SearchRepo.
     */
    @Test
    public void testSearchContent() throws Exception {
        SearchData data = new SearchData("doc", "lang", content);
        repo.writeDocument(data);
        repo.writeDocument(new SearchData("mini", "sv", "||$nyckel||@det enda värdet i detta dokument, mucho"));
        
        SearchResult res = repo.searchContent("yckel");
        Assert.assertEquals("yckel", res.searchString);
        Assert.assertEquals(1, res.results.size());

        res = repo.searchContent("nyckel");
        Assert.assertEquals(1, res.results.size());

        res = repo.searchContent("re text");
        Assert.assertEquals(1, res.results.size());
        
        SearchMatch match = res.results.get(0);
        Assert.assertEquals("mo", match.before);
        Assert.assertEquals("\nmuch more", match.after);
        Assert.assertEquals("re text", match.match);
        Assert.assertFalse(match.keyMatch);
        Assert.assertEquals("key2", match.key);
        Assert.assertEquals("more text\nmuch more", match.value);
        
        res = repo.searchContent("ey2");
        Assert.assertEquals(1, res.results.size());
        
        match = res.results.get(0);
        Assert.assertTrue(match.keyMatch);
        Assert.assertEquals("ey2", match.match);
        Assert.assertEquals("key2", match.key);
        Assert.assertEquals("more text\nmuch more", match.value);

        res = repo.searchContent("much");
        Assert.assertEquals(2, res.results.size());

        res = repo.searchContent("many");
        Assert.assertEquals(1, res.results.size());
        match = res.results.get(0);
        Assert.assertEquals("doc", match.filename);
        Assert.assertEquals("lang", match.lang);

    }

    /**
     * Test of writeDocument method, of class SearchRepo.
     */
    @Test
    public void testWriteDocument() throws Exception {
        SearchData data = new SearchData("doc", "lang", content);
        repo.writeDocument(data);
        repo.writeDocument(data);
    }

    @Test
    public void testClearDocuments() throws Exception {
        SearchData data = new SearchData("doc", "lang", content);
        repo.writeDocument(data);
        SearchData mini = new SearchData("mini", "sv", "||$nyckel||@det enda värdet i detta dokument, mucho");
        repo.writeDocument(mini);
        repo.writeDocument(new SearchData("mini", "en", "||$nyckel||@the only value in this document"));

        SearchResult res = repo.searchContent("nyckel");
        Assert.assertEquals(2, res.results.size());

        repo.clearDocuments(mini);

        res = repo.searchContent("nyckel");
        Assert.assertEquals(0, res.results.size());
    }
}
