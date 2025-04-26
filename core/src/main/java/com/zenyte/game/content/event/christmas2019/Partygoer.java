package com.zenyte.game.content.event.christmas2019;

import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;

/**
 * @author Corey
 * @since 15/12/2019
 */
public class Partygoer extends NPCPlugin {
    
    private static final int WHOS_DONE_THIS = 10;
    private static final int WHAT_AM_I_MEANT_TO_DO_ABOUT_IT = 20;
    private static final int SOUNDS_BAD = 30;
    private static final int I_HAD_BETTER_INVESTIGATE = 50;
    private static final int IM_KIND_OF_BUSY = 60;
    
    public static Dialogue introductionDialogue(final Player player, final NPC npc) {
        return new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                npc("Thank goodness you've come! This is terrible!", Expression.HIGH_REV_SAD);
                player("Woah, calm down, take deep breaths.", Expression.CALM_TALK);
                npc("But all the food's been stolen! Santa's been kidnapped! The Queen of Snow's feast is ruined!", Expression.HIGH_REV_SHOCKED);
                
                options(TITLE,
                        new DialogueOption("Who's done this?", key(WHOS_DONE_THIS)),
                        new DialogueOption("So, what am I meant to do about it?", key(WHAT_AM_I_MEANT_TO_DO_ABOUT_IT)),
                        new DialogueOption("Sounds bad, but I've got to go.", key(SOUNDS_BAD)));
                {
                    player(WHOS_DONE_THIS, "Who's done this?");
                    npc("Ebenezer Scourge! From what I gather, he lives alone in an old house nearby. He's a selfish, Christmas-hating old man.", Expression.HIGH_REV_MAD);
                    options(TITLE,
                            new DialogueOption("I had better investigate.", key(I_HAD_BETTER_INVESTIGATE)),
                            new DialogueOption("I'm kind of busy at the moment.", key(IM_KIND_OF_BUSY)));
                    {
                        player(I_HAD_BETTER_INVESTIGATE, "I had better investigate.");
                        npc("I think the snow imps are investigating as well. Perhaps you should speak with one of them.", Expression.HIGH_REV_NORMAL);
                        player("Thanks.");
                    }
                    {
                        player(IM_KIND_OF_BUSY, "I'm kind of busy at the moment.");
                    }
                }
                {
                    player(WHAT_AM_I_MEANT_TO_DO_ABOUT_IT, "So, what am I meant to do about it?", Expression.ON_ONE_HAND);
                    npc("You've got to save Christmas! Santa is trapped, you have to help free him!", Expression.HIGH_REV_SAD);
                    plain("You should speak to the " + Colour.BLUE.wrap("Queen of Snow") + " to find out how to help.");
                }
                {
                    player(SOUNDS_BAD, "Sounds bad, but I've got to go.", Expression.ON_ONE_HAND);
                    npc("Please help us!", Expression.HIGH_REV_SAD);
                }
            }
        };
    }
    
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            switch (AChristmasWarble.getProgress(player)) {
                case SANTA_FREED:
                case EVENT_COMPLETE:
                case CAN_OPEN_PRESENT:
                    player.getDialogueManager().start(new Dialogue(player, npc) {
                        @Override
                        public void buildDialogue() {
                            npc("Thank you for saving Christmas!", Expression.HIGH_REV_HAPPY);
                        }
                    });
                    break;
                case FIND_OUT_ABOUT_SCOURGES_PAST:
                    player.getDialogueManager().start(new Dialogue(player, npc) {
                        @Override
                        public void buildDialogue() {
                            npc("Ghost! There's a ghost at the feast! Is it on the guest list?", Expression.HIGH_REV_SCARED);
                            player("I'm not a ghost. I'm an adventurer - and I think I was invited.");
                            npc("Name?", Expression.HIGH_REV_NORMAL);
                            player(player.getName());
                            npc("Welcome, " + player.getName() + "! Welcome!", Expression.HIGH_REV_NORMAL);
                            player("Hello there. I don't suppose you know anything about Scourge's past?");
                            npc("No, sorry.", Expression.HIGH_REV_NORMAL);
                        }
                    });
                    break;
                case FROZEN_GUESTS:
                    player.getDialogueManager().start(new Dialogue(player, npc) {
                        @Override
                        public void buildDialogue() {
                            npc("Someone must save them!", Expression.HIGH_REV_SCARED);
                        }
                    });
                    break;
                default:
                    player.getDialogueManager().start(introductionDialogue(player, npc));
                    break;
            }
        });
    }
    
    @Override
    public int[] getNPCs() {
        return new int[]{ChristmasConstants.PARTYGOER_EVENT_INTRODUCER, ChristmasConstants.PARTYGOER_NPC_ID, ChristmasConstants.PARTYGOER_2_NPC_ID, ChristmasConstants.PARTYGOER_3_NPC_ID};
    }
    
}
