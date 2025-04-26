package com.zenyte.plugins.item;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.plugins.dialogue.skills.CelastrusBarkFletchingD;

/**
 * @author Tommeh | 28-4-2019 | 16:59
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class CelastrusBark extends ItemPlugin {

    @Override
    public void handle() {
        bind("Fletch", (player, item, slotId) -> {
            player.getDialogueManager().start(new CelastrusBarkFletchingD(player));
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {ItemId.CELASTRUS_BARK };
    }
}
