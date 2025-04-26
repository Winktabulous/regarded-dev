package com.zenyte.server;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JsonUtil {
    public static final ObjectMapper JSON_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());
    public static final ObjectWriter JSON_WRITER;
    private static final Gson gson = new GsonBuilder().create();
    private static final Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();

    static {
        JSON_MAPPER.registerModule(new KotlinModule.Builder().build());
        JSON_MAPPER.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        JSON_MAPPER.setVisibility(JSON_MAPPER.getSerializationConfig().
                getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        JSON_WRITER = JSON_MAPPER.writerWithDefaultPrettyPrinter();
    }
    @Deprecated
    public static <T> void toJson(T t, String filePath) {
        Gson prettyGson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        String prettyJson = prettyGson.toJson(t);
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(new File(filePath)));
            bw.write(prettyJson);
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> String toJson(T t) {
        return prettyGson.toJson(t);
    }

    public static <T> String toSimpleJson(T t) {
        return gson.toJson(t);
    }

    public static <T> T fromJsonText(String text, TypeToken<T> typeToken) {
        return prettyGson.fromJson(text, typeToken.getType());
    }

    @Deprecated
    public static <T> T fromJson(String filePath) throws IOException {
        return fromJson(filePath, new TypeToken<>(){});
    }

    @Deprecated
    public static <T> T fromJson(String filePath, TypeToken<T> typeToken) throws IOException {
        return new Gson().fromJson(FileUtils.readFileToString(new File(filePath)), typeToken.getType());
    }

    @Deprecated
    public static <T> T fromJsonOrDefault(String filePath, TypeToken<T> typeToken, T defaultObject) throws IOException {
        if (!new File(filePath).exists()) {
            return defaultObject;
        }

        return fromJson(filePath, typeToken);
    }

    public static <T> void toJacksonJson(T t, String filePath) throws IOException {
        JSON_WRITER.writeValue(new File(filePath), t);
    }

    public static JsonNode fromJacksonJson(File file) throws IOException {
        return JSON_MAPPER.readTree(file);
    }

    public static <T> T fromJacksonJson(File file, TypeReference<T> clazz) throws IOException {
        return fromJacksonJson(file.getPath(), clazz);
    }

    public static <T> T fromJacksonJsonString(String json, TypeReference<T> clazz) throws IOException {
        return JSON_MAPPER.readValue(json, clazz);
    }

    public static <T> T fromJacksonJson(String filePath, TypeReference<T> clazz) throws IOException {
        return JSON_MAPPER.readValue(new File(filePath), clazz);
    }


    public static Gson pretty() {
        return prettyGson;
    }

    public static Gson normal() {
        return gson;
    }
}
