package com.example.datadiscovery;

import com.example.datadiscovery.utils.TableData;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.util.List;

public class TableIndexer {
    private Directory indexDirectory;
    private Analyzer analyzer;

    public TableIndexer() {
        this.indexDirectory = new RAMDirectory();
        this.analyzer = new StandardAnalyzer();
    }

    public void indexTables(List<TableData> tables) throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        try (IndexWriter writer = new IndexWriter(indexDirectory, config)) {
            for (TableData table : tables) {
                Document doc = new Document();
                doc.add(new TextField("table", table.getTable(), TextField.Store.YES));
                doc.add(new TextField("caption", table.getCaption(), TextField.Store.YES));

                for (String footnote : table.getFootnotes()) {
                    doc.add(new TextField("footnote", String.join(" ", footnote), TextField.Store.NO));
                }

                for (String reference : table.getReferences()) {
                    doc.add(new TextField("rows", String.join(" ", reference), TextField.Store.NO));
                }

                writer.addDocument(doc);
            }
        }
    }

    public Directory getIndexDirectory() {
        return indexDirectory;
    }
}