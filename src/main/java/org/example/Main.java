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
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        try {

            /*
            // Creazione dell'indice
            String directoryPath = "C:/Users/rikyj/Documents/university/Magistrale/Ingegneria dei dati/urls_htmls_tables/urls_htmls_tables/all_tables";

            List<TableData> tableDataList = new JsonToTableData().
                                                createTableDataMap(directoryPath); // parsing da JSON a TableData
            List<Document> documentsList = new CreateDocuments().
                                                parseJsonToDocuments(tableDataList); // parsing da TableData a Lucene doc

            TableIndexer tableIndexer = new TableIndexer();
            tableIndexer.indexTables(documentsList);
            System.out.println("Indicizzazione completata con successo!");
            */

            // Inizializzazione variabili d'uso
            String indexPath = "C:/Users/hp/DataDiscoveryProject_3HW/src/index";
            float queryCounter = 0; // Counter per il numero di query eseguite
            List<Integer> relevanceRankings = new ArrayList<>(); // Array[i] = ranking del doc più rilevante per la query i+1-esima

            // Avvio della ricerca
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
            IndexSearcher indexSearcher = new IndexSearcher(reader);
            TableVisualizer visualizer = new TableVisualizer(); // Visualizer per il campo 'table'
            EvaluationMetrics evalMetrics = new EvaluationMetrics();

            // Analyzer per la query
            Analyzer stdAnalyzer = new StandardAnalyzer(new Stopwords().getStopWords());

            // Definizione dei campi di ricerca
            String[] fields = {"caption", "table", "footnotes", "references"};
            Map<String, Float> boosts = new HashMap<>();
            boosts.put("caption", 2.0f); // Dai maggiore peso a 'caption'
            boosts.put("table", 1.5f);
            boosts.put("footnotes", 1.2f);
            boosts.put("references", 0.5f);

            MultiFieldQueryParser multiFieldParser = new MultiFieldQueryParser(fields, stdAnalyzer, boosts);

            Scanner scanner = new Scanner(System.in);

            // Avvio del loop di ricerca
            while(true){
                queryCounter++;

                System.out.print("Inserisci la tua query: ");
                String query = scanner.nextLine();

                // Fase di esecuzione della query
                Query multiFieldQuery = multiFieldParser.parse(query);
                TopDocs topDocs = indexSearcher.search(multiFieldQuery, 10);
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

            scanner.close();
            reader.close();
        } catch (IOException e) {
            System.err.println("Errore durante l'indicizzazione: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}