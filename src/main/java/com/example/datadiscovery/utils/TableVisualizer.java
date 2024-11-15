package com.example.datadiscovery.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;

public class TableVisualizer {

    public void visualizeTable(String tableContent) {
        try {

            // Parsing HTML con Jsoup
            Document doc = Jsoup.parse(tableContent);
            Elements tables = doc.select("table");

            for (Element table : tables) {
                System.out.println("\nTabella trovata:");

                // Preparazione per calcolare la larghezza delle colonne
                List<List<String>> tableData = new ArrayList<>();
                int maxColumns = 0;

                // Estrazione delle righe
                Elements rows = table.select("tr");
                for (Element row : rows) {
                    List<String> rowData = new ArrayList<>();
                    Elements cells = row.select("th, td");

                    for (Element cell : cells) {
                        // Pulisce il testo della cella rimuovendo simboli e stringhe specifiche
                        String cleanedText = cell.text()
                                .replaceAll("[\\\\]\\w+", "")          // Rimuove simboli in piu
                                .replaceAll("\\\\mathbf\\{.*?\\}", "")  // Rimuove \mathbf{}
                                .replaceAll("±.*?plus-or-minus", "")    // Rimuove ± e "plus-or-minus"
                                .replaceAll("\\{.*?\\}", "")            // Rimuove contenuti tra parentesi graffe {}
                                .replaceAll("%percent\\d+\\d+\\\\%", "%") // Rimuove "%percentXXX%" e lo converte in "%"
                                .replaceAll("percent", "%")            // Sostituisce "percent" con "%"
                                .replaceAll("\\s{2,}", " ");           // Rimuove spazi multipli
                        rowData.add(cleanedText.trim());
                    }
                    tableData.add(rowData);
                    maxColumns = Math.max(maxColumns, rowData.size()); // Aggiorna il numero massimo di colonne
                }

                // Stampa della tabella formattata
                for (List<String> rowData : tableData) {
                    for (int i = 0; i < maxColumns; i++) {

                        // Gestisce celle mancanti
                        String cellText = (i < rowData.size()) ? rowData.get(i) : "";
                        System.out.printf("%-20s", cellText); // 20 caratteri di larghezza per cella
                    }
                    System.out.println();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

