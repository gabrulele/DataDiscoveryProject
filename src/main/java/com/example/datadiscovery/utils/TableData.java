package com.example.datadiscovery.utils;

import java.util.List;

public class TableData {
    private String caption;
    private String table;
    private List<String> footnotes;
    private List<String> references;

    public TableData(String caption, String table, List<String> footnotes, List<String> references) {
        this.caption = caption;
        this.table = table;
        this.footnotes = footnotes;
        this.references = references;
    }

    public String getCaption() {
        return caption;
    }

    public String getTable() {
        return table;
    }

    public List<String> getFootnotes() {
        return footnotes;
    }

    public List<String> getReferences() {
        return references;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public void setFootnotes(List<String> footnotes) {
        this.footnotes = footnotes;
    }

    public void setReferences(List<String> references) {
        this.references = references;
    }

    /**
     * Metodo per stampare una lista di stringhe nel formato [elem1, elem2 ...]
     * con una lunghezza massima per riga e rientro per le righe successive.
     *
     * @param lista la lista di stringhe
     * @param maxLineLength la lunghezza massima per ciascuna riga
     */
    public static void printFormattedList(List<String> lista, int maxLineLength) {

        // Iniziamo a costruire la stringa con la parentesi quadra
        StringBuilder sb = new StringBuilder("[");

        for (int i = 0; i < lista.size(); i++) {
            String elemento = lista.get(i);

            // Variabile per tenere traccia se siamo alla prima riga o meno
            boolean firstLine = true;

            // Dividiamo l'elemento in righe che non superano maxLineLength
            while (elemento.length() > maxLineLength) {
                if (!firstLine) {
                    sb.append("             ");  // Aggiungi il rientro di 4 spazi per le righe successive
                }
                sb.append(elemento, 0, maxLineLength).append("\n");
                elemento = elemento.substring(maxLineLength);
                firstLine = false;  // Dopo la prima riga, non siamo più alla prima riga
            }

            // Aggiungi l'ultima parte dell'elemento
            if (!firstLine) {
                sb.append("             ");  // Rientra l'ultima parte anche se non c'è newline
            }
            sb.append(elemento);  // Aggiungi la parte rimanente dell'elemento
            if (i < lista.size() - 1) {
                sb.append(" , \n");  // Separatore tra gli elementi
            }
        }

        sb.append("]");  // Chiudi la parentesi quadra

        // Stampa il risultato
        System.out.println(sb.toString());
    }

    public void printTableData(TableData tableData, TableVisualizer visualizer) {

        // Stampa le informazioni della tabella
        // visualizer.visualizeTable(tableData.getTable());  // 'table.getTable()' prende il contenuto della tabella

        System.out.println("\nCaption: " + tableData.getCaption());

        System.out.print("\nFootnotes: ");
        printFormattedList(tableData.getFootnotes(), 50);

        System.out.print("\nReferences: ");
        printFormattedList(tableData.getReferences(), 50);

        System.out.println("<-------------------------------------------------------------------->");
    }
}