package com.zenyte.game.content.event.christmas2019;

import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;

/**
 * @author Corey
 * @since 19/12/2019
 */
public class JackFrost extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            switch (AChristmasWarble.getProgress(player)) {
                case FIND_OUT_ABOUT_SCOURGES_PAST:
                    player.getDialogueManager().start(new Dialogue(player, npc) {
                        @Override
                        public void buildDialogue() {
                            if (ChristmasUtils.wearingGhostCostume(player)) {
                                npc("I'm not talking to you while you're in those bedsheets.", Expression.HIGH_REV_NORMAL);
                                return;
                            }
                            player("Hello, Jack.");
                            npc("Oh, hello.", Expression.HIGH_REV_NORMAL);
                            player("Do you know much about Scourge?");
                            npc("No.", Expression.HIGH_REV_NORMAL);
                            player("But he's your neighbour?");
                            npc("So?", Expression.HIGH_REV_NORMAL);
                        }
                    });
                    break;
                default:
                    player.getDialogueManager().start(new Dialogue(player, npc) {
                        @Override
                        public void buildDialogue() {
                            player("Hello, Jack.");
                            plain("Jack doesn't seem to be paying attention to what you're saying.");
                        }
                    });
                    break;
            }
        });
    }
    
    @Override
    public int[] getNPCs() {
        return new int[]{ChristmasConstants.JACK_FROST_NPC_ID};
    }
}
