package com.example.datadiscovery.utils;

import com.example.datadiscovery.utils.TableData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Map;

public class JsonTableParser {
    public Map<String, TableData> parseJsonFile(String filePath) {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(filePath)) {

            // Specifica che stiamo deserializzando una mappa di `TableData`
            Type mapType = new TypeToken<Map<String, TableData>>(){}.getType();
            return gson.fromJson(reader, mapType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
