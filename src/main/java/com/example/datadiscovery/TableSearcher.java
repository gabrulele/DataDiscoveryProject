package com.example.datadiscovery;

import com.example.datadiscovery.utils.Stopwords;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;
import java.util.*;

public class TableSearcher {

    public TopDocs search(String indexPath) throws Exception {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci la tua query: ");
        String query = scanner.nextLine();

        // Apri l'indice
        TopDocs topDocs;
        try (IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)))) {
            IndexSearcher searcher = new IndexSearcher(reader);

            // Analyzer per analizzare la query
            Analyzer stdAnalyzer = new StandardAnalyzer(new Stopwords().getStopWords());

            // Definisci i campi da cercare
            String[] fields = {"caption", "table", "footnotes", "references"};
            Map<String, Float> boosts = new HashMap<>();
            boosts.put("caption", 2.0f); // Dai maggiore peso a 'caption'
            boosts.put("table", 1.5f);
            boosts.put("footnotes", 1.2f);
            boosts.put("references", 0.5f);

            MultiFieldQueryParser multiFieldParser = new MultiFieldQueryParser(fields, stdAnalyzer, boosts);
            Query multiFieldQuery = multiFieldParser.parse(query);

            // Esegui la ricerca
            topDocs = searcher.search(multiFieldQuery, 10);
        }
        return topDocs;
    }
}
