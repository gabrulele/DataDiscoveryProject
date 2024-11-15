package com.example.datadiscovery;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TableSearcher {

    private final IndexSearcher searcher;
    private final StandardAnalyzer analyzer;

    public TableSearcher(String indexPath) throws IOException {
        Directory directory = FSDirectory.open(Paths.get(indexPath));
        IndexReader reader = DirectoryReader.open(directory);
        this.searcher = new IndexSearcher(reader);
        this.analyzer = new StandardAnalyzer();
    }

    public List<Document> search(String captionQueryStr, String tableDataQueryStr) throws Exception {
        QueryParser captionParser = new QueryParser("caption", analyzer);
        QueryParser tableDataParser = new QueryParser("tableData", analyzer);

        Query captionQuery = captionParser.parse(captionQueryStr);
        Query tableDataQuery = tableDataParser.parse(tableDataQueryStr);

        BooleanQuery combinedQuery = new BooleanQuery.Builder()
                .add(captionQuery, BooleanClause.Occur.SHOULD)
                .add(tableDataQuery, BooleanClause.Occur.SHOULD)
                .build();

        TopDocs results = searcher.search(combinedQuery, 10);
        List<Document> documents = new ArrayList<>();
        for (ScoreDoc scoreDoc : results.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            documents.add(doc);
        }
        return documents;
    }
}
