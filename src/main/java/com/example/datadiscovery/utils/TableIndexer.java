package com.example.datadiscovery.utils;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableIndexer {

    public void indexTables(List<Document> documents) throws IOException {

        try {

            // Definiamo dove salvare l' indice Lucene
            Directory directory = FSDirectory.open(Paths.get("C:/Users/hp/DataDiscoveryProject_3HW/src/index"));

            Map<String, Analyzer> perFieldAnalyzers = new HashMap<>();
            StandardAnalyzer stdAnalyzer = new StandardAnalyzer(new Stopwords().getStopWords());

            perFieldAnalyzers.put("caption",  stdAnalyzer);
            perFieldAnalyzers.put("table", stdAnalyzer);
            perFieldAnalyzers.put("footnotes",  stdAnalyzer);
            perFieldAnalyzers.put("references", stdAnalyzer);

            Analyzer perFieldAnalyzer = new PerFieldAnalyzerWrapper(new EnglishAnalyzer(), perFieldAnalyzers);

            // Definiamo la configurazione dell' IndexWriter
            IndexWriterConfig config = new IndexWriterConfig(perFieldAnalyzer);
            config.setCodec(new SimpleTextCodec());
            IndexWriter writer = new IndexWriter(directory, config);

            for (Document document : documents) {
                writer.addDocument(document); // Indicizzazione dei documenti
            }

            writer.commit();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}