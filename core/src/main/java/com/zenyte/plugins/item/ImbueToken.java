package com.zenyte.plugins.item;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.plugins.dialogue.ItemChat;

/**
 * @author Tommeh | 24-2-2019 | 20:24
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ImbueToken extends ItemPlugin {

    @Override
    public void handle() {
        bind("Read", (player, item, slotId) -> player.getDialogueManager().start(new ItemChat(player, item, "I suspect this can be used to upgrade certain items.")));
    }

    @Override
    public int[] getItems() {
        return new int[] { 11681 };
    }
}
