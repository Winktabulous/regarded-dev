package com.zenyte.game.world.entity.npc.actions;

import com.google.common.reflect.TypeToken;
import com.zenyte.game.world.DefaultGson;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public enum NpcActions {
    ;

    public static final String DEFAULT_OPTIONS_JSON_PATH = "assets/npc_options.json";
    private static final Type TYPE = new TypeToken<Map<Integer, List<String>>>() {}.getType();

    public static Int2ObjectMap<List<String>> loadUsedNpcOptions(Path path) {
        if (path == null)
            path = Paths.get(DEFAULT_OPTIONS_JSON_PATH);
        Int2ObjectMap<List<String>> list = new Int2ObjectOpenHashMap<>(10 * 1024);
        try {
            final FileReader reader = new FileReader(path.toFile());
            list.putAll(DefaultGson.getGson().fromJson(reader, TYPE));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }
}
