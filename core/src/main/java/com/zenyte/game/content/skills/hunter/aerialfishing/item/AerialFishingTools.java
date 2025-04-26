package com.zenyte.game.content.skills.hunter.aerialfishing.item;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Cresinkel
 */

public class AerialFishingTools {
    public static boolean hasBait(Player player) {
        if (player.getInventory().containsAnyOf(ItemId.FISH_CHUNKS, ItemId.KING_WORM)) {
            return true;
        }
        return false;
    }

    public static boolean hasBird(Player player) {
        if (player.getEquipment().containsItem(ItemId.CORMORANTS_GLOVE_22817) || player.getEquipment().containsItem(ItemId.CORMORANTS_GLOVE)) {
            return true;
        }
        return false;
    }
}
