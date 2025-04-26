package com.zenyte.server;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.nio.file.Path;

/**
 * @author Kryeus / John J. Woloszyk
 */
public class ServerAttributes extends AttributesSerializable {
    public static String getSaveFile() {
        return Path.of("data", "server_attributes.json").toString();
    }

    public ServerAttributes() {
        super(getSaveFile());
    }

    @Override
    public Type getType() {
        return new TypeToken<ServerAttributes>() {}.getType();
    }
}
