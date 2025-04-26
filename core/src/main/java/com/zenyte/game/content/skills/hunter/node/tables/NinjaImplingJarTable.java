package com.zenyte.game.content.skills.hunter.node.tables;

import com.zenyte.game.content.drops.table.DropTable;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import mgi.types.config.items.ItemDefinitions;

/**
 * @author Corey
 * @see <a href=https://oldschool.runescape.wiki/w/Ninja_impling_jar>Ninja impling jar</a>
 * @since 11/01/2019
 */
public class NinjaImplingJarTable {
    
    public static final DropTable table = new DropTable();
    
    static {
        table.append(ItemId.SNAKESKIN_BOOTS)
                .append(ItemId.GRANITE_BODY)
                .append(ItemId.SPLITBARK_HELM)
                .append(ItemId.MYSTIC_BOOTS)
                .append(ItemId.RUNE_CHAINBODY)
                .append(ItemId.MYSTIC_GLOVES)
                .append(ItemId.OPAL_MACHETE)
                .append(ItemId.RUNE_CLAWS)
                .append(ItemId.RUNE_SCIMITAR)
                .append(ItemId.DRAGON_DAGGERP_5680)
                .append(ItemId.RUNE_ARROW, 1, 70)
                .append(ItemId.RUNE_DART, 1, 70)
                .append(ItemId.RUNE_KNIFE, 1, 40)
                .append(ItemId.RUNE_THROWNAXE, 1, 50)
                .append(ItemId.ONYX_BOLTS, 1, 2)
                .append(ItemId.ONYX_BOLT_TIPS, 1, 4)
                .append(ItemDefinitions.getOrThrow(ItemId.BLACK_DRAGONHIDE).getNotedId(), 1, 10)
                .append(ItemDefinitions.getOrThrow(ItemId.PRAYER_POTION3).getNotedId(), 1, 4)
                .append(ItemDefinitions.getOrThrow(ItemId.WEAPON_POISON_5937).getNotedId(), 1, 4)
                .append(ItemDefinitions.getOrThrow(ItemId.DAGANNOTH_HIDE).getNotedId(), 1, 3);
    }
    
    public static Item roll() {
        return table.rollItem();
    }
    
}
