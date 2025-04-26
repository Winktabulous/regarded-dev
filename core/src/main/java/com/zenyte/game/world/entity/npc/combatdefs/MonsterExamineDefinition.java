package com.zenyte.game.world.entity.npc.combatdefs;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * @author Kris | 05/11/2018 01:22
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MonsterExamineDefinition {

    private String name;
    private Int2ObjectOpenHashMap<String> definitions;

    public String getName() {
        return name;
    }

    public Int2ObjectOpenHashMap<String> getDefinitions() {
        return definitions;
    }


}
