package com.zenyte.plugins.item;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 18/01/2019 16:30
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@SuppressWarnings("unused")
public class ScytheOfVitur extends ItemPlugin {
    @Override
    public void handle() {
        bind("Wield", (player, item, slotId) -> {
            if ((Boolean) player.getTemporaryAttributes().getOrDefault("TOB_inside", false)) {
                player.getTemporaryAttributes().put("TOB_scythe_equipped", true);
            }
            player.getEquipment().wear(slotId);
        });
        bind("Charge", (player, item, slotId) -> player.sendMessage("Your scythe must be charged using a special " + "vyre well found at Ver Sinhaza."));
        bind("Uncharge", (player, item, slotId) -> {
            if (player.getDuel() != null && player.getDuel().inDuel()) {
                player.sendMessage("You can't do this during a duel.");
                return;
            }
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    plain("You will not be able to re-obtain your vials of blood and blood runes.");
                    options("Uncharge the scythe?<br><col=ff0000>Alternatively you can unload the charges back into the well.", new DialogueOption("Yes.", () -> {
                        //Verify the existence of the item.
                        if (player.getInventory().getItem(slotId) == item) {
                            item.setCharges(0);
                            item.setId(ItemId.SCYTHE_OF_VITUR_UNCHARGED);
                            player.getInventory().refresh(slotId);
                            player.sendMessage("You uncharge your scythe.");
                        }
                    }), new DialogueOption("No."));
                }
            });
        });
    }

    @Override
    public int[] getItems() {
        return new int[] { ItemId.SCYTHE_OF_VITUR, ItemId.SCYTHE_OF_VITUR_UNCHARGED };
    }
}
