package com.zenyte.game.content.minigame.duelarena;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kris | 22. dets 2017 : 18:58.15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum DuelSetting {

	NO_RANGED("No Ranged", 4, 41, 54),
	NO_MELEE("No Melee", 5, 42, 55),
	NO_MAGIC("No Magic", 6, 43, 56),
	NO_SPECIAL_ATTACK("No Special attacks", 13, 49, 62),
	NO_FUN_WEAPONS("Fun weapons", 12, 48, 61),
	NO_FORFEIT("Forfeit", 0, 37, 50),
	NO_PRAYER("No Prayers", 9, 46, 59),
	NO_DRINKS("No Drinks", 7, 44, 57),
	NO_FOOD("No Food", 8, 45, 58),
	NO_MOVEMENT("No Movement", 1, 38, 51),
	OBSTACLES("Obstacles", 10, 47, 60),
	NO_WEAPON_SWITCH("No Weapon Switching", 2, 39, 52),
	SHOW_INVENTORIES("Show Inventories", 3, 40, 53),
	HEAD("Disable Head Slot", 14, 69),
	BACK("Disable Back Slot", 15, 70),
	NECK("Disable Neck Slot", 16, 71),
	LEFT_HAND("Disable Left hand Slot", 17, 72),
	TORSO("Disable Torso Slot", 18, 73),
	RIGHT_HAND("Disable Right hand Slot", 19, 74),
	LEG("Disable Leg Slot", 21, 75),
	HAND("Disable Hand Slot", 23, 76),
	FEET("Disable Feet Slot", 24, 77),
	RING("Disable Ring Slot", 26, 78),
    AMMUNITION("Disable Ammo Slot", 27, 79);

    public static final Map<Integer, DuelSetting> SETTINGS = new HashMap<Integer, DuelSetting>();
    public static final DuelSetting[] VALUES = values();

    static {
        for (DuelSetting setting : values())
            for (int comp : setting.components)
                SETTINGS.put(comp, setting);
    }

    private final int bit;
    private final int[] components;
    private final String name;

    DuelSetting(final String name, final int bit, final int... components) {
        this.name = name;
        this.bit = bit;
        this.components = components;
    }

    public int getBit() {
        return bit;
    }

    public int[] getComponents() {
        return components;
    }

    public String getName() {
        return name;
    }

}
