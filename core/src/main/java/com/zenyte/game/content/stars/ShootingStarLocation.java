package com.zenyte.game.content.stars;

import java.util.EnumSet;
import java.util.Set;

/**
 * @author Andys1814
 */
public enum ShootingStarLocation {
    // Asgarnia
    DWARVEN_MINE("by the Dwarven Mine northern entrance", 3018, 3443, 0),
    MINING_GUILD("near the Mining Guild entrance", 3030, 3348, 0),
    FALADOR_MINE("near the West Falador Mine", 2906, 3355, 0),
    TAVERLY("near Taverley (White Wolf Tunnel entrance)", 2882, 3474, 0),
    CRAFTING_GUILD("inside of the Crafting Guild", 2940, 3280, 0),
    RIMMINGTON_MINE("by the Rimmington Mine", 2974, 3241, 0),

    // Crandor and Karamja
    SOUTH_CRANDOR_MINE("by the South Crandor Mine", 2822, 3238, 0),
    NORTH_CRANDOR_MINE("by the North Crandor Mine", 2835, 3296, 0),
    NORTH_BRIMHAVEN_MINE("near the North Brimhaven Mine", 2736, 3221, 0),
    SOUTH_BRIMHAVEN_MINE("near the South Brimhaven Mine", 2742, 3143, 0),
    KARAMJA_JUNGLE_MINE("near the Karamja Jungle mine (Nature Altar)", 2845, 3037, 0),
    SHILO_VILLAGE_MINE("by the Shilo Village Mine", 2827, 2999, 0),

    // Feldip Hills
    FELDIP_HUNTER_AREA("near the Feldip Hunter Area", 2571, 2964, 0),
    RANTZ_CAVE("by Rantz's Cave", 2630, 2993, 0),
    CORSAIR_COVE("near Corsair Cove", 2567, 2858, 0),
    CORSAIR_RESOURCE_AREA("near the Corsair Cove Resource Area", 2483, 2886, 0),
    MYTHS_GUILD("by the Myths' Guild", 2483, 2862, 0),

    // Fossil Island and Mos Le'Harmless
    FOSSIL_ISLAND_MINE("by the Fossil Island Mine", 3774, 3814, 0),
    VOLCANIC_MINE("by the Volcanic Mine entrance", 3818, 3801, 0),
    MOS_LEHARMLESS("in Mos Le'Harmless", 3686, 2969, 0),

    // Fremennik Lands and Lunar Isle
    RELLEKKA_MINE("by the Rellekka Mine", 2683, 3699, 0),
    KELDAGRIM_ENTRANCE_MINE("by the Keldagrim Entrance mine", 2727, 3683, 0),
    MISCELLANIA_MINE("near the Miscellania Mine", 2528, 3887, 0),
    JATIZSO_MINE("near the Jatizso Mine", 2393, 3814, 0),
    CENTRAL_FREMENNIK_ISLES_MINE("by the Central Fremennik Isles Mine", 2375, 3832, 0),
    LUNAR_ISLE_MINE("by the Lunar Isle Mine", 2139, 3938, 0),

    // Great Kourend
    HOSIDIUS_MINE("by the Hosidius Mine", 1778, 3493, 0),
    SHAYZIEN_MINE("by the Shayzien mine", 1597, 3648, 0),
    PORT_PISCARILIUS_MINE("near the Port Piscarilius Mine", 1769, 3709, 0),
    DENSE_ESSENCE_MINE("near the Dark Essence Mine", 1760, 3853, 0),
    LOVAKITE_MINE("near the Lovakite Mine", 1437, 3840, 0),
    LOVAKENJ_BANK("in Lovakengj (bank)", 1534, 3747, 0),

    // Kandarin
    CATHERBY("in Catherby", 2804, 3434, 0),
    YANILLE("in Yanille", 2602, 3086, 0),
    PORT_KHAZARD_MINE("near the Port Khazard Mine", 2624, 3141, 0),
    LEGENDS_GUILD_MINE("near the Legends' Guild Mine", 2705, 3333, 0),
    COAL_TRUCKS("by the Coal Trucks", 2589, 3478, 0),
    SOUTH_EAST_ARDOUGNE_MINE("by the South-east Ardougne Mine (monastery)", 2608, 3233, 0),

