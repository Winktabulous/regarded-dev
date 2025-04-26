package com.zenyte.game.content.skills.hunter.node.tables;

import com.zenyte.game.content.drops.table.DropTable;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import mgi.types.config.items.ItemDefinitions;

/**
 * @author William
 * @see <a href=https://oldschool.runescape.wiki/w/Crystal_impling_jar>Crystal impling jar</a>
 * @since 07/24/2021
 */
public class CrystalImplingJarTable {

    public static final DropTable table = new DropTable();

    static {
        table.append(ItemDefinitions.getOrThrow(ItemId.AMULET_OF_POWER).getNotedId(), 56, 5, 7)
                .append(ItemId.CRYSTAL_ACORN, 56)
                .append(ItemId.CRYSTAL_SHARD, 56, 5, 10)
                .append(ItemId.DRAGONSTONE_AMULET, 56)
                .append(ItemDefinitions.getOrThrow(ItemId.DRAGONSTONE).getNotedId(), 56, 2)
                .append(ItemId.RUBY_BOLT_TIPS, 56, 50, 125)
                .append(ItemId.ONYX_BOLT_TIPS, 56, 6, 10)
                .append(ItemId.RUNE_ARROWTIPS, 56, 150, 300)
                .append(ItemId.RUNE_ARROW, 56, 400, 750)
                .append(ItemId.RUNE_JAVELIN_HEADS, 56, 20, 60)
                .append(ItemId.RUNE_DART_TIP, 56, 25, 75)
                .append(ItemId.RUNE_DART, 56, 50, 100)
                .append(ItemId.DRAGON_DART_TIP, 56, 10, 15)
                .append(ItemDefinitions.getOrThrow(ItemId.DRAGON_DAGGER).getNotedId(), 56, 2)
                .append(ItemDefinitions.getOrThrow(ItemId.RUNE_SCIMITAR).getNotedId(), 56, 3, 6)
                .append(ItemDefinitions.getOrThrow(ItemId.BABYDRAGON_BONES).getNotedId(), 56)
                .append(ItemId.RANARR_SEED, 56, 3, 8)
                .append(ItemId.YEW_SEED, 56);
    }

    public static Item roll() {
        return table.rollItem();
    }
}
