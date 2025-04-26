package com.zenyte.plugins.itemonnpc;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.model.item.degradableitems.DegradableItem;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectId;

/**
 * @author Kris | 18/06/2019 15:02
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class IbansStaffOnEdalf implements ItemOnNPCAction {

    @Override
    public void handleItemOnNPCAction(final Player player, final Item item, final int slot, final NPC npc) {
        player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                npc("Would you like me to repair your Iban's staff for you? It'll cost you 100,000 coins to do so.");
                options(TITLE, new DialogueOption("Repair the staff.", () -> {
                    if (player.getInventory().getItem(slot) != item) {
                        return;
                    }
                    if (!player.getInventory().containsItem(995, 100000)) {
                        player.sendMessage("You need at least 100,000 coins to do this.");
                        return;
                    }
                    player.getInventory().deleteItem(995, 100000);
                    player.getInventory().deleteItem(item);
                    player.getInventory().addOrDrop(new Item(12658, 1, DegradableItem.getFullCharges(12658)));
                }), new DialogueOption("Cancel."));
            }
        });
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 1410 };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LIGHT_DOOR_10006 };
    }
}
