package com.zenyte.game.content.treasuretrails.npcs;

import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 03/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CeciliaNPC extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                if (!TreasureTrail.containsMusicClue(player)) {
                    npc("Good day, " + player.getName() + ".");
                    return;
                }
                npc("Have you come to play music to me?");
                options(TITLE, new DialogueOption("Yes, I have.", key(10)), new DialogueOption("No, I'm not interested in that.", key(20)));
                player(10, "Yes, I have.");
                npc("Select the song I asked you to play, and speak to me again.");
                player(20, "No, I'm not interested in that.");
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.CECILIA };
    }
}
