package com.zenyte.game.content.event.christmas2019;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;

/**
 * @author Corey
 * @since 17/12/2019
 */
public class EbenezerScourge extends NPCPlugin {
    
    private static final int RELEASE_SANTA = 10;
    private static final int RUIN_FEAST = 20;
    
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            final String impName = ChristmasUtils.getImpName(player);
            
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    npc("Trespassers! On my property! Do you not know who I am?", Expression.HIGH_REV_MAD);
                    npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "You is a bitter, crankerous old...", Expression.HIGH_REV_MAD);
                    player("You're Ebenezer Scourge?", Expression.FURIOUS);
                    npc("I am. Not that it's any of your business. Now, why are you trespassing, " +
                                (player.getAppearance().isMale() ? "boy" : "girl") + "? Speak quickly.", Expression.HIGH_REV_MAD);
                    npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "...you is mean and foul and greedy...", Expression.HIGH_REV_MAD);
                    options(TITLE,
                            new DialogueOption("We're here to ask you to release Santa.", key(RELEASE_SANTA)),
                            new DialogueOption("Why did you ruin the Queen of Snow's feast?", key(RUIN_FEAST)),
                            new DialogueOption("Actually, I have to go.", this::finish));
                    
                    {
                        player(RELEASE_SANTA, "We're here to ask you to release Santa and return the food you stole from our feast.");
                        npc("Father Christmas is the epitome of human weakness. I will not be having him, or any joy and merriment for that matter, in my back yard.", Expression.HIGH_REV_MAD);
                        npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "...you is nasty and crew-ool...", Expression.HIGH_REV_MAD);
                        player("But it's Christmas!", Expression.SAD);
                        npc("Bah, humbug!", Expression.HIGH_REV_MAD);
                        player("Please, won't you reconsider?");
                        npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "...and your breath stinks!", Expression.HIGH_REV_MAD);
                        npc("Silence, both of you! This conversation is over! I plan to sleep through the rest of this vile festive period. If you disturb my slumber you shall feel my wrath - and you can tell that to the idle merry-makers slacking around", Expression.HIGH_REV_MAD);
                        npc("in the Land of Snow.", Expression.HIGH_REV_MAD);
                        npc("Oh and be warned: if you try and help that oaf in the cage you'll be joining him!", Expression.HIGH_REV_MAD).executeAction(() -> {
                            AChristmasWarble.progress(player, AChristmasWarble.ChristmasWarbleProgress.SPOKEN_TO_SCOURGE);
                            player.getPacketDispatcher().sendGraphics(new Graphics(2508), npc.getLocation());
                            player.setFaceEntity(null);
                            finish();
                            player.lock(2);
                            
                            WorldTasksManager.schedule(() -> player.getDialogueManager().start(new Dialogue(player, ChristmasConstants.PERSONAL_SNOW_IMP) {
                                @Override
                                public void buildDialogue() {
                                    player("Well, that didn't go too well.", Expression.ON_ONE_HAND);
                                    npc(impName, "No.", Expression.HIGH_REV_WONDERING);
                                    player("Well, what's the plan now, 'brains'?");
                                    plain(impName + " appears to be thinking...");
                                    npc(impName, "Ghosts!", Expression.HIGH_REV_HAPPY);
                                    player("Excuse me?");
                                    npc(impName, "The only way to scares old crankerous people is with ghosts, everyone knows that!", Expression.HIGH_REV_NORMAL);
                                    player("Really?");
                                    npc(impName, "Of course. Remember I's the brain. Trust me, this is gonna work!", Expression.HIGH_REV_NORMAL);
                                    player("But where can we get a ghost from?");
                                    npc(impName, "You'll have to dress up as one, of course! We're gonna need to make a costume. Search around this house - you'll need some sheets, something clanky, oh, and a needle and thread!", Expression.HIGH_REV_NORMAL);
                                    player("Well, if you think that would work...");
                                }
                            }), 2);
                            
                        });
                    }
                    
                    {
                        player(RUIN_FEAST, "Why did you ruin the Queen of Snow's feast?", Expression.ANNOYED);
                        npc("Bah, I don't need a reason.", Expression.HIGH_REV_HAPPY);
                        npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "...you is stupid and horrid...", Expression.HIGH_REV_MAD);
                        player("But you don't just ruin someone else's fun for no good reason!");
                        npc("Actually, I do", Expression.HIGH_REV_HAPPY);
                        options(TITLE,
                                new DialogueOption("We're here to ask you to release Santa.", key(RELEASE_SANTA)),
                                new DialogueOption("Actually, I have to go.", this::finish));
                    }
                    
                }
            });
        });
    }
    
    @Override
    public int[] getNPCs() {
        return new int[]{ChristmasConstants.SCOURGE_NPC_ID};
    }
}
