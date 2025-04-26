package com.zenyte.game.content.grandexchange;

import com.zenyte.game.parser.Parse;
import com.zenyte.game.world.DefaultGson;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public final class JSONGEItemDefinitionsLoader implements Parse {

    private static final Logger log = LoggerFactory.getLogger(JSONGEItemDefinitionsLoader.class);

    private static final Int2ObjectMap<JSONGEItemDefinitions> definitions = new Int2ObjectOpenHashMap<>();

    public static Int2ObjectMap<JSONGEItemDefinitions> getDefinitions() {
        return definitions;
    }

    @Override
    public void parse() throws Throwable {
        final BufferedReader br = new BufferedReader(new FileReader("data/grandexchange/market_prices.json"));
        final JSONGEItemDefinitions[] priceDefinitions = DefaultGson.getGson().fromJson(br,
                JSONGEItemDefinitions[].class);
        for (final JSONGEItemDefinitions def : priceDefinitions) {
            if (def == null) {
                continue;
            }
            definitions.put(def.getId(), def);
        }
    }

    public static void main(String[] args) {
        try {
            new JSONGEItemDefinitionsLoader().parse();
            final BufferedReader reader = new BufferedReader(new FileReader("data/grandexchange/ge_prices.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                final String[] split = line.split(":");
                final int id = Integer.parseInt(split[0].trim());
                final int price = Integer.parseInt(split[1].replaceAll("[,.]", "").trim());
                JSONGEItemDefinitions definition = lookup(id);
                if (definition == null) {
                    definition = new JSONGEItemDefinitions();
                    definition.setId(id);
                }
                definition.setPrice(price);
                definitions.put(id, definition);
            }
            save(null);
        } catch (Throwable e) {
            log.error("", e);
        }
    }

    public static void save(Consumer<List<JSONGEItemDefinitions>> onLoaded) {
        final ArrayList<JSONGEItemDefinitions> list = new ArrayList<>(definitions.values());
        list.sort(Comparator.comparingInt(JSONGEItemDefinitions::getId));
        final String toJson = DefaultGson.getGson().toJson(list);
        try {
            final PrintWriter pw = new PrintWriter("data/grandexchange/market_prices.json", StandardCharsets.UTF_8);
            pw.println(toJson);
            pw.close();
        } catch (final Exception e) {
            log.error("", e);
        }

        if (onLoaded != null)
            onLoaded.accept(list);
    }

    /**
     * Looks up a definition based on the key value in the map.
     *
     * @param itemID the key value we're using to search for the respective definition.
     */
    public static JSONGEItemDefinitions lookup(int itemID) {
        return definitions.get(itemID);
    }

}
