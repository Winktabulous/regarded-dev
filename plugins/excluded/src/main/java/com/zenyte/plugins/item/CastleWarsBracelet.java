package com.zenyte.plugins.item;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

/**
 * @author Kris | 15/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CastleWarsBracelet extends ItemPlugin {
    @Override
    public void handle() {
        bind("Check", (player, item, container, slotId) -> {
            final int charges = item.getId() == ItemId.CASTLE_WARS_BRACELET1 ? 1 : item.getId() == ItemId.CASTLE_WARS_BRACELET2 ? 2 : 3;
            final String message = "Your Castle wars bracelet has " + charges + " charge" + (charges == 1 ? "" : "s") + " remaining.";
            player.sendMessage(message + (player.inArea("Castle Wars") ? (" The effect is " + (player.getTemporaryAttributes().containsKey("castle wars bracelet effect") ? "active" : "inactive") + " for the duration of this game.") : ""));
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {ItemId.CASTLE_WARS_BRACELET1, ItemId.CASTLE_WARS_BRACELET2, ItemId.CASTLE_WARS_BRACELET3};
    }
}
