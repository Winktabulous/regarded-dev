package com.zenyte.game.content.skills.hunter.node.tables;

import com.zenyte.game.content.drops.table.DropTable;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import mgi.types.config.items.ItemDefinitions;

/**
 * @author Corey
 * @see <a href=https://oldschool.runescape.wiki/w/Essence_impling_jar>Essence impling jar</a>
 * @since 11/01/2019
 */
public class EssenceImplingJarTable {
    
    public static final DropTable table = new DropTable();
    private static final int COMMON = 50;
    private static final int UNCOMMON = 20;
    private static final int RARE = 4;
    
    static {
        table.append(ItemDefinitions.getOrThrow(ItemId.PURE_ESSENCE).getNotedId(), COMMON, 20, 35)
                .append(ItemId.WATER_RUNE, COMMON, 30)
                .append(ItemId.AIR_RUNE, COMMON, 30)
                .append(ItemId.FIRE_RUNE, COMMON, 50)
                .append(ItemId.MIND_RUNE, COMMON, 25)
                .append(ItemId.BODY_RUNE, COMMON, 28)
                .append(ItemId.CHAOS_RUNE, COMMON, 4)
                .append(ItemId.MIND_TALISMAN, COMMON)
            
                .append(ItemId.LAVA_RUNE, UNCOMMON, 4)
                .append(ItemId.MUD_RUNE, UNCOMMON, 4)
                .append(ItemId.SMOKE_RUNE, UNCOMMON, 4)
                .append(ItemId.STEAM_RUNE, UNCOMMON, 4)
                .append(ItemId.COSMIC_RUNE, UNCOMMON, 4)
            
                .append(ItemId.DEATH_RUNE, RARE, 13)
                .append(ItemId.LAW_RUNE, RARE, 13)
                .append(ItemId.BLOOD_RUNE, RARE, 7)
                .append(ItemId.SOUL_RUNE, RARE, 11)
                .append(ItemId.NATURE_RUNE, RARE, 13);
    }
    
    public static Item roll() {
        return table.rollItem();
    }
    
}
