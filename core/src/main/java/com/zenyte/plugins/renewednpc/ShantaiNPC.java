package com.zenyte.plugins.renewednpc;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 26/03/2019 14:31
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ShantaiNPC extends NPCPlugin {

    @Override
    public void handle() {
        bind("Buy-pass", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                final int space = player.getInventory().getFreeSlots();
                final int count = player.getInventory().getAmountOf(1854);
                if (space > 0 || count > 0 && count < Integer.MAX_VALUE) {
                    if (!player.getInventory().containsItem(995, 5)) {
                        npc("You'll need 5 coins to buy the pass!");
                        return;
                    }
                    player.getInventory().deleteItem(995, 5);
                    player.getInventory().addItem(new Item(1854, 1));
                    npc("Pleasure doing business with you!");
                    return;
                }
                npc("You'll need some more free space if you wish to purchase that.");
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.SHANTAY };
    }
}
