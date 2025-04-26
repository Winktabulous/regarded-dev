package com.zenyte.game.content.skills.woodcutting;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tommeh | 7 feb. 2018 : 00:56:18
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public enum CanoeDefinitions {

	LOG(12, 30, 1, 20),
	DUGOUT(27, 60, 2, 18),
	STABLE_DUGOUT(42, 90, 3, 12),
	WAKA(57, 150, 4, 11);

	private final int level, bit, componentId;
	private final double experience;
	
	public static final CanoeDefinitions[] ENTRIES = values();
    public static final Map<Integer, CanoeDefinitions> CANOES = new HashMap<>();

    static {
        for (CanoeDefinitions def : ENTRIES) {
            CANOES.put(def.componentId, def);
        }
    }

    CanoeDefinitions(final int level, final double experience, final int bit, final int componentId) {
        this.level = level;
        this.experience = experience;
        this.bit = bit;
        this.componentId = componentId;
    }

    public static CanoeDefinitions get(final int componentId) {
        return CANOES.get(componentId);
    }

    public static CanoeDefinitions getDefinitionByBitValue(int bit) {
        for (CanoeDefinitions canoe : ENTRIES) {
            if (bit == canoe.getBit() || bit == (canoe.getBit() + 10))
                return canoe;
        }
        return null;
    }

    public int getLevel() {
        return level;
    }

    public int getBit() {
        return bit;
    }

    public int getComponentId() {
        return componentId;
    }

    public double getExperience() {
        return experience;
    }

}
