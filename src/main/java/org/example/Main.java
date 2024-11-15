package org.example;

import com.example.datadiscovery.utils.JsonTableParser;
import com.example.datadiscovery.utils.TableData;
import com.example.datadiscovery.utils.TableVisualizer;
import java.io.IOException;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        String filePath = "C:/Users/hp/papers/urls_htmls_tables/test_tables/2407.02549.json";

        TableVisualizer visualizer = new TableVisualizer();
        JsonTableParser parser = new JsonTableParser();
        Map<String, TableData> tablesMap = parser.parseJsonFile(filePath);

        // Controllo che la mappa non sia nulla o vuota
        if (tablesMap != null && !tablesMap.isEmpty()) {
            for (Map.Entry<String, TableData> entry : tablesMap.entrySet()) {

                // Stampa la chiave
                System.out.println("Tabella ID: " + entry.getKey());

                // Stampa il valore (informazioni della tabella)
                TableData table = entry.getValue();  // table rappresenta tutti e 4 i campi
                table.printTableData(table, visualizer);
            }
        } else {
            System.out.println("Nessuna tabella trovata nel JSON.");
        }

        /*
        JsonTableParser parser = new JsonTableParser();
        TableIndexer indexer = new TableIndexer();

        try {
            List<TableData> tables = parser.parseJsonFile(filePath);
            indexer.indexTables(tables);
            System.out.println("Indicizzazione completata con successo!");
        } catch (IOException e) {
            System.err.println("Errore durante l'indicizzazione: " + e.getMessage());
        }
        */
    }
}