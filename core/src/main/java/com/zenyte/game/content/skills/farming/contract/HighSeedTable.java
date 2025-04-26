package com.zenyte.game.content.skills.farming.contract;

import com.zenyte.game.content.drops.table.DropTable;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;

/**
 * @author Christopher
 * @since 4/7/2020
 */
public class HighSeedTable {
    public static final DropTable table = new DropTable();

    static {
        table.append(ItemId.PAPAYA_TREE_SEED, 5, 1, 3)
                .append(ItemId.PALM_TREE_SEED, 5, 1, 2)
                .append(ItemId.HESPORI_SEED, 7, 1, 2)
                .append(ItemId.RANARR_SEED, 4, 1, 2)
                .append(ItemId.SNAPDRAGON_SEED, 4, 1)
                .append(ItemId.MAPLE_SEED, 4, 1, 2)
                .append(ItemId.MAHOGANY_SEED, 4, 1, 2)
                .append(ItemId.YEW_SEED, 3, 1)
                .append(ItemId.DRAGONFRUIT_TREE_SEED, 3, 1)
                .append(ItemId.CELASTRUS_SEED, 2, 1)
                .append(ItemId.TORSTOL_SEED, 2, 1)
                .append(ItemId.MAGIC_SEED, 1, 1)
                .append(ItemId.SPIRIT_SEED, 1, 1)
                .append(ItemId.REDWOOD_TREE_SEED, 1, 1);
    }

    public static Item roll() {
        return table.rollItem();
    }
}
