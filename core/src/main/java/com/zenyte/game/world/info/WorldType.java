package com.zenyte.game.world.info;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * The different supported {@link WorldType} entries.
 *
 * @author Tom
 */
public enum WorldType {
    REGULAR(1, "127.0.0.1", 43594, 2, "Main World", "Canada", 1), REGULAR_2(2, "127.0.0.1", 43593, 1, "Main world - Members", "Canada", 1), DEVELOPMENT(3, "127.0.0.1", 0, 1, "Development world", "Canada", 1);
    public static final WorldType[] VALUES = values();
    public static final String WORLD_AUTHENTICATION_KEY = "T;Z0z_Mc-(WR,K}d1sH]m.;-Mh@G/4E(Ag?axHg^P$GQq#:KJc5P~O)\"k-<F+G;";
    public static final Int2ObjectOpenHashMap<WorldType> WORLDS = new Int2ObjectOpenHashMap<WorldType>(VALUES.length);
    static int amount = 0;

    static {
        for (final WorldType value : VALUES) {
            WORLDS.put(value.worldId, value);
        }
    }

    private final int port;
    private final int worldId;
    private final String address;
    private final int flag;
    private final String activity;
    private final int country;

    WorldType(final int worldId, final String address, final int port, final int flag, final String activity, final String region, final int country) {
        this.worldId = worldId;
        this.address = address;
        this.port = port;
        this.activity = activity;
        this.flag = flag;
        this.country = country;
    }

    public static final void updateWorldList() throws IOException {
        final RandomAccessFile file = new RandomAccessFile("C:/Users/Kris/Dropbox/Public/output", "rw");
        file.writeInt(0);
        file.writeShort(WorldType.VALUES.length);
        for (final WorldType world : WorldType.VALUES) {
            file.writeShort(world.worldId);
            file.writeInt(world.flag);
            writeString(file, world.address);
            writeString(file, world.activity);
            file.writeByte(world.country);
            file.writeShort(amount++);
        }
    }

    private static void writeString(final RandomAccessFile out, final String string) throws IOException {
        final byte[] bytes = string.getBytes();
        out.write(bytes);
        out.writeByte(0);
    }

    public int getWorldId() {
        return worldId;
    }

    public int getPort() {
        return port;
    }

    public String getAddress() {
        return address;
    }

    public int getFlag() {
        return flag;
    }

    public String getActivity() {
        return activity;
    }

    public int getCountry() {
        return country;
    }
}
