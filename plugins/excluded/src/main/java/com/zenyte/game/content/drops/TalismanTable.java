package com.zenyte.game.content.drops;

import com.zenyte.game.content.drops.table.DropTable;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;

/**
 * @author Corey
 * @see <a href=https://oldschool.runescape.wiki/w/Drop_table#Talisman_drop_table>Talisman drop table</a>
 * @since 24/11/2019
 */
public class TalismanTable {
    
    public static final DropTable table = new DropTable();
    
    static {
        table.append(ItemId.AIR_TALISMAN, 10)
                .append(ItemId.BODY_TALISMAN, 10)
                .append(ItemId.EARTH_TALISMAN, 10)
                .append(ItemId.FIRE_TALISMAN, 10)
                .append(ItemId.MIND_TALISMAN, 10)
                .append(ItemId.WATER_TALISMAN, 10)
                .append(ItemId.COSMIC_TALISMAN, 4)
                .append(ItemId.CHAOS_TALISMAN, 3)
                .append(ItemId.NATURE_TALISMAN, 3);
    }
    
    public static Item roll() {
        return table.rollItem();
    }
    
}
