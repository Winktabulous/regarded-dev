package com.zenyte.game.content.drops;

import com.zenyte.game.content.drops.table.DropTable;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;

/**
 * @author Corey
 * @see <a href=https://oldschool.runescape.wiki/w/Drop_table#Common_seed_drop_table>Common seed drop table</a>
 * @since 24/11/2019
 */
public class CommonSeedTable {
    
    private static final DropTable table = new DropTable();
    
    static {
        table.append(ItemId.LIMPWURT_SEED, 137)
                .append(ItemId.STRAWBERRY_SEED, 131)
                .append(ItemId.MARRENTILL_SEED, 125)
                .append(ItemId.JANGERBERRY_SEED, 92)
                .append(ItemId.TARROMIN_SEED, 85)
                .append(ItemId.WILDBLOOD_SEED, 83)
                .append(ItemId.WATERMELON_SEED, 63)
                .append(ItemId.HARRALANDER_SEED, 56)
                .append(ItemId.SNAPE_GRASS_SEED, 40)
                .append(ItemId.RANARR_SEED, 39)
                .append(ItemId.WHITEBERRY_SEED, 34)
                .append(ItemId.MUSHROOM_SPORE, 29)
                .append(ItemId.TOADFLAX_SEED, 27)
                .append(ItemId.BELLADONNA_SEED, 18)
                .append(ItemId.IRIT_SEED, 18)
                .append(ItemId.POISON_IVY_SEED, 13)
                .append(ItemId.AVANTOE_SEED, 12)
                .append(ItemId.CACTUS_SEED, 12)
                .append(ItemId.KWUARM_SEED, 9)
                .append(ItemId.POTATO_CACTUS_SEED, 8)
                .append(ItemId.SNAPDRAGON_SEED, 5)
                .append(ItemId.CADANTINE_SEED, 4)
                .append(ItemId.LANTADYME_SEED, 3)
                .append(ItemId.DWARF_WEED_SEED, 2)
                .append(ItemId.TORSTOL_SEED, 1);
    }
    
    public static Item roll() {
        return table.rollItem();
    }
    
}
