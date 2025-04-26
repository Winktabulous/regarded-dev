package com.zenyte.game.content.skills.hunter.node.tables;

import com.zenyte.game.content.drops.table.DropTable;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import mgi.types.config.items.ItemDefinitions;

/**
 * @author Corey
 * @see <a href=https://oldschool.runescape.wiki/w/Magpie_impling_jar>Magpie impling jar</a>
 * @since 11/01/2019
 */
public class MagpieImplingJarTable {
    
    public static final DropTable table = new DropTable();
    
    static {
        table.append(ItemDefinitions.getOrThrow(ItemId.BLACK_DRAGONHIDE).getNotedId(), 10, 6)
                .append(ItemDefinitions.getOrThrow(ItemId.DIAMOND_AMULET).getNotedId(), 5, 3)
                .append(ItemDefinitions.getOrThrow(ItemId.AMULET_OF_POWER).getNotedId(), 5, 3)
                .append(ItemDefinitions.getOrThrow(ItemId.RING_OF_FORGING).getNotedId(), 5, 3)
                .append(ItemId.SPLITBARK_GAUNTLETS, 5)
                .append(ItemId.MYSTIC_BOOTS, 5)
                .append(ItemId.MYSTIC_GLOVES, 5)
                .append(ItemId.RUNE_WARHAMMER, 5)
                .append(ItemDefinitions.getOrThrow(ItemId.RING_OF_LIFE).getNotedId(), 5, 4)
                .append(ItemId.RUNE_SQ_SHIELD, 5)
                .append(ItemId.DRAGON_DAGGER, 5)
                .append(ItemId.NATURE_TIARA, 5)
                .append(ItemDefinitions.getOrThrow(ItemId.RUNITE_BAR).getNotedId(), 5, 2)
                .append(ItemDefinitions.getOrThrow(ItemId.DIAMOND).getNotedId(), 5, 4)
                .append(ItemId.PINEAPPLE_SEED, 5)
                .append(ItemId.LOOP_HALF_OF_KEY, 5)
                .append(ItemId.TOOTH_HALF_OF_KEY, 5)
                .append(ItemId.SNAPDRAGON_SEED, 5)
                .append(ItemId.GRANITE_BODY, 5)
                .append(ItemId.SINISTER_KEY, 5);
    }
    
    public static Item roll() {
        return table.rollItem();
    }
    
}
