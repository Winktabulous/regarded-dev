package com.zenyte.game.content.event.easter2020.plugin.npc;

import com.zenyte.game.content.event.easter2020.*;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;
import com.zenyte.plugins.SkipPluginScan;

/**
 * @author Corey
 * @since 28/03/2020
 */
@SkipPluginScan
public class EasterBunny extends NPCPlugin {

    private static final int YOU_HAVE_A_SON = 20;

    private static final int WHATS_SO_SPECIAL_ABOUT_AN_EGG_PLANT = 30;

    private static final int CAN_I_HELP = 50;

    private static final int SOMETHING_ELSE = 60;

    private static final int OFF_I_GO_THEN = 70;

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                final Stage progress = SplittingHeirs.getProgress(player);
                if (progress == Stage.NOT_STARTED) {
                    player("Hello!");
                    npc("...", Expression.EASTER_BUNNY_SAD);
                    player("Hello?");
                    npc("Oh! Hello there. Sorry, I didn't really notice you.", Expression.EASTER_BUNNY_SAD);
                    player("It's okay. I just hoped you'd have some chocolate for me.");
                    npc("So did I, but it's all gone wrong. I only wanted a bit of a rest. Now no one will get their " + "chocolate goodness.", Expression.EASTER_BUNNY_SAD);
                    player("WHAT?");
                    npc("I know, depressing, isn't it?", Expression.EASTER_BUNNY_SAD);
                    player("Why? What happened this year?");
                    npc("I'm getting too old for this chocolate delivery job, so I went away on a little holiday, " + "hoping that would refresh me. I left my son in charge of the Egg Plant...and now it's " + "all in pieces because he's so lazy.", Expression.EASTER_BUNNY_SAD);
                    options(TITLE, new DialogueOption("What? You have a son?", key(YOU_HAVE_A_SON)), new DialogueOption("What's so special about an egg plant?", key(WHATS_SO_SPECIAL_ABOUT_AN_EGG_PLANT)), new DialogueOption("Can I help?", key(CAN_I_HELP)), new DialogueOption("Bye then.", this::finish));
                    {
                        player(YOU_HAVE_A_SON, "What? You have a son?");
                        npc("Yes. Is that so surprising? The only surprise to me is that he has become so lazy. Still" + "...I guess that's my own fault. I spoilt him.", Expression.EASTER_BUNNY_SAD);
                        player("I see...so that's why there won't be any eggs?");
                        npc("Yeah.", Expression.EASTER_BUNNY_SAD);
                        options(TITLE, new DialogueOption("What's so special about an egg plant?", key(WHATS_SO_SPECIAL_ABOUT_AN_EGG_PLANT)), new DialogueOption("Can I help?", key(CAN_I_HELP)), new DialogueOption("Bye then.", this::finish));
                    }
                    {
                        player(WHATS_SO_SPECIAL_ABOUT_AN_EGG_PLANT, "What's so special about an egg plant?");
                        npc("Oh that's the factory. It produces all those lovely, sticky treats like chocolate eggs, " + "some of them with nutty centres, or brightly painted to be a joy to the eyes. That's" + " why we call it the 'Egg Plant'.", Expression.EASTER_BUNNY_NORMAL);
                        player("Sounds a bit nuts to me.");
                        options(TITLE, new DialogueOption("What? You have a son?", key(YOU_HAVE_A_SON)), new DialogueOption("Can I help?", key(CAN_I_HELP)), new DialogueOption("Bye then.", this::finish));
                    }
                    {
                        player(CAN_I_HELP, "Can I help?");
                        npc("Oh, would you? I hope I'm not being too much truffle. Er...trouble.", Expression.EASTER_BUNNY_SAD);
                        player("Of course, what shall I do?");
                        npc("You could fix up the Egg Plant so it's working; that would be a start.", Expression.EASTER_BUNNY_SAD);
                        player("Okay!");
                        npc("Now you've agreed to help, you'll need to get through the warrens to the Egg Plant, to " + "speak to that lazy son of mine. For that you need to be bunny-sized!", Expression.EASTER_BUNNY_NORMAL);
                        player("How do I do that?");
                        npc("You simply go down the rabbit hole. My magic will sort the transformation, though you may feel a little itchy for a couple of weeks afterwards.", Expression.EASTER_BUNNY_NORMAL).executeAction(() -> SplittingHeirs.advanceStage(player, Stage.START));
                        options(TITLE, new DialogueOption("I'd like to ask something else.", key(SOMETHING_ELSE)), new DialogueOption("Off I go then!", key(OFF_I_GO_THEN)));
                        {
                            options(SOMETHING_ELSE, TITLE, new DialogueOption("What? You have a son?", key(YOU_HAVE_A_SON)), new DialogueOption("What's so special about an egg plant?", key(WHATS_SO_SPECIAL_ABOUT_AN_EGG_PLANT)), new DialogueOption("Bye then.", this::finish));
                        }
                        {
                            player(OFF_I_GO_THEN, "Off I go then!");
                        }
                    }
                } else if (progress == Stage.POST_MEETING_CUTSCENE) {
                    player("I did it! I did it!");
                    npc("Chocs away! That means I can retire, doesn't it?", Expression.EASTER_BUNNY_VERY_HAPPY);
                    player("It does indeed.");
                    npc("It'll be good to get out of this suit.", Expression.EASTER_BUNNY_VERY_HAPPY).executeAction(() -> {
                        try {
                            final AllocatedArea area = MapBuilder.findEmptyChunk(5, 5);
                            final OutsideInstance instance = new OutsideInstance(area);
                            instance.constructRegion();
                            final FadeScreen fs = new FadeScreen(player);
                            fs.fade();
                            player.getCutsceneManager().play(new OutsideCutscene(player, instance, fs::unfade));
                        } catch (OutOfSpaceException e) {
                            e.printStackTrace();
                        }
                    });
                } else if (progress == Stage.POST_UNDRESS_CUTSCENE) {
                    player.getDialogueManager().finish();
                    player.getDialogueManager().start(new OutsideCutscene.UndressDialogue(player, player.getTransmogrifiedId(npc.getDefinitions(), npc.getId())));
                } else if (progress == Stage.EVENT_COMPLETE) {
                    npc("Thanks for helping me out, time to enjoy my holiday!", Expression.EASTER_BUNNY_VERY_HAPPY);
                } else {
                    npc("I hope you can help me out, this old man deserves a break...", Expression.EASTER_BUNNY_SAD);
                    player("I'll see what I can do.");
                }
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { // base
        EasterConstants.SAD_EASTER_BUNNY, NpcId.EASTER_BUNNY_15178, EasterConstants.HAPPY_EASTER_BUNNY, EasterConstants.EASTER_BUNNY_OUTSIDE_CUTSCENE, EasterConstants.POST_QUEST_EASTER_BUNNY, NpcId.EASTER_BUNNY };
    }
}
