/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tweb.search.control;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.tweb.search.SearchData;
import org.tweb.search.SearchMatch;
import org.tweb.search.entity.SearchResult;

/**
 *
 * @author jonas
 */
class SearchRepoLucene implements SearchRepo {
    
    @Inject
    Logger logger;
    
    private static final String FIELD_CONTENT = "content";
    private static final String FIELD_LANG = "lang";
    private static final String FIELD_NAME = "name";

    private StandardAnalyzer analyzer;
    private RAMDirectory ramDirectory;

    @PostConstruct
    public void init() {
        logger.info("Constructing the search repo lucene");
        analyzer = new StandardAnalyzer();
        setupDirectory();
    }

    void setupDirectory() {
        ramDirectory = new RAMDirectory();
    }

    @Override
    public SearchResult searchContent(String str) throws SearchException, IOException {
        SearchResult result = new SearchResult(str);

        try (final DirectoryReader ireader = DirectoryReader.open(ramDirectory)) {
            IndexSearcher isearcher = new IndexSearcher(ireader);
            QueryParser parser = new QueryParser(FIELD_CONTENT, analyzer);
            parser.setAllowLeadingWildcard(true);
            Query query = parser.parse("*" + str + "*");
            TopDocs topDocs = isearcher.search(query, 20);
            for (int i = 0; i < Math.min(20, topDocs.totalHits); i++) {
                Document doc = isearcher.doc(topDocs.scoreDocs[i].doc);
                result.results.addAll(extractSearchMatches(str, doc));
            }
        }
        catch(ParseException e) {
            throw new SearchException(e);
        }

        return result;
    }

    @Override
    public void writeDocument(SearchData data) throws IOException {
        retrySome(this::write, data, 3);
    }

    void retrySome(Consumer<SearchData> fun, SearchData data, int maxAttempts) {
        int attempts = 0;

        while (attempts++ < maxAttempts) {
            try {
                fun.accept(data);
                break;
            }
            catch (UncheckedIOException ex) {
                try {
                    Thread.sleep(attempts * 100);
                } catch (InterruptedException e) {
                    logger.info("Sleep interrupt, lets try again");
                }
            }
        }
    }


    private void write(SearchData data) throws UncheckedIOException {
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);

        try (final IndexWriter iwriter = new IndexWriter(ramDirectory, indexWriterConfig)) {
            Document doc = new Document();
            doc.add(new Field(FIELD_NAME, data.getDocument(), StringField.TYPE_STORED));
            doc.add(new Field(FIELD_LANG, data.getLang(), StringField.TYPE_STORED));
            doc.add(new Field(FIELD_CONTENT, data.getContent().toString(), TextField.TYPE_STORED));
            iwriter.addDocument(doc);
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }

    @Override
    public void clear() {
        ramDirectory.close();
        setupDirectory();
    }

    @Override
    public void clearDocuments(SearchData data) throws IOException {
        retrySome(this::clear, data, 3);
    }

    private void clear(SearchData data)  {
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);

        try (final IndexWriter iwriter = new IndexWriter(ramDirectory, indexWriterConfig)) {
            iwriter.deleteDocuments(new Term(FIELD_NAME, data.getDocument()));
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }

    private List<SearchMatch> extractSearchMatches(String str, Document doc) {
        SearchData searchData = new SearchData(doc.get(FIELD_NAME), doc.get(FIELD_LANG), doc.get(FIELD_CONTENT));
        return searchData.collectMatches(str);
    }
    
}
