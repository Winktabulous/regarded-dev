package com.zenyte.plugins.itemonplayer;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnPlayerPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 15/06/2019 10:20
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DonatorScrollItemOnPlayerPlugin implements ItemOnPlayerPlugin {
    @Override
    public void handleItemOnPlayerAction(final Player giver, final Item item, final int slot, final Player target) {
        giver.getDialogueManager().start(new Dialogue(giver) {
            @Override
            public void buildDialogue() {
                item(item, "Offer " + target.getName() + " the scroll? You won\'t be able to reclaim it.");
                options(TITLE, new DialogueOption("Offer them the scroll.", () -> {
                    if (player.getInventory().getItem(slot) != item) {
                        return;
                    }
                    target.getDialogueManager().start(new Dialogue(target) {
                        @Override
                        public void buildDialogue() {
                            item(item, giver.getName() + " offers you a scroll! Would you like to accept it?");
                            options(TITLE, new DialogueOption("Accept the scroll.", () -> {
                                if (isUnavailable(giver) || isUnavailable(target)) {
                                    player.sendMessage("Unable to process request.");
                                    return;
                                }
                                if (!target.getInventory().hasFreeSlots()) {
                                    target.sendMessage("You need some free inventory space to accept the scroll.");
                                    return;
                                }
                                if (giver.getInventory().getItem(slot) != item) {
                                    target.sendMessage("The other player has cancelled the offer.");
                                    return;
                                }
                                final int result = giver.getInventory().deleteItem(item).getSucceededAmount();
                                if (result > 0) {
                                    target.getInventory().addOrDrop(item);
                                }
                            }), new DialogueOption("Cancel the scroll."));
                        }
                    });
                }), new DialogueOption("Cancel."));
            }
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {13190, 30017, 30018};
    }
}
