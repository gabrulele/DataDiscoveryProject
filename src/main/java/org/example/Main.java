package org.example;

import com.example.datadiscovery.TableIndexer;
import com.example.datadiscovery.TableSearcher;
import com.example.datadiscovery.metrics.EvaluationMetrics;
import com.example.datadiscovery.utils.CreateDocuments;
import com.example.datadiscovery.utils.JsonToTableData;
import com.example.datadiscovery.utils.TableData;
import com.example.datadiscovery.utils.TableVisualizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        try {

            float queryCounter = 0; // Counter per il numero di query eseguite
            List<Integer> relevanceRankings = new ArrayList<>(); // Array[i] = ranking del doc più rilevante per la query i+1-esima

            /*
            // Creazione dell'indice
            String directoryPath = "C:/Users/hp/papers/urls_htmls_tables/urls_htmls_tables/all_tables";

            List<TableData> tableDataList = new JsonToTableData().
                                                createTableDataMap(directoryPath); // parsing da JSON a TableData
            List<Document> documentsList = new CreateDocuments().
                                                parseJsonToDocuments(tableDataList); // parsing da TableData a Lucene doc

            TableIndexer tableIndexer = new TableIndexer();
            tableIndexer.indexTables(documentsList);
            System.out.println("Indicizzazione completata con successo!");
            */

            // Avvio della ricerca nell'indice, con loop per ricerche ripetute
            TableSearcher tableSearcher = new TableSearcher();
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("C:/Users/hp/DataDiscoveryProject/src/index")));
            IndexSearcher indexSearcher = new IndexSearcher(reader);
            TableVisualizer visualizer = new TableVisualizer(); // Visualizer per la visualizzazione del campo table
            EvaluationMetrics evalMetrics = new EvaluationMetrics();

            while(true){
                queryCounter++;
                TopDocs topDocs = tableSearcher.search("C:/Users/hp/DataDiscoveryProject/src/index");

                // Stampa il numero totale di risultati
                System.out.println("Numero totale di risultati trovati: " + topDocs.totalHits.value + "\n");
                System.out.println();
                for (int i = 0; i < topDocs.scoreDocs.length; i++) {
                    ScoreDoc scoreDoc = topDocs.scoreDocs[i]; // Ottieni il corrispondente ScoreDoc

                    Document doc = indexSearcher.doc(scoreDoc.doc);

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
                Scanner scanner = new Scanner(System.in);
                System.out.print("Digita la posizione del documento più rilevante [1-10]: ");
                String userRank = scanner.nextLine();
                relevanceRankings.add(Integer.valueOf(userRank));

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
        } catch (IOException e) {
            System.err.println("Errore durante l'indicizzazione: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}