package com.example.datadiscovery.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonToTableData {

    private boolean isTableData(JsonElement value) {

        // Verifica se il valore è un oggetto JSON che può essere interpretato come TableData
        if (value.isJsonObject()) {
            JsonObject obj = value.getAsJsonObject();

            // Controlla se l'oggetto contiene i campi "caption" e "table", senza controllare se sono vuoti
            return obj.has("caption") && obj.has("table");
        }
        return false;
    }

    public List<TableData> createTableDataMap(String directoryPath) throws IOException {

        List<TableData> tableDataList = new ArrayList<>();

        File directory = new File(directoryPath);
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));

        if (files == null) {
            throw new IOException("La directory specificata non contiene file JSON o non è accessibile.");
        }

        Gson gson = new Gson();

        for (File file : files) {
            try (Reader reader = new FileReader(file)) {
                JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

                // Itera tutte le chiavi del JSON
                for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                    String key = entry.getKey();
                    JsonElement value = entry.getValue();

                    if (!isTableData(value)) {
                        continue;  // Salta la chiave se il valore non è un oggetto TableData
                    }

                    // Gestisci il campo "footnotes", che può essere sia una stringa che un array
                    JsonObject obj = value.getAsJsonObject();

                    String caption = obj.has("caption") ? obj.get("caption").getAsString() : "";
                    String table = obj.has("table") ? obj.get("table").getAsString() : "";

                    //
                    List<String> footnotes = new ArrayList<>();
                    if (obj.has("footnotes")) {
                        JsonElement footnotesElement = obj.get("footnotes");
                        if (footnotesElement.isJsonArray()) {

                            // Se è un array, aggiungi tutti gli elementi
                            for (JsonElement footnote : footnotesElement.getAsJsonArray()) {
                                footnotes.add(footnote.getAsString());
                            }
                        } else if (footnotesElement.isJsonPrimitive()) {

                            // Se è una stringa, aggiungila come singolo elemento
                            footnotes.add(footnotesElement.getAsString());
                        }
                    }

                    // Gestisci il campo "references", che può essere sia una lista di stringhe che una lista di liste di stringhe
                    List<String> references = new ArrayList<>();
                    if (obj.has("references")) {
                        JsonElement referencesElement = obj.get("references");
                        if (referencesElement.isJsonArray()) {

                            // Se è un array di oggetti
                            for (JsonElement reference : referencesElement.getAsJsonArray()) {
                                if (reference.isJsonArray()) {

                                    // Se ogni elemento dell'array è un altro array di stringhe
                                    for (JsonElement ref : reference.getAsJsonArray()) {
                                        references.add(ref.getAsString());
                                    }
                                } else if (reference.isJsonPrimitive()) {
                                    // Se l'elemento è una singola stringa, aggiungila alla lista
                                    references.add(reference.getAsString());
                                }
                            }
                        }
                    }

                    // Crea un oggetto TableData
                    TableData tableData = new TableData(caption, table, footnotes, references);

                    // Inserisci la coppia chiave TableData conforme nella mappa
                    tableDataList.add(tableData);
                }
            } catch (Exception e) {
                System.err.println("Errore durante il parsing del file: " + file.getName());
                e.printStackTrace();
            }
        }
        return tableDataList;
    }
}

