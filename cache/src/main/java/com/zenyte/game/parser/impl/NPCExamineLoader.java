package com.zenyte.game.parser.impl;

import com.zenyte.game.util.Examine;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public enum NPCExamineLoader {
    ;

    private static final Int2ObjectMap<Examine> map = new Int2ObjectOpenHashMap<>();

    public static Examine get(final int npcId) {
        return map.get(npcId);
    }

    public static void insert(int npcID, Examine examine) {
        map.put(npcID, examine);
    }

}
