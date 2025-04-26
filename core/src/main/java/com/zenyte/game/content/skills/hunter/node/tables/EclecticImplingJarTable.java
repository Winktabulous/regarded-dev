package com.zenyte.game.content.skills.hunter.node.tables;

import com.zenyte.game.content.drops.table.DropTable;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import mgi.types.config.items.ItemDefinitions;

/**
 * @author Corey
 * @see <a href=https://oldschool.runescape.wiki/w/Eclectic_impling_jar>Eclectic impling jar</a>
 * @since 11/01/2019
 */
public class EclecticImplingJarTable {
    
    public static final DropTable table = new DropTable();
    
    static {
        table.append(ItemId.MITHRIL_PICKAXE, 10)
                .append(ItemId.CURRY_LEAF, 10)
                .append(ItemId.SNAPE_GRASS, 10)
                .append(ItemId.AIR_RUNE, 10, 30, 58)
                .append(ItemDefinitions.getOrThrow(ItemId.OAK_PLANK).getNotedId(), 10, 4)
                .append(ItemId.EMPTY_CANDLE_LANTERN, 10)
                .append(ItemId.GOLD_ORE, 10)
                .append(ItemDefinitions.getOrThrow(ItemId.GOLD_BAR).getNotedId(), 10, 5)
                .append(ItemId.UNICORN_HORN, 10)
        
                .append(ItemId.ADAMANT_KITESHIELD)
                .append(ItemId.BLUE_DHIDE_CHAPS)
                .append(ItemId.RED_SPIKY_VAMBS)
                .append(ItemId.RUNE_DAGGER)
                .append(ItemId.BATTLESTAFF)
                .append(ItemDefinitions.getOrThrow(ItemId.ADAMANTITE_ORE).getNotedId(), 1, 10)
                .append(ItemDefinitions.getOrThrow(ItemId.SLAYERS_RESPITE).getNotedId(), 1, 2)
                .append(ItemId.WILD_PIE)
                .append(ItemId.WATERMELON_SEED, 1, 3)
                .append(ItemId.DIAMOND);
    }
    
    public static Item roll() {
        return table.rollItem();
    }
    
}
