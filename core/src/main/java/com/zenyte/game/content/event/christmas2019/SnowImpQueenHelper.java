package com.zenyte.game.content.event.christmas2019;

import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;

public class SnowImpQueenHelper extends NPCPlugin {
    
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            if (AChristmasWarble.hasCompleted(player)) {
                player.getDialogueManager().start(new Dialogue(player, npc) {
                    @Override
                    public void buildDialogue() {
                        npc("Thanks for helping us " + player.getName() + "!", Expression.HIGH_REV_HAPPY);
                    }
                });
            } else if (AChristmasWarble.getProgress(player) == AChristmasWarble.ChristmasWarbleProgress.FROZEN_GUESTS) {
                player.getDialogueManager().start(new Dialogue(player, npc) {
                    @Override
                    public void buildDialogue() {
                        npc("De oomans are on fire! Save 'em.", Expression.HIGH_REV_NORMAL);
                        npc(ChristmasUtils.getImpName(player), "We're on it, matey.", Expression.HIGH_REV_NORMAL);
                    }
                });
            } else {
                player.getDialogueManager().start(new Dialogue(player, npc) {
                    @Override
                    public void buildDialogue() {
                        npc("Please help us save Christmas!", Expression.HIGH_REV_SAD);
                    }
                });
            }
    
        });
    }
    
    @Override
    public int[] getNPCs() {
        return new int[]{ChristmasConstants.SNOW_IMP_HELPER};
    }
}
