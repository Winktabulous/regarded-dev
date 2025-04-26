package com.zenyte.game.content.event.christmas2019;

import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;

import java.util.ArrayList;

/**
 * @author Corey
 * @since 15/12/2019
 */
public class QueenOfSnow extends NPCPlugin {
    private static final int TALK_ABOUT_THE_FEAST = 10;
    private static final int ASK_ABOUT_THE_LAND_OF_SNOW = 20;
    private static final int PERHAPS_I_COULD_HELP = 30;
    private static final int CANT_YOU_HELP = 40;
    private static final int TALK_ABOUT_SCOURGE = 60;

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    final AChristmasWarble.ChristmasWarbleProgress progress = AChristmasWarble.getProgress(player);
                    if (progress == null) {
                        return;
                    }
                    final ArrayList<Dialogue.DialogueOption> options = new ArrayList<DialogueOption>();
                    switch (progress) {
                    case SANTA_FREED: 
                    case EVENT_COMPLETE: 
                        npc("Thank you for confronting Scourge and rescuing Nick. You've been exceedingly brave. " +
                                "Peace has been restored to the Land of Snow.", Expression.HIGH_REV_HAPPY);
                        return;
                    case FIND_OUT_ABOUT_SCOURGES_PAST: 
                        options.add(new DialogueOption("Talk about Scourge", key(TALK_ABOUT_SCOURGE)));
                        break;
                    case FROZEN_GUESTS: 
                        npc("This is terrible; my poor guests!", Expression.HIGH_REV_SHOCKED);
                        player("You have to help, Your Majesty.");
                        npc("Do you think I'm not trying? Scourge is containing my magic; I'm trying to fight it...", Expression.HIGH_REV_NORMAL);
                        player("Keep fighting, Your Majesty.");
                        return;
                    case START: 
                        options.add(new DialogueOption("Talk about the feast.", key(TALK_ABOUT_THE_FEAST)));
                        break;
                    }
                    if (ChristmasUtils.wearingGhostCostume(player)) {
                        npc("Why are you wearing those strange robes?", Expression.HIGH_REV_WONDERING);
                        player("Never fear, it's only I, " + player.getName() + "!");
                    }
                    npc("Oh, hello there noble traveller. I am the Queen of Snow, monarch of the Land of Snow.", Expression.HIGH_REV_NORMAL);
                    options.add(new DialogueOption("Ask about the Land of Snow.", key(ASK_ABOUT_THE_LAND_OF_SNOW)));
                    options.add(new DialogueOption("Leave.", this::finish));
                    options(TITLE, options.toArray(new DialogueOption[0]));
                    {
                        player(TALK_ABOUT_THE_FEAST, "What's going on here?");
                        npc("My imps and I have been preparing a Christmas feast for the people of Gielinor, to celebrate the Yuletide period. Unfortunately, something quite terrible has happened...", Expression.HIGH_REV_NORMAL);
                        npc("Our neighbour, Ebenezer Scourge, stole all the food and kidnapped dear Nick. I am terribly worried for poor Nick - Scourge is not a nice person, and a terribly strong wizard.", Expression.HIGH_REV_SAD);
                        options(TITLE, new DialogueOption("Perhaps I could help?", key(PERHAPS_I_COULD_HELP)), new DialogueOption("Can't you help?", key(CANT_YOU_HELP)), new DialogueOption("Actually, I have to go.", this::finish));
                        {
                            player(PERHAPS_I_COULD_HELP, "Perhaps I could help?");
                            npc("Ah, you sound very much like imps. They're very keen to intervene, I just can't see " +
                                    "how they could possibly beat Scourge.", Expression.HIGH_REV_NORMAL);
                            options(TITLE, new DialogueOption("Can't you help?", key(CANT_YOU_HELP)), new DialogueOption("Actually, I have to go.", this::finish));
                        }
                        {
                            npc(CANT_YOU_HELP, "Things aren't that simple... I want to, but there are...complications.", Expression.HIGH_REV_NORMAL);
                            npc("Besides, Scourge is a very powerful wizard. I doubt I could dissuade him with my kind of magic. Mine is a subtle magic, alas, not battle magic.", Expression.HIGH_REV_NORMAL);
                            player("But couldn't you at least try?", Expression.ON_ONE_HAND);
                            npc("If I were to threaten Scourge, Guthix only knows what he may do. For the safety of my guests and Nick, it is best that I do nothing.", Expression.HIGH_REV_NORMAL);
                            npc("Perhaps you could speak with one of my imps... Scourge wouldn't see the two of you " +
                                    "as a threat, but together you might be able to best him.", Expression.HIGH_REV_NORMAL);
                            player("I might just do that.");
                        }
                    }
                    {
                        player(ASK_ABOUT_THE_LAND_OF_SNOW, "What is this place?");
                        npc("This is the Land of Snow. It was created by Guthix as part of his balancing of the world. The coldness of this place counteracts the heat of the great deserts.", Expression.HIGH_REV_NORMAL);
                    }
                    {
                        player(TALK_ABOUT_SCOURGE, "Hello, Your Majesty. I'm trying to find out some information on " +
                                "Scourge's past. When did he first come to the Land of Snow?");
                        npc("Hmm... He moved to the Land of Snow just under a year ago.", Expression.HIGH_REV_SAD);
                        player("And that was the first time you met?");
                        npc("Well, I have met him before...long ago... Oh, I suppose I should tell you.", Expression.HIGH_REV_SAD);
                        npc("We were in love.", Expression.HIGH_REV_SAD);
                        player("What?");
                        npc("It was a long time ago, long before I became the Queen of Snow, before I'd even met Nick" +
                                ". We were both young.", Expression.HIGH_REV_SAD);
                        npc("He came to the Land of Snow quite by accident, having tried a teleportation spell that went wrong. We met and quickly became great friends.", Expression.HIGH_REV_SAD);
                        npc("He had a keen sense of adventure, and when we explored the Land of Snow together it was as if I was exploring it for the first time.", Expression.HIGH_REV_NORMAL);
                        npc("Then, one crisp morning, he was gone.", Expression.HIGH_REV_SAD);
                        npc("I searched for him for hours, but eventually had to accept that he had returned to Gielinor. I was devastated.", Expression.HIGH_REV_SAD);
                        player("When did you see him again?");
                        npc("He returned on Christmas Eve a few years later. He said that he loved me and had only left by accident.", Expression.HIGH_REV_SAD);
                        npc("I knew otherwise, though. Rumours from Gielinor had reached my ears. He left me to " +
                                "pursue a life of evil. He preyed on the weak - exploiting them to gain power and " +
                                "money. He'd betrayed my heart.", Expression.HIGH_REV_SAD);
                        npc("Besides, in the time that had elapsed, I had met Nick and become queen. I tried to " +
                                "explain that we couldn't be together.", Expression.HIGH_REV_SAD);
                        npc("He wouldn't listen. He grew angry and vowed revenge. Right then I saw him for the " +
                                "greedy, cruel, little man he was.", Expression.HIGH_REV_SAD);
                        player("What did he do?");
                        npc("Nothing. Well, nothing at the time. I didn't see him again until he moved to the Land of" +
                                " Snow. Kidnapping Nick and ruining this Christmas has been his revenge.", Expression.HIGH_REV_SAD);
                        player("Thank you for telling me this").executeAction(() -> AChristmasWarble.progress(player, AChristmasWarble.ChristmasWarbleProgress.GHOST_OF_CHRISTMAS_PAST));
                        final String impName = ChristmasUtils.getImpName(player);
                        npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "Thanks, My Majesty.", Expression.HIGH_REV_NORMAL);
                        npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, player.getName() + ", we's use this " +
                                "against Scourge. A story of such sadness like this'll work for sure!", Expression.HIGH_REV_NORMAL);
                        player("So what do we do now?");
                        npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "We go back to Scourge's house, " +
                                "climb up the stairs and show Scourge the error of his ways!", Expression.HIGH_REV_NORMAL);
                    }
                }
            });
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] {ChristmasConstants.QUEEN_OF_SNOW_ID};
    }
}
