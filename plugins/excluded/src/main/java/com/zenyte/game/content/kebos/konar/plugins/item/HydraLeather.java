package com.zenyte.game.content.kebos.konar.plugins.item;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Tommeh | 24/10/2019 | 23:45
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class HydraLeather extends ItemPlugin {

    @Override
    public void handle() {
        bind("Inspect", (player, item, slotId) -> player.getDialogueManager().start(new PlainChat(player, "This leather looks pretty tough to work with... Maybe the dragonkin<br><br> had a way.")));
    }

    @Override
    public int[] getItems() {
        return new int[] { 22983 };
    }
}
