package com.zenyte.game.content.skills.hunter.node.tables;

import com.zenyte.game.content.drops.table.DropTable;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import mgi.types.config.items.ItemDefinitions;

/**
 * @author Corey
 * @see <a href=https://oldschool.runescape.wiki/w/Nature_impling_jar>Nature impling jar</a>
 * @since 11/01/2019
 */
public class NatureImplingJarTable {
    
    public static final DropTable table = new DropTable();
    
    static {
        table.append(ItemId.LIMPWURT_SEED, 10)
                .append(ItemId.JANGERBERRY_SEED, 10)
                .append(ItemId.BELLADONNA_SEED, 10)
                .append(ItemId.HARRALANDER_SEED, 10)
                .append(ItemId.CACTUS_SPINE, 10)
                .append(ItemId.MAGIC_LOGS, 10)
                .append(ItemDefinitions.getOrThrow(ItemId.TARROMIN).getNotedId(), 10, 4)
                .append(ItemId.COCONUT, 10)
                .append(ItemId.IRIT_SEED, 10)
        
                .append(ItemId.CURRY_TREE_SEED, 1)
                .append(ItemId.ORANGE_TREE_SEED, 1)
                .append(ItemId.SNAPDRAGON, 1)
                .append(ItemId.KWUARM_SEED, 1)
                .append(ItemId.AVANTOE_SEED, 1, 5)
                .append(ItemId.WILLOW_SEED, 1)
                .append(ItemId.TORSTOL_SEED, 1)
                .append(ItemId.RANARR_SEED, 1)
                .append(ItemDefinitions.getOrThrow(ItemId.TORSTOL).getNotedId(), 1, 2)
                .append(ItemId.DWARF_WEED_SEED, 1);
    }
    
    public static Item roll() {
        return table.rollItem();
    }
    
}
