package com.zenyte.game.content.skills.firemaking;

import com.zenyte.game.item.Item;

import java.util.HashMap;
import java.util.Map;

/**
 * Original @author Tommeh.
 * 
 * @author Kris | 27. march 2018 : 22:17.28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>
 */
public enum Firemaking {

	KINDLING(1, 48, new Item(20799), 26185),
    NORMAL(1, 40, new Item(1511)),
    RED_LOGS(1, 50, new Item(7404), 26186),
    GREEN_LOGS(1, 50, new Item(7405), 26575),
    BLUE_LOGS(1, 50, new Item(7406), 26576),
    WHITE_LOGS(1, 50, new Item(10328), 20000),
    PURPLE_LOGS(1, 50, new Item(10329), 20001),
    ACHEY_TREE(1, 40, new Item(2862)),
    OAK(15, 60, new Item(1521)),
    WILLOW(30, 90, new Item(1519)),
    TEAK(35, 105, new Item(6333)),
    ARCTIC_PINE(42, 125, new Item(10810)),
    MAPLE(45, 135, new Item(1517)),
    MAHOGANY(50, 157.5, new Item(6332)),
    YEW(60, 202.5, new Item(1515)),
    MAGIC(75, 303.8, new Item(1513)),
    REDWOOD(90, 350, new Item(19669));

    public static final Firemaking[] VALUES = values();
    public static final Map<Integer, Firemaking> MAP = new HashMap<>(VALUES.length);

    static {
        for (Firemaking val : VALUES) {
            MAP.put(val.logs.getId(), val);
        }
    }

    private final int level;
    private final double xp;
    private final Item logs;
    private final int objectId;

    Firemaking(final int level, final double xp, final Item logs) {
        this(level, xp, logs, 5249);
    }

    Firemaking(final int level, final double xp, final Item logs, final int objectId) {
        this.level = level;
        this.xp = xp;
        this.logs = logs;
        this.objectId = objectId;
    }

    public int getLevel() {
        return level;
    }

    public double getXp() {
        return xp;
    }

    public Item getLogs() {
        return logs;
    }

    public int getObjectId() {
        return objectId;
    }


}