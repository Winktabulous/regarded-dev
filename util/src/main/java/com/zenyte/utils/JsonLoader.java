package com.zenyte.utils;

import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class JsonLoader {

    private static final Logger log = LoggerFactory.getLogger(JsonLoader.class);

    /**
     * Allows the user to read and/or modify the parsed data.
     *
     * @param reader  the reader instance.
     * @param builder the builder instance.
     */
    public abstract void load(JsonObject reader, Gson builder);

    /**
     * Returns the path to the <code>.json</code> file that will be parsed.
     *
     * @return the path to the file.
     */
    public abstract String filePath();

    /**
     * Loads the parsed data. How the data is loaded is defined by
     * <code>load(JsonObject j, Gson g)</code>.
     *
     * @return the loader instance, for chaining.
     * @throws Exception if any exception occur while loading the parsed data.
     */
    public JsonLoader load() {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath()))) {
            JsonArray array = (JsonArray) JsonParser.parseReader(br);
            Gson builder = new GsonBuilder().create();
            for (int i = 0; i < array.size(); i++) {
                try {
                    JsonObject reader = (JsonObject) array.get(i);
                    load(reader, builder);
                } catch (Exception e) {
                    log.error("Exception caught while loading json entry at index: " + i + ", file: " + filePath());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.err.printf("Error loading Json file: %s%n", filePath());
            e.printStackTrace();
        }
        return this;
    }

}
