package com.zenyte.plugins.item;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.plugins.dialogue.ItemChat;

/**
 * @author Tommeh | 31-1-2019 | 14:46
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class TourmalineCore extends ItemPlugin {

    @Override
    public void handle() {
        bind("Inspect", (player, item, slotId) -> player.getDialogueManager().start(new ItemChat(player, item, "Fallen from the centre of a Grotesque Guardian. This could be attached to a pair of Bandos boots.")));
    }

    @Override
    public int[] getItems() {
        return new int[] { 21730 };
    }
}
