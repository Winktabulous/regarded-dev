package com.zenyte.game.content.drops;

import com.zenyte.game.content.drops.table.DropTable;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;

/**
 * @author Corey
 * @see <a href=https://oldschool.runescape.wiki/w/Drop_table#Rare_seed_drop_table>Rare seed drop table</a>
 * @since 24/11/2019
 */
public class RareSeedTable {
    
    public static final DropTable table = new DropTable();
    
    static {
        table.append(ItemId.TOADFLAX_SEED, 47)
                .append(ItemId.IRIT_SEED, 32)
                .append(ItemId.BELLADONNA_SEED, 31)
                .append(ItemId.AVANTOE_SEED, 22)
                .append(ItemId.POISON_IVY_SEED, 22)
                .append(ItemId.CACTUS_SEED, 21)
                .append(ItemId.KWUARM_SEED, 15)
                .append(ItemId.POTATO_CACTUS_SEED, 15)
                .append(ItemId.SNAPDRAGON_SEED, 10)
                .append(ItemId.CADANTINE_SEED, 7)
                .append(ItemId.LANTADYME_SEED, 5)
                .append(ItemId.SNAPE_GRASS_SEED, 4)
                .append(ItemId.DWARF_WEED_SEED, 3)
                .append(ItemId.TORSTOL_SEED, 2);
    }
    
    public static Item roll() {
        return table.rollItem();
    }
    
}
