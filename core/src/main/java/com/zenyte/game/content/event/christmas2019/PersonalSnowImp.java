package com.zenyte.game.content.event.christmas2019;

import com.zenyte.game.GameConstants;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;
import com.zenyte.game.world.region.area.LandOfSnowArea;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.utilities.CollectionUtils;

import java.util.ArrayList;

/**
 * @author Corey
 * @since 16/12/2019
 */
public class PersonalSnowImp extends NPCPlugin {
    private static final int WHOS_SCOURGE = 10;
    private static final int SOMETHING_MUST_BE_DONE = 20;
    private static final int COUNT_ME_IN = 30;

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            final String impName = ChristmasUtils.getImpNpcName(player, npc);
            if (AChristmasWarble.getProgress(player) == AChristmasWarble.ChristmasWarbleProgress.FROZEN_GUESTS) {
                player.getDialogueManager().start(new Dialogue(player, npc) {
                    @Override
                    public void buildDialogue() {
                        npc(impName, "De oomans are on fire! Save 'em.", Expression.HIGH_REV_NORMAL);
                        npc(ChristmasUtils.getImpName(player), "We're on it, matey.", Expression.HIGH_REV_NORMAL);
                    }
                });
                return;
            }
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    if (AChristmasWarble.hasCompleted(player)) {
                        npc(impName, "You's and " + ChristmasUtils.getImpName(player) + " has saved the feastie!", Expression.HIGH_REV_HAPPY);
                        return;
                    }
                    player("Hello there, who are you?");
                    npc(impName, "'Ello, I's " + impName + ". Is you here for the feastie?", Expression.HIGH_REV_NORMAL);
                    player("Yes. I'm " + player.getName() + ". I've got to say, you're looking kind of glum, " + impName + ". What's up?");
                    npc(impName, "We's the Queen of Snow's servants. We's were helping her prepare a feastie for all " +
                            "the people of " + GameConstants.SERVER_NAME + " when something bad happened.", Expression.HIGH_REV_NORMAL);
                    player("What happened?", Expression.ANXIOUS);
                    npc(impName, "Ower grumpy, nasty neighbour, Ezeneser Scourges, stole all the food we'd made for " +
                            "the feastie. Ol' Saint Nick tried to stop 'im, but Scourges overpowered 'im.", Expression.HIGH_REV_MAD);
                    final ArrayList<Dialogue.DialogueOption> options = new ArrayList<DialogueOption>();
                    options.add(new DialogueOption("Who's Scourge?", key(WHOS_SCOURGE)));
                    if (!AChristmasWarble.progressedAtLeast(player, AChristmasWarble.ChristmasWarbleProgress.SPOKEN_TO_PERSONAL_IMP)) {
                        options.add(new DialogueOption("Something must be done about this!", key(SOMETHING_MUST_BE_DONE)));
                    }
                    options.add(new DialogueOption("Sorry, I've got to go, " + impName + ".", this::finish));
                    options(TITLE, options.toArray(new DialogueOption[0]));
                    {
                        player(WHOS_SCOURGE, "Who's Scourge?");
                        npc(impName, "Scourges is the foulest, most greedy and des-pick-able ooman about. Every time " +
                                "he leaves that ol' house of 'is he does something nasty.", Expression.HIGH_REV_NORMAL);
                        player("And did you say he overpowered Santa?");
                        npc(impName, "Scourges is a dark wizard. When Nick tried to stop 'im, Scourges bound him in " +
                                "chains with a snap o' his digits.", Expression.HIGH_REV_NORMAL).executeAction(() -> {
                            if (!AChristmasWarble.progressedAtLeast(player, AChristmasWarble.ChristmasWarbleProgress.SPOKEN_TO_PERSONAL_IMP)) {
                                key(SOMETHING_MUST_BE_DONE);
                            }
                        });
                        player(SOMETHING_MUST_BE_DONE, "Something must be done about this!");
                        npc(impName, "You's right! Something must be done! P'rhaps...", Expression.HIGH_REV_NORMAL);
                        player("What?", Expression.ON_ONE_HAND);
                        npc(impName, "P'rhaps we's join forces and take on Scourges together?", Expression.HIGH_REV_NORMAL);
                        options(TITLE, new DialogueOption("Count me in!", key(COUNT_ME_IN)), new DialogueOption(
                                "Sorry, I don't have time right now.", this::finish));
                        {
                            player(COUNT_ME_IN, "Count me in!");
                            player("I suppose we'd better think up a cunning plan.");
                            npc(impName, "Oh, don't worry 'bout that. I's the brains of this outfit.", Expression.HIGH_REV_NORMAL);
                            player("You're the brains? What am I, then?");
                            npc(impName, "Well, you ain't the brains, 'n you certainly awn't the looks. You ain't " +
                                    "really the brawn neither - look at them skinny arms.", Expression.HIGH_REV_NORMAL);
                            npc(impName, "I know! You can be the height.", Expression.HIGH_REV_NORMAL);
                            player("Well, 'brains', what's the plan?", Expression.LAUGH);
                            npc(impName, "We need to go and talk to Scourges. 'Is house is to the east. Let's go!", Expression.HIGH_REV_NORMAL).executeAction(() -> {
                                LandOfSnowArea.spawnPet(player);
                                ChristmasUtils.saveImpName(player, impName);
                                ChristmasUtils.saveImpId(player, Imp.forBaseId(npc.getId()).getIndex());
                                AChristmasWarble.progress(player, AChristmasWarble.ChristmasWarbleProgress.SPOKEN_TO_PERSONAL_IMP);
                            });
                        }
                    }
                }
            });
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] {ChristmasConstants.PERSONAL_SNOW_IMP};
    }


    public enum Imp {
        ONE(1, 15009, ChristmasConstants.SNOW_IMP_VAR1), TWO(2, 15010, ChristmasConstants.SNOW_IMP_VAR2), THREE(3, 15011, ChristmasConstants.SNOW_IMP_VAR3), FOUR(4, 15012, ChristmasConstants.SNOW_IMP_VAR4);
        public static final Int2ObjectMap<Imp> byId;
        public static final Int2ObjectMap<Imp> byBaseId;
        private static final Imp[] values = values();

        static {
            CollectionUtils.populateMap(values, byId = new Int2ObjectOpenHashMap<>(values.length), Imp::getIndex);
            CollectionUtils.populateMap(values, byBaseId = new Int2ObjectOpenHashMap<>(values.length), Imp::getBaseId);
        }

        private final int index;
        private final int baseId;
        private final int varbit;

        public static Imp forId(final int id) {
            return byId.get(id);
        }

        public static Imp forBaseId(final int id) {
            return byBaseId.get(id);
        }

        Imp(int index, int baseId, int varbit) {
            this.index = index;
            this.baseId = baseId;
            this.varbit = varbit;
        }

        public int getIndex() {
            return index;
        }

        public int getBaseId() {
            return baseId;
        }

        public int getVarbit() {
            return varbit;
        }
    }
}
