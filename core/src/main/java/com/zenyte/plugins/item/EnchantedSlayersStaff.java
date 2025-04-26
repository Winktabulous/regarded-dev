package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.ItemChat;

/**
 * @author Kris | 10/05/2019 23:17
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EnchantedSlayersStaff extends ItemPlugin {
    @Override
    public void handle() {
        bind("Revert", (player, item, container, slotId) -> {
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    item(item, "Reverting the staff does not refund the enchantment scroll back. Are you sure you wish to continue?");
                    options("Revert the staff?", new DialogueOption("Revert it.", () -> {
                        if (player.getInventory().getItem(slotId) != item) {
                            return;
                        }
                        player.getInventory().set(slotId, new Item(4170));
                        player.getDialogueManager().finish();
                        player.getDialogueManager().start(new ItemChat(player, new Item(4170), "You revert your enchanted staff back to a normal slayer's staff."));
                    }), new DialogueOption("No."));
                }
            });
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {
                21255
        };
    }
}
