package com.zenyte.plugins.renewednpc;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public class CosmicBeing extends NPCPlugin {

    private static final String[] messages = new String[] { "Where do flies go at night?", "Have you ever flicked through a dictionary and afterwards felt really depressed by how much you don't know?", "Do other birds speak pigeon pidgin?", "Political philosophy is an oxymoron.", "Why are yawns contagious?", "Do sick days include when you're sick of work?", "To be is to be the value of a variable.", "How do you know when you've run out of invisible ink?", "Where does a snake's tail begin?", "How long do fish wait to swim after they eat?", "If you're halfway from here to there, which is which?" };

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                player("What universal mystery are you pondering today?");
                npc(Utils.getRandomElement(messages));
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.COSMIC_BEING };
    }
}
