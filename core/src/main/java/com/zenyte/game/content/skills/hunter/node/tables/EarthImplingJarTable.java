package com.zenyte.game.content.skills.hunter.node.tables;

import com.zenyte.game.content.drops.table.DropTable;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import mgi.types.config.items.ItemDefinitions;

/**
 * @author Corey
 * @see <a href=https://oldschool.runescape.wiki/w/Earth_impling_jar>Earth impling jar</a>
 * @since 11/01/2019
 */
public class EarthImplingJarTable {
    
    public static final DropTable table = new DropTable();
    private static final int COMMON = 50;
    private static final int UNCOMMON = 20;
    private static final int RARE = 4;

    static {
        table.append(ItemId.EARTH_TALISMAN, COMMON)
                .append(ItemId.EARTH_TIARA, COMMON)
                .append(ItemId.EARTH_RUNE, COMMON, 32)
                .append(ItemDefinitions.getOrThrow(ItemId.MITHRIL_ORE).getNotedId(), COMMON, 1, 3)
                .append(ItemId.UNICORN_HORN, COMMON)
                .append(ItemId.STEEL_BAR, COMMON)
                .append(ItemId.MITHRIL_PICKAXE, COMMON)
                .append(ItemId.WILDBLOOD_SEED, COMMON, 2)
                .append(ItemId.JANGERBERRY_SEED, COMMON, 2)
                .append(ItemDefinitions.getOrThrow(ItemId.COMPOST).getNotedId(), COMMON, 6)
        
                .append(ItemDefinitions.getOrThrow(ItemId.SUPERCOMPOST).getNotedId(), UNCOMMON, 2)
                .append(ItemDefinitions.getOrThrow(ItemId.BUCKET_OF_SAND).getNotedId(), UNCOMMON, 4)
                .append(ItemId.HARRALANDER_SEED, UNCOMMON, 2)
                .append(ItemDefinitions.getOrThrow(ItemId.COAL).getNotedId(), UNCOMMON, 2)
        
                .append(ItemId.GOLD_ORE, RARE)
                .append(ItemDefinitions.getOrThrow(ItemId.UNCUT_EMERALD).getNotedId(), RARE, 2)
                .append(ItemDefinitions.getOrThrow(ItemId.EMERALD).getNotedId(), RARE, 2)
                .append(ItemId.RUBY, RARE);
    }
    
    public static Item roll() {
        return table.rollItem();
    }
    
}
