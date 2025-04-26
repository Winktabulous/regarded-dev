package com.zenyte.utils;


import com.zenyte.ContentConstants;
import mgi.utilities.ByteBuffer;
import net.runelite.api.WorldType;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;

public class WorldListGenerator {
   static ArrayList<World> worlds;

    static {
        worlds = new ArrayList<>();
        worlds.add(new World(1, EnumSet.of(WorldType.MEMBERS), "play.near-reality.com", ContentConstants.SERVER_NAME + " - Main", 1, 1750));
        worlds.add(new World(2, EnumSet.of(WorldType.MEMBERS), "play-beta.near-reality.com", ContentConstants.SERVER_NAME + " - Beta", 1, 777));
        worlds.add(new World(3, EnumSet.of(WorldType.MEMBERS), "127.0.0.1", ContentConstants.SERVER_NAME + " - Devs", 1, 10));
    }

    public static void main(String[] args) throws IOException {
        ByteBuffer buffer = new ByteBuffer();
        buffer.writeInt(0);
        buffer.writeUnsignedShort(worlds.size());
        for(World world: worlds) {
            buffer.writeUnsignedShort(world.id);
            buffer.writeInt(1); // Members = 1
            buffer.writeString(world.address);
            buffer.writeString(world.activity);
            buffer.writeUnsignedByte(1);
            buffer.writeUnsignedShort(world.players);
        }

        DataOutputStream fw = new DataOutputStream(new FileOutputStream("data\\worldlist.ws"));
        fw.write(buffer.toArray(0, buffer.getPosition()));
    }

    public static class World {
        int id;
        private EnumSet<WorldType> types;
        private String address;
        private String activity;
        private int location;
        private int players;

        public World(int id, EnumSet<WorldType> types, String address, String activity, int location, int players) {
            this.id = id;
            this.activity = activity;
            this.location = location;
            this.address = address;
            this.players = 2000;
            this.types = EnumSet.of(WorldType.MEMBERS);
        }

        public int getPlayers() {
            return players;
        }

        public void setPlayers(int players) {
            this.players = players;
        }

        public int getLocation() {
            return location;
        }

        public void setLocation(int location) {
            this.location = location;
        }

        public String getActivity() {
            return activity;
        }

        public void setActivity(String activity) {
            this.activity = activity;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public EnumSet<WorldType> getTypes() {
            return types;
        }

        public void setTypes(EnumSet<WorldType> types) {
            this.types = types;
        }
    }


}
