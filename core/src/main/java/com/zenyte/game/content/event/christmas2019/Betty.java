package com.zenyte.game.content.event.christmas2019;

import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;

/**
 * @author Corey
 * @since 19/12/2019
 */
public class Betty extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            switch (AChristmasWarble.getProgress(player)) {
                case SANTA_FREED:
                case CAN_OPEN_PRESENT:
                case EVENT_COMPLETE:
                    player.getDialogueManager().start(new Dialogue(player, npc) {
                        @Override
                        public void buildDialogue() {
                            npc("Thanks for saving this year's feast!", Expression.HIGH_REV_NORMAL);
                        }
                    });
                    break;
                case FROZEN_GUESTS:
                    player.getDialogueManager().start(new Dialogue(player, npc) {
                        @Override
                        public void buildDialogue() {
                            npc("How thank goodness you've come. That fire is cursed.", Expression.HIGH_REV_SCARED);
                            player("Do you know how to free them from it?");
                            npc("You'll have to reserve the curse. It's crucial that you REVERSE it!", Expression.HIGH_REV_NORMAL);
                        }
                    });
                    break;
                default:
                    player.getDialogueManager().start(new Dialogue(player, npc) {
                        @Override
                        public void buildDialogue() {
                            npc("Please help out dear Nick, he doesn't deserve this.", Expression.HIGH_REV_NORMAL);
                        }
                    });
                    break;
            }
        });
    }
    
    @Override
    public int[] getNPCs() {
        return new int[]{ChristmasConstants.BETTY_NPC_ID};
    }
}
