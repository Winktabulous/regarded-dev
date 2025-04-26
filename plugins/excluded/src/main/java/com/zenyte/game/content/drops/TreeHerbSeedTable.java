package com.zenyte.game.content.drops;

import com.zenyte.game.content.drops.table.DropTable;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;

/**
 * @author Corey
 * @see <a href=https://oldschool.runescape.wiki/w/Drop_table#Tree-herb_seed_drop_table>Tree-herb seed drop table</a>
 * @since 24/11/2019
 */
public class TreeHerbSeedTable {
    
    public static final DropTable table = new DropTable();
    
    static {
        table.append(ItemId.RANARR_SEED, 15)
                .append(ItemId.SNAPDRAGON_SEED, 14)
                .append(ItemId.TORSTOL_SEED, 11)
                .append(ItemId.WATERMELON_SEED, 10, 15)
                .append(ItemId.WILLOW_SEED, 10)
                .append(ItemId.MAHOGANY_SEED, 9)
                .append(ItemId.MAPLE_SEED, 9)
                .append(ItemId.TEAK_SEED, 9)
                .append(ItemId.YEW_SEED, 9)
                .append(ItemId.PAPAYA_TREE_SEED, 7)
                .append(ItemId.MAGIC_SEED, 6)
                .append(ItemId.PALM_TREE_SEED, 5)
                .append(ItemId.SPIRIT_SEED, 4)
                .append(ItemId.DRAGONFRUIT_TREE_SEED, 3)
                .append(ItemId.CELASTRUS_SEED, 2)
                .append(ItemId.REDWOOD_TREE_SEED, 2);
    }
    
    public static Item roll() {
        return table.rollItem();
    }
    
}
