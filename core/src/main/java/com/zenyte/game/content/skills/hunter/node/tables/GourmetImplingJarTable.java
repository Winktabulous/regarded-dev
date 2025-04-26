package com.zenyte.game.content.skills.hunter.node.tables;

import com.zenyte.game.content.drops.table.DropTable;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import mgi.types.config.items.ItemDefinitions;

/**
 * @author Corey
 * @see <a href=https://oldschool.runescape.wiki/w/Gourmet_impling_jar>Gourmet impling jar</a>
 * @since 11/01/2019
 */
public class GourmetImplingJarTable {
    
    public static final DropTable table = new DropTable();
    
    static {
        table.append(ItemId.TUNA, 20)
                .append(ItemId.BASS, 10)
                .append(ItemId.CURRY, 10)
                .append(ItemId.MEAT_PIE, 10)
                .append(ItemId.CHOCOLATE_CAKE, 10)
                .append(ItemId.FROG_SPAWN, 10)
                .append(ItemId.SPICE, 10)
                .append(ItemId.CURRY_LEAF, 10)
        
                .append(ItemId.UGTHANKI_KEBAB, 1)
                .append(ItemDefinitions.getOrThrow(ItemId.LOBSTER).getNotedId(), 1, 4)
                .append(ItemDefinitions.getOrThrow(ItemId.SHARK).getNotedId(), 1, 3)
                .append(ItemId.FISH_PIE)
                .append(ItemId.CHEFS_DELIGHT)
                .append(ItemDefinitions.getOrThrow(ItemId.RAINBOW_FISH).getNotedId(), 1, 5)
                .append(ItemDefinitions.getOrThrow(ItemId.GARDEN_PIE).getNotedId(), 1, 6)
                .append(ItemDefinitions.getOrThrow(ItemId.SWORDFISH).getNotedId(), 1, 3)
                .append(ItemId.STRAWBERRIES5)
                .append(ItemDefinitions.getOrThrow(ItemId.COOKED_KARAMBWAN).getNotedId(), 1, 2);
        
        // TODO grubby key
    }
    
    public static Item roll() {
        return table.rollItem();
    }
    
}
