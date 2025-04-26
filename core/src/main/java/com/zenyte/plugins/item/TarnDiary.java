package com.zenyte.plugins.item;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 20/06/2019 22:49
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TarnDiary extends ItemPlugin {
    @Override
    public void handle() {
        bind("Read", (player, item, container, slotId) -> player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                item(item, "The book reads a paragraph about a salve amulet. Perhaps you could enchant a salve amulet with it?");
            }
        }));
    }

    @Override
    public int[] getItems() {
        return new int[] {
                10587
        };
    }
}
