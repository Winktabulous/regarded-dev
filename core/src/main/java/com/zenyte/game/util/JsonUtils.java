package com.zenyte.game.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-09-20
 */
public class JsonUtils {

    public static Gson getGson() {
        return new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context) ->
                LocalDate.parse(json.getAsString()))

            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
                new JsonPrimitive(src.toString()))
            .create();
    }

    public static String toJson(Object object) {
        return getGson().toJson(object);
    }
}
