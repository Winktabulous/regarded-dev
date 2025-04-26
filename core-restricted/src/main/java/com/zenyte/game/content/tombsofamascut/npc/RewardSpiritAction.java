package com.zenyte.game.content.tombsofamascut.npc;

import com.zenyte.game.content.tombsofamascut.encounter.RewardEncounter;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.plugins.dialogue.OptionDialogue;

/**
 * @author Savions
 */
public class RewardSpiritAction extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            final OptionDialogue dialogue = new OptionDialogue(player, "Are you ready to leave the Tombs of Amascut?", new String[] {"Yes.", "No."},
                    new Runnable[] {() -> player.getTOAManager().leaveTombs("You leave the Tombs of Amascut."), null});
            player.getDialogueManager().start(dialogue);
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] {RewardEncounter.SPIRIT_ID};
    }
}
