package com.zenyte.game.content.drops;

import com.zenyte.game.content.drops.table.DropTable;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;

/**
 * @author Corey
 * @see <a href=https://oldschool.runescape.wiki/w/Drop_table#Fixed_allotment_seed_drop_table>Fixed allotment seed drop table</a>
 * @since 24/11/2019
 */
public class AllotmentSeedTable {
    
    private static final DropTable table = new DropTable();
    
    static {
        table.append(ItemId.POTATO_SEED, 96, 4)
                .append(ItemId.ONION_SEED, 72, 4)
                .append(ItemId.CABBAGE_SEED, 48, 4)
                .append(ItemId.TOMATO_SEED, 24, 3)
                .append(ItemId.SWEETCORN_SEED, 12, 3)
                .append(ItemId.STRAWBERRY_SEED, 6, 2)
                .append(ItemId.WATERMELON_SEED, 3, 2)
                .append(ItemId.SNAPE_GRASS_SEED, 2, 2);
    }
    
    public static Item roll() {
        return table.rollItem();
    }
    
}
