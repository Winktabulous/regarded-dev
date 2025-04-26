package com.zenyte.game.model.item.enums;

import com.zenyte.game.item.ItemId;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * @author Kris | 07/05/2019 20:37
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum UpgradeKit {

    STEAM_STAFF(12795, 12798, 11787),
    MYSTIC_STEAM_STAFF(12796, 12798, 11789),
    LAVA_STAFF(21198, 21202, 3053),
    MYSTIC_LAVA_STAFF(21200, 21202, 3054),
    DRAGON_PICKAXE(12797, 12800, 11920),
    ODIUM_WARD(12807, 12802, 11926),
    MALEDICTION_WARD(12806, 12802, 11924),
    GRANITE_MAUL(12848, ItemId.GRANITE_CLAMP, 4153),
    GRANITE_MAUL_OR_ORNATE_HANDLE(24227, ItemId.GRANITE_CLAMP, 24225),
    GRANITE_MAUL_ORNATE_HANDLE(24225, 24229, 4153);

    private final int completeItem, kit, baseItem;

    public static final UpgradeKit[] values = values();
    public static final Int2ObjectOpenHashMap<UpgradeKit> MAPPED_VALUES =
            new Int2ObjectOpenHashMap<>(values.length);

    static {
        for (final UpgradeKit val : values) {
            MAPPED_VALUES.put(val.completeItem, val);
        }
    }

    UpgradeKit(final int completeItem, final int kit, final int baseItem) {
        this.completeItem = completeItem;
        this.kit = kit;
        this.baseItem = baseItem;
    }

    public int getCompleteItem() {
        return completeItem;
    }

    public int getKit() {
        return kit;
    }

    public int getBaseItem() {
        return baseItem;
    }

}
