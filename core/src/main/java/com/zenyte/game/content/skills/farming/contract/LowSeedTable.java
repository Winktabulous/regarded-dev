package com.zenyte.game.content.skills.farming.contract;

import com.zenyte.game.content.drops.table.DropTable;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;

/**
 * @author Christopher
 * @since 4/7/2020
 */
public class LowSeedTable {
    public static final DropTable table = new DropTable();

    static {
        table.append(ItemId.POTATO_SEED, 2, 8, 12)
                .append(ItemId.ONION_SEED, 2, 8, 12)
                .append(ItemId.CABBAGE_SEED, 2, 8, 12)
                .append(ItemId.TOMATO_SEED, 2, 8, 12)
                .append(ItemId.SWEETCORN_SEED, 2, 8, 12)
                .append(ItemId.STRAWBERRY_SEED, 2, 8, 12)
                .append(ItemId.BARLEY_SEED, 2, 8, 14)
                .append(ItemId.HAMMERSTONE_SEED, 2, 6, 8)
                .append(ItemId.ASGARNIAN_SEED, 2, 6, 8)
                .append(ItemId.JUTE_SEED, 2, 8, 12)
                .append(ItemId.YANILLIAN_SEED, 2, 6, 8)
                .append(ItemId.KRANDORIAN_SEED, 2, 6, 8)
                .append(ItemId.ACORN, 2, 3, 5)
                .append(ItemId.APPLE_TREE_SEED, 2, 3, 5)
                .append(ItemId.BANANA_TREE_SEED, 2, 3, 5)
                .append(ItemId.ORANGE_TREE_SEED, 2, 3, 5)
                .append(ItemId.CURRY_TREE_SEED, 2, 3, 5)
                .append(ItemId.REDBERRY_SEED, 2, 6, 8)
                .append(ItemId.CADAVABERRY_SEED, 2, 6, 8)
                .append(ItemId.DWELLBERRY_SEED, 2, 6, 8)
                .append(ItemId.JANGERBERRY_SEED, 2, 6, 8)
                .append(ItemId.MARIGOLD_SEED, 2, 8, 12)
                .append(ItemId.ROSEMARY_SEED, 2, 8, 12)
                .append(ItemId.NASTURTIUM_SEED, 2, 8, 12)
                .append(ItemId.WOAD_SEED, 2, 8, 12)
                .append(ItemId.GUAM_SEED, 2, 3, 5)
                .append(ItemId.MARRENTILL_SEED, 2, 3, 5)
                .append(ItemId.HARRALANDER_SEED, 2, 3, 5)
                .append(ItemId.MUSHROOM_SPORE, 1, 4, 6)
                .append(ItemId.BELLADONNA_SEED, 1, 4, 6);
    }

    public static Item roll() {
        return table.rollItem();
    }
}
