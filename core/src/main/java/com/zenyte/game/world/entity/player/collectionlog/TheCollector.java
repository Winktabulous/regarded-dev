package com.zenyte.game.world.entity.player.collectionlog;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 24/03/2019 15:09
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TheCollector extends NPCPlugin {
    @Override
    public void handle() {
        bind("Get log", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                if (!player.getInventory().hasFreeSlots()) {
                    npc("Make some room in your inventory first.");
                    return;
                }
                final Item item = new Item(22711);
                player.getInventory().addItem(item);
                item(item, "The Collector hands you a Collection log.");
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[] {8491};
    }
}
