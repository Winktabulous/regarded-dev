package com.zenyte.game.world;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.near_reality.util.gson.Int2ObjectMapDeserializer;
import com.near_reality.util.gson.IntListTypeAdapter;
import com.near_reality.util.gson.Object2IntMapDeserializer;
import com.near_reality.util.gson.ObjectCollectionDeserializer;
import com.zenyte.ContentConstants;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectCollection;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public enum DefaultGson {
    ;

    /**
     * The Gson loader
     */
    private static final ThreadLocal<Gson> gson = ThreadLocal.withInitial(() ->
            new GsonBuilder()
                    .setDateFormat("MMM dd, yyyy, hh:mm:ss a")
                    .disableHtmlEscaping()
                    .setPrettyPrinting()
                    .registerTypeAdapter(IntList.class, IntListTypeAdapter.INSTANCE)
                    .registerTypeAdapter(Object2IntMap.class, Object2IntMapDeserializer.INSTANCE)
                    .registerTypeAdapter(Int2ObjectMap.class, Int2ObjectMapDeserializer.INSTANCE)
                    .registerTypeAdapter(ObjectCollection.class, ObjectCollectionDeserializer.INSTANCE)
                    .create());

    public static Gson getGson() {
        return gson.get();
    }

    public static <T> T fromGsonString(String jsonString, Class<T> classOfT) {
        return getGson().fromJson(jsonString.replace("%SERVER_NAME%", ContentConstants.SERVER_NAME), classOfT);
    }

    public static <T> T fromGson(BufferedReader reader, Class<T> classOfT) {
        String jsonString = reader.lines().collect(Collectors.joining());
        return fromGsonString(jsonString, classOfT);
    }

    public static <T> T fromGson(Path filePath, Class<T> classOfT) throws IOException {
        String jsonString = Files.readString(filePath);
        return fromGsonString(jsonString, classOfT);
    }

    public static <T> T fromGson(File file, Class<T> classOfT) throws IOException {
        return fromGson(file.toPath(), classOfT);
    }

    public static <T> T fromGson(String filePath, Class<T> classOfT) throws IOException {
        return fromGson(Path.of(filePath), classOfT);
    }

}
