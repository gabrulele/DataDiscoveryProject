package com.example.datadiscovery.utils;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateDocuments {

    public List<Document> parseJsonToDocuments(List<TableData> tableDataList) throws IOException {
        List<Document> documents = new ArrayList<>();

        if (tableDataList == null) {
            throw new IOException("La lista specificata non contiene tabelData o non è accessibile.");
        }

        for (TableData tableData : tableDataList) {

            // Estrazione dei campi
            String caption = tableData.getCaption();
            String table = tableData.getTable();
            List<String> footnotes = tableData.getFootnotes();
            List<String> references = tableData.getReferences();

            // Creazione del documento Lucene
            Document document = new Document();
            document.add(new TextField("caption", caption, TextField.Store.YES));
            document.add(new TextField("table", table, TextField.Store.YES));

            // Aggiunta di ciascun footnote come un valore separato nel campo "footnotes"
            for (String footnote : footnotes) {
                document.add(new TextField("footnotes", footnote, TextField.Store.YES));
            }

            // Aggiunta di ciascuna reference come un valore separato nel campo "references"
            for (String reference : references) {
                document.add(new TextField("references", reference, TextField.Store.YES));
            }

            documents.add(document);
        }
        return documents;
    }
}

