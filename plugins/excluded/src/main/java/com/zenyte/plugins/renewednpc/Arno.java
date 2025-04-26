package com.zenyte.plugins.renewednpc;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.ItemRetrievalService;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 17/06/2019 15:11
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Arno extends NPCPlugin {
    @Override
    public void handle() {
        bind("Collect", (player, npc) -> {
            if (player.getRetrievalService().getType() != ItemRetrievalService.RetrievalServiceType.ARNO) {
                player.getDialogueManager().start(new Dialogue(player, npc) {
                    @Override
                    public void buildDialogue() {
                      npc("You have no items waiting with me.");
                    }
                });
                return;
            }
            GameInterface.ITEM_RETRIEVAL_SERVICE.open(player);
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] {
                8588
        };
    }
}
