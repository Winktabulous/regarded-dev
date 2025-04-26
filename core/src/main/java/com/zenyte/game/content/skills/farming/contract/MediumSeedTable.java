package com.zenyte.game.content.skills.farming.contract;

import com.zenyte.game.content.drops.table.DropTable;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;

/**
 * @author Christopher
 * @since 4/7/2020
 */
public class MediumSeedTable {
    public static final DropTable table = new DropTable();

    static {
        table.append(ItemId.IRIT_SEED, 3, 2, 6)
                .append(ItemId.HESPORI_SEED, 3, 1)
                .append(ItemId.LIMPWURT_SEED, 3, 4, 8)
                .append(ItemId.WATERMELON_SEED, 2, 8, 12)
                .append(ItemId.SNAPE_GRASS_SEED, 2, 6, 8)
                .append(ItemId.WILDBLOOD_SEED, 2, 8, 12)
                .append(ItemId.WHITEBERRY_SEED, 2, 6, 8)
                .append(ItemId.POISON_IVY_SEED, 2, 6, 8)
                .append(ItemId.CACTUS_SEED, 2, 2, 6)
                .append(ItemId.POTATO_CACTUS_SEED, 2, 2, 6)
                .append(ItemId.WILLOW_SEED, 1, 2, 4)
                .append(ItemId.PINEAPPLE_SEED, 1, 3, 5)
                .append(ItemId.TOADFLAX_SEED, 1, 1, 3)
                .append(ItemId.AVANTOE_SEED, 1, 1, 3)
                .append(ItemId.KWUARM_SEED, 1, 1, 3)
                .append(ItemId.CADANTINE_SEED, 1, 1, 3)
                .append(ItemId.LANTADYME_SEED, 1, 1, 3)
                .append(ItemId.DWARF_WEED_SEED, 1, 1, 3)
                .append(ItemId.CALQUAT_TREE_SEED, 1, 3, 6)
                .append(ItemId.TEAK_SEED, 1, 1, 3);
    }

    public static Item roll() {
        return table.rollItem();
    }
}
