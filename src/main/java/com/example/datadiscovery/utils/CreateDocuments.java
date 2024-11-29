package com.example.datadiscovery.utils;

import org.apache.lucene.document.*;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateDocuments {

    EmbeddingModel model= new AllMiniLmL6V2EmbeddingModel();

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

           //creazione del field embedded per le caption
           if(caption!=null && !caption.trim().isEmpty()){
           TextSegment segment = TextSegment.from(caption);
           Embedding embedding = model.embed(segment).content();
           document.add(new KnnFloatVectorField("embedding", embedding.vector()));
           }

            documents.add(document);
        }
        return documents;
    }
}

