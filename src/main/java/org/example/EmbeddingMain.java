package org.example;

import com.example.datadiscovery.metrics.EvaluationMetrics;
import com.example.datadiscovery.utils.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.StoredFields;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.KnnFloatVectorQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;

public class EmbeddingMain {

     

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);

        while(true){

            // Inizializzazione variabili d'uso
            String indexPath = "C:/Users/rikyj/Documents/university/Magistrale/Ingegneria dei dati/HW3/src/index";
            float queryCounter = 0; // Counter per il numero di query eseguite
            List<Integer> relevanceRankings = new ArrayList<>(); // Array[i] = ranking del doc più rilevante per la query i+1-esima
            // Avvio della ricerca
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
            IndexSearcher indexSearcher = new IndexSearcher(reader);
            TableVisualizer visualizer = new TableVisualizer(); // Visualizer per il campo 'table'
            EvaluationMetrics evalMetrics = new EvaluationMetrics();

            EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
            queryCounter++;

            System.out.print("Inserisci la tua query: ");
            String query = scanner.nextLine();


            // Fase di esecuzione della query
            Query embeddedingQuery = new KnnFloatVectorQuery("embedding", embeddingModel.embed(TextSegment.from(query)).content().vector(), 10);
            TopDocs topDocs = indexSearcher.search(embeddedingQuery, 10);
            StoredFields storedFields = indexSearcher.storedFields();

            // Stampa il numero totale di risultati                
            System.out.println("Numero totale di risultati trovati: " + topDocs.totalHits.value() + "\n\n");
            for (int i = 0; i < topDocs.scoreDocs.length; i++) {
                ScoreDoc scoreDoc = topDocs.scoreDocs[i]; // Ottieni il corrispondente ScoreDoc

                Document doc =  storedFields.document(scoreDoc.doc);

                System.out.printf("Risultato #%d (Score: %.4f)\n", i + 1, scoreDoc.score); // Stampa lo score

                    TableData.printFormattedString(doc.get("caption"), 70);

                    System.out.println("Table: ");
                    // visualizer.visualizeTable(doc.get("table"));

                    System.out.print("Footnotes: ");
                    String[] footnotes = doc.getValues("footnotes");
                    TableData.printFormattedList(List.of(footnotes),70);

                    System.out.print("References: ");
                    String[] references = doc.getValues("references");
                    TableData.printFormattedList(List.of(references),70);

                    System.out.println("\n<------------------------------------------------------------------------>\n");
                }

            // Scelta del doc più rilevante in modalità Feedback Utente
            System.out.print("Digita la posizione del documento più rilevante [1-10]: ");
            String userRank = scanner.nextLine();
            relevanceRankings.add(Integer.valueOf(userRank));

            // Valuta la fine della ricerca
            System.out.print("Vuoi continuare? Y/n ");
            String exit = scanner.nextLine();
            if (exit.equalsIgnoreCase("n")) {

                // Stampa le metriche complessive
                System.out.println("\n<Metriche complessive>");

                // System.out.println("NDCG: " + evalMetrics.calculateNDCG(evalMetrics.assignRelevances(topDocs), topDocs));
                System.out.println("MRR: " + evalMetrics.calculateMRR(relevanceRankings,queryCounter));

                System.out.println("Uscita dal programma.");
                break;
                }
            }


    }
}
