package com.zenyte.game.content.drops;

import com.zenyte.game.content.drops.table.DropTable;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;

/**
 * @author Corey
 * @see <a href=https://oldschool.runescape.wiki/w/Drop_table#Herb_drop_table>Herb drop table</a>
 * @since 24/11/2019
 */
public class HerbTable {
    
    public static final DropTable table = new DropTable();
    
    static {
        table.append(ItemId.GRIMY_GUAM_LEAF, 32)
                .append(ItemId.GRIMY_MARRENTILL, 24)
                .append(ItemId.GRIMY_TARROMIN, 18)
                .append(ItemId.GRIMY_HARRALANDER, 14)
                .append(ItemId.GRIMY_RANARR_WEED, 11)
                .append(ItemId.GRIMY_IRIT_LEAF, 8)
                .append(ItemId.GRIMY_AVANTOE, 6)
                .append(ItemId.GRIMY_KWUARM, 5)
                .append(ItemId.GRIMY_CADANTINE, 4)
                .append(ItemId.GRIMY_LANTADYME, 3)
                .append(ItemId.GRIMY_DWARF_WEED, 3);
    }
    
    public static Item roll() {
        return table.rollItem();
    }
    
}
