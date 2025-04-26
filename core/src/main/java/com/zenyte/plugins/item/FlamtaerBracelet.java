package com.zenyte.plugins.item;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 09/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FlamtaerBracelet extends ItemPlugin {
    @Override
    public void handle() {
        bind("Check", (player, item, container, slotId) -> {
            final int uses = player.getNumericAttribute("flamtaer bracelet uses").intValue();
            player.sendMessage("Your flamtaer bracelet has " + (80 - uses) + " charge" + (uses == 79 ? "" : "s") + " left.");
        });
        bind("Break", (player, item, container, slotId) -> {
            player.getInventory().deleteItem(slotId, item);
            player.addAttribute("flamtaer bracelet uses", 0);
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    item(item, "You destroy the flamtaer bracelet.");
                }
            });
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {21180};
    }
}
