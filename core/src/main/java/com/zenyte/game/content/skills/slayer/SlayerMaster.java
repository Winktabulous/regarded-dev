package com.zenyte.game.content.skills.slayer;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Kris | 5. nov 2017 : 21:22.44
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public enum SlayerMaster {
    TURAEL(401, 1, 1, 0, "in Burthorpe"),
    KRYSTILIA(7663, 1, 1, 25, "in Edgeville"),
    MAZCHNA(402, 1, 20, 2, "in Canifis"),
    VANNAKA(403, 1, 40, 4, "within the Edgeville dungeon"),
    CHAELDAR(404, 1, 70, 10, "in Zanaris"),
    NIEVE(490, 1, 85, 12, "in Tree Gnome Stronghold"),
    DURADEL(405, 50, 100, 15, "in Shilo Village"),
    KONAR_QUO_MATEN(8623, 1, 75, 18, "On Mount Karuulm"),
    SUMONA(16064, 99, 100, 15, "at Home"),
    ;

    private final int npcId;
    private final int slayerRequirement;
    private final int combatRequirement;
    private final int pointsPerTask;
    private final String location;

    SlayerMaster(int npcId, int slayerRequirement, int combatRequirement, int pointsPerTask, String location) {
        this.npcId = npcId;
        this.slayerRequirement = slayerRequirement;
        this.combatRequirement = combatRequirement;
        this.pointsPerTask = pointsPerTask;
        this.location = location;
    }

    public static boolean isMaster(final int id) {
        return mappedMasters.containsKey(id);
    }

    public final int getMultiplier(final int taskNum) {
        if (taskNum % 1000 == 0) {
            return 50;
        } else if (taskNum % 250 == 0) {
            return 35;
        } else if (taskNum % 100 == 0) {
            return 25;
        } else if (taskNum % 50 == 0) {
            return 15;
        } else if (taskNum % 10 == 0) {
            return 5;
        }
        return 1;
    }

    @Override
    public String toString() {
        if(this == SUMONA)
            return "Summona";
        return StringUtils.capitalize(name().replace('_', ' ').toLowerCase());
    }

    public int getNpcId() {
        return npcId;
    }

    public int getSlayerRequirement() {
        return slayerRequirement;
    }

    public int getCombatRequirement() {
        return combatRequirement;
    }

    public int getPointsPerTask() {
        return pointsPerTask;
    }

    public String getLocation() {
        return location;
    }

    public static final SlayerMaster[] values = values();
    public static final Int2ObjectMap<SlayerMaster> mappedMasters =
            new Int2ObjectOpenHashMap<>(values.length);

    static {
        for (final SlayerMaster master : values) {
            mappedMasters.put(master.npcId, master);
        }
    }

}
