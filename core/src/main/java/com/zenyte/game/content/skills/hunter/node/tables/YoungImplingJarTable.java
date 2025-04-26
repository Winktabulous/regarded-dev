package com.zenyte.game.content.skills.hunter.node.tables;

import com.zenyte.game.content.drops.table.DropTable;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;

/**
 * @author Corey
 * @see <a href=https://oldschool.runescape.wiki/w/Young_impling_jar>Young impling jar</a>
 * @since 11/01/2019
 */
public class YoungImplingJarTable {
    
    public static final DropTable table = new DropTable();
    private static final int COMMON = 30;
    private static final int UNCOMMON = 10;
    private static final int RARE = 4;
    
    static {
        table.append(ItemId.STEEL_NAILS, COMMON, 5)
                .append(ItemId.LOCKPICK, COMMON)
                .append(ItemId.PURE_ESSENCE, COMMON)
                .append(ItemId.TUNA, COMMON)
                .append(ItemId.CHOCOLATE_SLICE, COMMON)
                
                .append(ItemId.STEEL_AXE, UNCOMMON)
                .append(ItemId.MEAT_PIZZA, UNCOMMON)
                .append(ItemId.GARDEN_PIE, UNCOMMON)
                .append(ItemId.JANGERBERRIES, UNCOMMON)
                .append(ItemId.COAL, UNCOMMON)
                .append(ItemId.BOW_STRING, UNCOMMON)
                .append(ItemId.SNAPE_GRASS, UNCOMMON)
                .append(ItemId.SOFT_CLAY, UNCOMMON)
                
                .append(ItemId.STUDDED_CHAPS, RARE)
                .append(ItemId.STEEL_FULL_HELM, RARE)
                .append(ItemId.OAK_PLANK, RARE)
                .append(ItemId.DEFENCE_POTION3, RARE)
                .append(ItemId.MITHRIL_BAR, RARE)
                .append(ItemId.YEW_LONGBOW, RARE);
    }
    
    public static Item roll() {
        return table.rollItem();
    }
    
}
