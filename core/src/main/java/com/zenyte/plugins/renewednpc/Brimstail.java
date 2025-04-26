package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 26/11/2018 19:34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Brimstail extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            if (TreasureTrail.talk(player, npc)) {
                return;
            }
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    npc("Hello adventurer, what can I do for you?");
                    player("Can you teleport me to the Rune Essence?");
                    npc("Okay. Hold onto your hat!").executeAction(() -> Aubury.teleport(player, npc));
                }
            });
        });
        bind("Teleport", Aubury::teleport);
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.BRIMSTAIL_11431 };
    }
}
