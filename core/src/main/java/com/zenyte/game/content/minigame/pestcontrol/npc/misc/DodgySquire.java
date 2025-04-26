package com.zenyte.game.content.minigame.pestcontrol.npc.misc;

import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 27/11/2018 11:21
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DodgySquire extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                npc("Psst! Got any Void kit you don't want? Spare robes, spare helms, that kind of thing?");
                options(TITLE, new DialogueOption("What if I do?", () -> setKey(10)), new DialogueOption("No, I don't.", () -> setKey(20)));
                player(10, "What if I do?");
                npc("Might be able to take it off your hands for you. Show me what you've got, and I'll see if I can get you some Commendation Points for it.");
                player(20, "No I don't.");
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.DODGY_SQUIRE };
    }
}
