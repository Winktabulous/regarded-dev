package com.zenyte.game.content.area.prifddinas.zalcano;

import com.zenyte.game.world.entity.Location;

public final class ZalcanoConstants {

    public static final Location ZALCANO_LAYER_LOCATION = new Location(3033, 6068);
    public static final Location ZALCANO_OUTSIDE_LOCATION = new Location(3283, 6058);
    public static final Location ZALCANO_SPAWN_LOCATION = new Location(3031, 6048);

    public static final int ZALCANO_TELEPORT_PLATFORM_ID = 36_197;
    public static final int ZALCANO_OUTSIDE_TELEPORT_PLATFORM_ID = 36_198;

    public static final int ZALCANO_FURNACE = 36_195;
    public static final int ZALCANO_IMBUE_ALTAR = 36_196;
    public static final int ZALCANO_BARRIER = 36_201;

    //NPC Id's
    public static final int RHIANNON_ID = 9052;
    public static final int ZALCANO_MINABLE = 9050;
    public static final int ZALCANO_DEFAULT = 9049;
    public static final int ZALCANO_GOLEM = 9051;

    public static final int ZALCANO_SPAWN_SYMBOLS_ANIMATION_ID = 8433;
    public static final int ZALCANO_DEATH_ANIMATION = 8440;

    public static final int ZALCANO_RISE_ANIMATION = 8439;

    public static final int DEMONIC_ORANGE_SYMBOL_ID = 36_199;
    public static final int DEMONIC_BLUE_SYMBOL_ID = 36_200;

    public static final int IMBUED_TEPHRA_GFX_PROJECTILE_ID = 1731;
    // GFX's
    public static final int FALLEN_BOULDER_GFX = 1727;

    // Rocks
    public static final int GLOWING_ROCK_FORMATION = 36192;
    public static final int ROCK_FORMATION = 36193;// 46418
    public static final int DEPLETED_ROCK_FORMATION = 36194;

    // Items
    public static final int TEPHRA_ITEM_ID = 23_905;
    public static final int REFINED_TEPHRA_ITEM_ID = 23_906;
    public static final int IMBUED_TEPHRA_ITEM_ID = 23_907;

    public static final int SHARDS_ITEM = 23866;
    public static final int INFERNAL_ASHES_ITEM = 25778;


    // Misc
    public static final String NAME = "Zalcano Lair";
    public static final String WARNING_NAME = "zalcano_warning";

    // Send Messages
    public static final String REFINE_TEPHRA = "You heat the tephra in the furnace and smelt it down into a refined lump.";
    public static final String IMBUE_TEPHRA = "You bind the altar's power into some refined tephra.";

    private ZalcanoConstants() throws IllegalAccessException {
        throw new IllegalAccessException("Cannot init this class");
    }


}
