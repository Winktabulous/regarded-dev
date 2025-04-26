package com.zenyte.game.content.skills.hunter.node.tables;

import com.zenyte.game.content.drops.table.DropTable;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;

/**
 * @author Corey
 * @see <a href=https://oldschool.runescape.wiki/w/Baby_impling_jar>Baby impling jar</a>
 * @since 10/01/2019
 */
public class BabyImplingJarTable {
    
    public static final DropTable table = new DropTable();
    private static final int COMMON = 30;
    private static final int UNCOMMON = 10;
    private static final int RARE = 4;
    
    static {
        table.append(ItemId.CHISEL, COMMON)
                .append(ItemId.THREAD, COMMON)
                .append(ItemId.NEEDLE, COMMON)
                .append(ItemId.KNIFE, COMMON)
                .append(ItemId.CHEESE, COMMON)
                .append(ItemId.HAMMER, COMMON)
                .append(ItemId.BALL_OF_WOOL, COMMON)
                
                .append(ItemId.BUCKET_OF_MILK, UNCOMMON)
                .append(ItemId.ANCHOVIES, UNCOMMON)
                .append(ItemId.SPICE, UNCOMMON)
                .append(ItemId.FLAX, UNCOMMON)
                .append(ItemId.MUD_PIE, UNCOMMON)
                .append(ItemId.SEAWEED, UNCOMMON)
                .append(ItemId.AIR_TALISMAN, UNCOMMON)
                
                .append(ItemId.SILVER_BAR, RARE)
                .append(ItemId.SAPPHIRE, RARE)
                .append(ItemId.HARD_LEATHER, RARE)
                .append(ItemId.LOBSTER, RARE)
                .append(ItemId.SOFT_CLAY, RARE);
    }
    
    public static Item roll() {
        return table.rollItem();
    }
    
}