    // Kebos Lowlands
    KEBOS_LOWLANDS_MINE("by the Kebos Lowlands mine (Kebos Swamp)", 1210, 3651, 0),
    MOUNT_KARUULM_MINE("by the Mount Karuulm mine", 1279, 3817, 0),
    MOUNT_KARUULM_BANK("near the Mount Karuulm bank", 1322, 3816, 0),
    MOUNT_QUIDAMORTEM_BANK("near the Mount Quidamortem bank", 1258, 3564, 0),

    // Kharidian Desert
    AL_KHARID_MINE("near the Al Kharid mine", 3296, 3298, 0),
    AL_KHARID("in Al Kharid (bank)", 3276, 3164, 0),
    UZER_MINE("near the Uzer Mine", 3424, 3160, 0),
    DESERT_QUARRY("near the Desert quarry", 3171, 2910, 0),
    AGILITY_PYRAMID_MINE("near the Agility Pyramid mine", 3316, 2867, 0),
    NARDAH("in Nardah", 3434, 2889, 0),

    // Misthalin
    EAST_LUMBRIDGE_SWAMP_MINE("near the East Lumbridge Swamp mine", 3230, 3155, 0),
    WEST_LUMBRIDGE_SWAMP_MINE("near the West Lumbridge Swamp mine", 3153, 3150, 0),
    DRAYNOR_VILLAGE("near Draynor Village", 3094, 3235, 0),
    VARROCK_EAST_BANK("by the Varrock (East bank)", 3258, 3408, 0),
    SOUTH_EAST_VARROCK_MINE("near the South-east Varrock mine", 3290, 3353, 0),
    SOUTH_WEST_VARROCK_MINE("near the South-west Varrock mine", 3175, 3362, 0),

    // Morytania
    CANIFIS_BANK("in Canifis (bank)", 3505, 3485, 0),
    BURGH_DE_ROTT_BANK("in Burgh de Rott (bank)", 3500, 3219, 0),
    ABANDONED_MINE("near the Abandoned Mine", 3451, 3233, 0),
    VER_SINHAZA_BANK("in Ver Sinhaza (bank)", 3650, 3214, 0),
    DAEYALT_ESSENCE_MINE("near the Daeyalt Essence mine entrance", 3635, 3340, 0),

    // Piscatoris and the Gnome Stronghold
    PISCATORIS_MINE("near the Piscatoris mine", 2341, 3635, 0),
    GRAND_TREE("near the Grand Tree", 2444, 3490, 0),
    TREE_GNOME_STRONHOLD_BANK("near the Tree Gnome Stronghold bank", 2448, 3436, 0),

    // Tirannwn
    ISAFDAR_MINE("near the Isafdar mine", 2269, 3158, 0),
    ARANDAR_MINE("near the Arandar mine", 2318, 3269, 0),
    LLETYA("in Lletya", 2329, 3163, 0),
    TRAHAEARN_MINE_ENTRANCE("by the Trahaearn mine entrance", 3274, 6055, 0),

    // Wilderness - Chance of Blood Money, 50% more star dust
    SOUTH_WILDERNESS_MINE("near the South Wilderness mine (Mage of Zamorak)", 3108, 3569, 0),
    SOUTH_WEST_WILDERNESS_MINE("by the South-west Wilderness Mine (South of Dark Warriors' Fortress)", 3018, 3593, 0),
    BANDIT_CAMP_MINE("near the Bandit Camp mine (Hobgoblins)", 3093, 3756, 0),
    LAVA_MAZE_RUNITE_MINE("by the Lava Maze runite mine", 3057, 3887, 0),
//    RESOURCE_AREA("near the Resource Area", 3188, 3932, 0),
    MAGE_ARENA("outside of the Mage Arena", 3091, 3962, 0),
    PIRATES_HIDEOUT_MINE("near the Pirates' Hideout", 3049, 3940, 0);

    public static final Set<ShootingStarLocation> LOCATIONS = EnumSet.allOf(ShootingStarLocation.class);

    private final String location;

    private final int x;
    private final int y;
    private final int z;

    ShootingStarLocation(String location, int x, int y, int z) {
        this.location = location;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getLocation() {
        return location;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
