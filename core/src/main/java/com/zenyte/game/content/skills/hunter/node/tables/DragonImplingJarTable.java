package com.zenyte.game.content.skills.hunter.node.tables;

import com.zenyte.game.content.drops.table.DropTable;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import mgi.types.config.items.ItemDefinitions;

/**
 * @author Corey
 * @see <a href=https://oldschool.runescape.wiki/w/Dragon_impling_jar>Dragon impling jar</a>
 * @since 11/01/2019
 */
public class DragonImplingJarTable {
    
    public static final DropTable table = new DropTable();
    
    static {
        table.append(ItemId.DRAGONSTONE_BOLT_TIPS, 1, 10, 30)
                .append(ItemId.DRAGONSTONE_BOLT_TIPS, 1, 36)
                .append(ItemId.MYSTIC_ROBE_BOTTOM)
                .append(ItemId.GRANITE_BODY)
                .append(ItemDefinitions.getOrThrow(ItemId.AMULET_OF_GLORY).getNotedId(), 1, 3)
                .append(ItemDefinitions.getOrThrow(ItemId.DRAGONSTONE_AMULET).getNotedId(), 1, 2)
                .append(ItemId.DRAGON_ARROW, 1, 100, 250)
                .append(ItemId.DRAGONSTONE_BOLTS, 1, 10, 40)
                .append(ItemId.DRAGON_LONGSWORD)
                .append(ItemDefinitions.getOrThrow(ItemId.DRAGON_DAGGERP_5698).getNotedId(), 1, 3)
                .append(ItemId.DRAGON_DART, 1, 100, 250)
                .append(ItemDefinitions.getOrThrow(ItemId.DRAGONSTONE).getNotedId(), 1, 3)
                .append(ItemId.DRAGON_DART_TIP, 1, 100, 350)
                .append(ItemId.DRAGON_ARROWTIPS, 1, 100, 350)
                .append(ItemId.DRAGON_JAVELIN_HEADS, 1, 25, 35)
                .append(ItemDefinitions.getOrThrow(ItemId.BABYDRAGON_BONES).getNotedId(), 1, 100, 350)
                .append(ItemDefinitions.getOrThrow(ItemId.DRAGON_BONES).getNotedId(), 1, 50, 100)
                .append(ItemId.MAGIC_SEED)
                .append(ItemId.SNAPDRAGON_SEED, 1, 6)
                .append(ItemDefinitions.getOrThrow(ItemId.SUMMER_PIE).getNotedId(), 1, 15);
    }
    
    public static Item roll() {
        return table.rollItem();
    }
    
}
