package com.zenyte.game.content.event.easter2020.plugin.npc;

import com.zenyte.game.content.event.easter2020.EasterConstants;
import com.zenyte.game.content.event.easter2020.SplittingHeirs;
import com.zenyte.game.content.event.easter2020.Stage;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.pathfinding.events.player.ObjectEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.ObjectStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.SkipPluginScan;
import com.zenyte.plugins.dialogue.PlainChat;
import com.zenyte.plugins.dialogue.PlayerChat;
import mgi.utilities.StringFormatUtil;

/**
 * @author Kris | 09/04/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@SkipPluginScan
public class NutMachine extends NPCPlugin {
    private static final int FOURTY = 30;
    private static final int TWENTY = 50;
    private static final int FIVE = 70;
    private static final Location nutMachine = new Location(2193, 4372, 0);

    @Override
    public void handle() {
        bind("Look-at", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                if (!SplittingHeirs.progressedAtLeast(player, Stage.RETRAIN_SQUIRRELS)) {
                    player.getDialogueManager().start(new PlayerChat(player, "I should speak with the Easter Bunny's " +
                            "son to see what to do next."));
                    return;
                }
                if (SplittingHeirs.getProgress(player) == Stage.RETRAIN_SQUIRRELS) {
                    player.getDialogueManager().start(new Dialogue(player, EasterConstants.BIG_BEN) {
                        @Override
                        public void buildDialogue() {
                            final int amountOfGold = player.getBank().getAmountOf(ItemId.COINS_995);
                            final String goldString = StringFormatUtil.format(amountOfGold);
                            player("Hello there, cute, fluffly, little squirrel.");
                            npc("Who d'you fink yer talkin' to? I 'appen ta be Big Ben, no' some stoopid, fluffy, " +
                                    "cotton wool-headed park animal. I's intelligent, I's knows what I's wants. And " +
                                    "I's knows how ta gets it.", Expression.EASTER_BUNNY_NORMAL);
                            player("I guess that told me...whoever thought squirrels could talk?");
                            npc("Whoever thought 'umans could fink?", Expression.EASTER_BUNNY_NORMAL);
                            player("Umm, well, Mr Squirrel-");
                            npc("Dat's Big Ben or lord and master ya you, 'uman.", Expression.EASTER_BUNNY_NORMAL);
                            player("Right... Big Ben, I understand you're in charge of this bunch of -");
                            npc("Stop righ' there, 'uman. If you's goin' ta call us rats wiv fluffy tails or vermin, " +
                                    "jus' bear in mind tha' we all be owners of teef strong enuff ta crack a nut. And" +
                                    " yes, I am in charge. Wha's it matter ta you?", Expression.EASTER_BUNNY_NORMAL);
                            player("Right...yes... Right you are, Mr Big Ben. I'm " + player.getName() + ", and I've" +
                                    " come to you on behalf of the Easter Bunny.");
                            npc("Ahh, yes, da boss bunny.", Expression.EASTER_BUNNY_NORMAL);
                            player("Well, he'd like me to get the factory working again. That includes you going back" +
                                    " to work.");
                            npc("Hmm...back ta work you say? That'll cost ya.", Expression.EASTER_BUNNY_NORMAL);
                            if (amountOfGold > 0) {
                                player("I only have " + goldString + " Gold.");
                            } else {
                                player("But I don't have any gold.", Expression.SAD);
                            }
                            npc("Nah, mate, wot use would us'n's 'ave fer gold? We wan' nuts!", Expression.EASTER_BUNNY_NORMAL);
                            player("Oh. Of course, how silly of me. What kind of nuts?");
                            npc("Just the ones we regularly sort is fine.", Expression.EASTER_BUNNY_NORMAL);
                            options(TITLE, new DialogueOption("Hmm, okay, but only 40% of them.", key(FOURTY)), new DialogueOption("Hmm, okay, but only 20% of them.", key(TWENTY)), new DialogueOption("Hmm, okay, but only 5% of them.", key(FIVE)), new DialogueOption("Bye then.", this::finish));
                            {
                                player(FOURTY, "Hmm, okay, but only 40% of them.");
                                npc("Are you 'avin' a larf? If we ate tha' many, old EB would be outta business " +
                                        "within a week! We may be greedy bu' we ain't stoopid.", Expression.EASTER_BUNNY_NORMAL);
                                options(TITLE, new DialogueOption("Hmm, okay, but only 20% of them.", key(TWENTY)), new DialogueOption("Hmm, okay, but only 5% of them.", key(FIVE)), new DialogueOption("Bye then.", this::finish));
                            }
                            {
                                player(TWENTY, "Hmm, okay, but only 20% of them.");
                                npc("Well tha' certainly is an h'attractive offer. 30%", Expression.EASTER_BUNNY_NORMAL);
                                player("22%");
                                npc("Naaaah, mate. 28%", Expression.EASTER_BUNNY_NORMAL);
                                player("They're not really my nuts to give! 25%, Last offer.");
                                npc("You's got Yerself a deal.", Expression.EASTER_BUNNY_NORMAL);
                                player("Eggselent.");
                                npc("Just one slight thing. Well, two, actually.", Expression.EASTER_BUNNY_NORMAL);
                                player("I'm not going to like this, am I?");
                                npc("Well, one: don ever become a comedian, your yolks leave much ta be desired. Two:" +
                                        " we...kind of forgot how to sort tha nuts, so you'll have to teach us.", Expression.EASTER_BUNNY_NORMAL);
                                player("Oh. As you seem to think I'm the eggs-pert. I guess I'll have to teach you, " +
                                        "then.");
                                npc("When yer ready.", Expression.EASTER_BUNNY_NORMAL).executeAction(() -> {
                                    player.getDialogueManager().start(new PlainChat(player, "You begin training the squirrels in the fine art of Nut Sorting...", false));
                                    new FadeScreen(player, () -> {
                                        SplittingHeirs.advanceStage(player, Stage.SQUIRRELS_RETRAINED_SPEAK_T0_BUNNY_JR);
                                        player.getDialogueManager().start(new Dialogue(player, EasterConstants.BIG_BEN) {
                                            @Override
                                            public void buildDialogue() {
                                                npc("Well...fer a 'uman that weren't 'alf bad.", Expression.EASTER_BUNNY_NORMAL);
                                                player("Of course, I am smart! Will you continue sorting now?");
                                                npc("Course, we made us a deal.", Expression.EASTER_BUNNY_NORMAL);
                                            }
                                        });
                                    }).fade(11);
                                });
                            }
                            {
                                player(FIVE, "Hmm, okay, but only 5% of them.");
                                npc("You tryin' ta kill us? We'd waste away ta nuffin'.", Expression.EASTER_BUNNY_NORMAL);
                                options(TITLE, new DialogueOption("Hmm, okay, but only 40% of them.", key(FOURTY)), new DialogueOption("Hmm, okay, but only 20% of them.", key(TWENTY)), new DialogueOption("Bye then.", this::finish));
                            }
                        }
                    });
                } else {
                    player.getDialogueManager().start(new Dialogue(player, EasterConstants.BIG_BEN) {
                        @Override
                        public void buildDialogue() {
                            npc("Cheers for the 'elp guvna!", Expression.EASTER_BUNNY_NORMAL);
                        }
                    });
                }
            }
            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                final WorldObject object = World.getObjectWithType(nutMachine, 10);
                player.setRouteEvent(new ObjectEvent(player, new ObjectStrategy(object), () -> execute(player, npc)));
            }
            @Override
            public void execute(final Player player, final NPC npc) {
                player.stopAll();
                player.faceObject(World.getObjectWithType(nutMachine, 10));
                handle(player, npc);
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] {EasterConstants.NUT_MACHINE, EasterConstants.NUT_MACHINE_BASE, EasterConstants.NUT_MACHINE_START, EasterConstants.NUT_MACHINE_WORKING};
    }
}
