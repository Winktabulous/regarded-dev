package com.zenyte.game.parser.impl;

import com.google.gson.Gson;
import com.zenyte.game.parser.Parse;
import com.zenyte.game.world.DefaultGson;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.types.config.items.JSONItemDefinitions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class JSONItemDefinitionsLoader implements Parse {
    private static final Logger log = LoggerFactory.getLogger(JSONItemDefinitionsLoader.class);
    public static final Int2ObjectMap<JSONItemDefinitions> definitions = new Int2ObjectOpenHashMap<>();

    @Override
    public void parse() throws Throwable {
        Path path = Path.of("data", "items", "ItemDefinitions.json");
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            JSONItemDefinitions[] definitions = DefaultGson.getGson().fromJson(reader, JSONItemDefinitions[].class);
            for (final JSONItemDefinitions def : definitions) {
                if (def != null) {
                    JSONItemDefinitionsLoader.definitions.put(def.getId(), def);
                }
            }
        }
    }

    public static void main(final String[] args) {
        try {
            new JSONItemDefinitionsLoader().parse();
        } catch (final Throwable e) {
            log.error("", e);
        }
    }

    /**
     * Looks up a definition based on the key value in the map.
     *
     * @param itemId the key value we using to search for the respective
     *               definition.
     * @return
     */
    public static JSONItemDefinitions lookup(final int itemId) {
        return getDefinitions().get(itemId);
    }

    /**
     * Gets the definitions map.
     *
     * @return
     */
    public static Int2ObjectMap<JSONItemDefinitions> getDefinitions() {
        return definitions;
    }

    public static void save() {
        final Gson gson = DefaultGson.getGson();
        final Collection<JSONItemDefinitions> values = definitions.values();
        final Comparator<JSONItemDefinitions> comparator = (npc1, npc2) -> {
            if (npc1 == null || npc2 == null) {
                return 0;
            }
            return npc1.getId() > npc2.getId() ? 1 : -1;
        };
        final List<JSONItemDefinitions> list = new ArrayList<>(values);
        list.sort(comparator);
        final String toJson = gson.toJson(list);
        try {
            final PrintWriter pw = new PrintWriter("data/items/ItemDefinitions.json", StandardCharsets.UTF_8);
            pw.println(toJson);
            pw.close();
        } catch (final Exception e) {
            log.error("", e);
        }
    }
}
