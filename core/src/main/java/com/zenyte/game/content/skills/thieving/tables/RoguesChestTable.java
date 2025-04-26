package com.zenyte.game.content.skills.thieving.tables;

import com.zenyte.game.content.drops.table.DropTable;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import mgi.types.config.items.ItemDefinitions;

/**
 * @author Corey
 * @since 11/01/2020
 */
public class RoguesChestTable {
    
    private static final int COMMON = 6;
    private static final int UNCOMMON = 4;
    private static final int RARE = 1;
    private static final DropTable table = new DropTable();
    
    static {
        table.append(ItemDefinitions.getOrThrow(ItemId.UNCUT_EMERALD).getNotedId(), COMMON, 5)
                .append(ItemDefinitions.getOrThrow(ItemId.UNCUT_SAPPHIRE).getNotedId(), COMMON, 6)
                .append(ItemId.COINS_995, COMMON, 1000)
                .append(ItemDefinitions.getOrThrow(ItemId.RAW_TUNA).getNotedId(), COMMON, 15)
                .append(ItemDefinitions.getOrThrow(ItemId.ASHES).getNotedId(), COMMON, 25)
                .append(ItemDefinitions.getOrThrow(ItemId.TINDERBOX).getNotedId(), COMMON, 3)
                .append(ItemId.MIND_RUNE, COMMON, 25)
            
                .append(ItemDefinitions.getOrThrow(ItemId.DIAMOND).getNotedId(), UNCOMMON, 1, 3)
                .append(ItemId.CHAOS_RUNE, UNCOMMON, 40)
                .append(ItemId.DEATH_RUNE, UNCOMMON, 30)
                .append(ItemId.FIRE_RUNE, UNCOMMON, 30)
                .append(ItemDefinitions.getOrThrow(ItemId.PIKE).getNotedId(), UNCOMMON, 10)
                .append(ItemDefinitions.getOrThrow(ItemId.COAL).getNotedId(), UNCOMMON, 13)
                .append(ItemDefinitions.getOrThrow(ItemId.IRON_ORE).getNotedId(), UNCOMMON, 10)
            
                .append(ItemDefinitions.getOrThrow(ItemId.SHARK).getNotedId(), RARE, 10)
                .append(ItemDefinitions.getOrThrow(ItemId.DRAGONSTONE).getNotedId(), RARE, 2);
    }
    
    public static Item roll() {
        return table.rollItem();
    }
    
}
