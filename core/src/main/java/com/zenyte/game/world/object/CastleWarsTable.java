package com.zenyte.game.world.object;

import com.zenyte.game.item.Item;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public enum CastleWarsTable {
    POTIONS(4463, new Item(4045, 1)), ROCKS(4460, new Item(4043, 1)), ROPE(4462, new Item(954, 1)), BARRICADES(4461, new Item(4053, 1)), TOOLKITS(4459, new Item(4051, 1)), PICKAXES(4464, new Item(1265, 1)), BANDAGES(4458, new Item(4049, 1));
    public static final Map<Integer, CastleWarsTable> DATA = new HashMap<>();

    static {
        for (final CastleWarsTable table : values()) DATA.put(table.getObject(), table);
    }

    private final int object;

    CastleWarsTable(final int object, final Item loot) {
        this.object = object;
        this.loot = loot;
    }

    public static CastleWarsTable getData(final int objectId) {
        final CastleWarsTable entry = DATA.get(objectId);
        return entry == null ? PICKAXES : entry;
    }

    private final Item loot;

    public int getObject() {
        return object;
    }

    public Item getLoot() {
        return loot;
    }
}
