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

    /*
     * Metodo per stampare una lista di stringhe nel formato [elem1, elem2 ...]
     * con una lunghezza massima per riga e rientro per le righe successive.
     */
    public static void printFormattedList(List<String> lista, int maxLineLength) {

        // Iniziamo a costruire la stringa con la parentesi quadra
        StringBuilder sb = new StringBuilder("[");

        for (int i = 0; i < lista.size(); i++) {
            String elemento = lista.get(i).replace("\n", " "); // Sostituisci \n con uno spazio

            boolean firstLine = true; // Per gestire il rientro iniziale

            while (elemento.length() > maxLineLength) {
                if (!firstLine) {
                    sb.append("              "); // Rientro per righe successive
                }
                sb.append(elemento, 0, maxLineLength).append("\n");
                elemento = elemento.substring(maxLineLength);
                firstLine = false; // Dopo la prima riga, non siamo più alla prima riga
            }

            // Aggiungi l'ultima parte dell'elemento
            if (!firstLine) {
                sb.append("              "); // Rientro per l'ultima parte
            }
            sb.append(elemento); // Aggiungi la parte rimanente dell'elemento

            // Aggiungi il separatore tra gli elementi
            if (i < lista.size() - 1) {
                sb.append("    <,>   \n");
            }
        }

        sb.append("]"); // Chiudi la parentesi quadra

        // Stampa il risultato
        System.out.println(sb.toString());
    }

    /*
     * Metodo per stampare una stringa con una lunghezza massima per riga
     * e rientro per le righe successive.
     */
    public static void printFormattedString(String caption, int maxLineLength) {
        String header = "Caption:"; // Aggiungiamo l'intestazione
        String[] words = caption.split(" ");   // Dividi la caption in parole
        StringBuilder currentLine = new StringBuilder(header); // La riga corrente inizia con l'intestazione
        StringBuilder result = new StringBuilder();

        for (String word : words) {

            // Controlla se aggiungere la parola corrente farebbe superare la lunghezza massima
            if (currentLine.length() + word.length() + 1 > maxLineLength) {

                // Se la riga è piena, aggiungiamo la riga formattata
                result.append(currentLine.toString()).append("\n");

                // Ora, le righe successive si allineano all'altezza di "Formatted Caption:"
                String indent = " ".repeat(header.length()); // Generiamo uno spazio uguale alla lunghezza dell'intestazione
                currentLine = new StringBuilder(indent + word); // Comincia una nuova riga con l'indentazione
            } else {
                // Se non superiamo la lunghezza, aggiungiamo la parola alla riga corrente
                if (currentLine.length() > 0) {
                    currentLine.append(" ");  // Aggiungi uno spazio prima della parola
                }
                currentLine.append(word);
            }
        }

        // Aggiungiamo l'ultima parte della riga che non è ancora stata aggiunta
        result.append(currentLine.toString());

        // Stampa il risultato finale
        System.out.println(result.toString());
    }
}