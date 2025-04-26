package com.zenyte.plugins.item;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 09/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class RingOfPursuit extends ItemPlugin {
    @Override
    public void handle() {
        bind("Check", (player, item, container, slotId) -> {
            final int uses = player.getNumericAttribute("ring of pursuit uses").intValue();
            player.sendMessage("Your ring of pursuit has " + (10 - uses) + " charge" + (uses == 9 ? "" : "s") + " left.");
        });
        bind("Break", (player, item, container, slotId) -> {
            player.getInventory().deleteItem(slotId, item);
            player.addAttribute("ring of pursuit uses", 0);
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    item(item, "You destroy the ring of pursuit.");
                }
            });
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {21126};
    }
}
