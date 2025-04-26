package com.zenyte.game.content.event.christmas2019.cutscenes;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.event.christmas2019.AChristmasWarble;
import com.zenyte.game.content.event.christmas2019.ChristmasConstants;
import com.zenyte.game.content.event.christmas2019.ChristmasUtils;
import com.zenyte.game.model.MinimapState;
import com.zenyte.game.model.ui.GameTab;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.AnimationUtil;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Emote;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.Cutscene;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraLookAction;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraPositionAction;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraResetAction;
import com.zenyte.game.world.entity.player.cutscene.actions.DialogueAction;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.utilities.StringFormatUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static com.zenyte.game.GameInterface.*;

/**
 * @author Kris | 21/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FutureScourgeCutscene extends Cutscene {
    private final Player player;
    private final ScourgeHouseInstance instance;
    private final Runnable unfadeRunnable;
    private CutsceneImp imp;
    private NPC dummy;
    private static final List<GameInterface> closedTabs = Arrays.asList(COMBAT_TAB, SKILLS_TAB, JOURNAL_HEADER_TAB, INVENTORY_TAB, EQUIPMENT_TAB, PRAYER_TAB_INTERFACE, SPELLBOOK, SETTINGS, EMOTE_TAB, GAME_NOTICEBOARD);
    private Emote lastSelectedEmote;

    @Override
    public void build() {
        final String impName = ChristmasUtils.getImpName(player);
        addActions(0, player::lock, () -> closedTabs.forEach(tab -> player.getInterfaceHandler().closeInterface(tab)), () -> player.getInterfaceHandler().openGameTab(GameTab.FRIENDS_TAB));
        addActions(3, () -> instance.spawnAtDoor(player), () -> player.getVarManager().sendBit(4606, 1), () -> player.getPacketDispatcher().sendClientScript(10700, 1));
        addActions(4, new CameraPositionAction(player, instance.getLocation(2471, 5407, 1), 350, -128, 0), new CameraLookAction(player, instance.getLocation(2471, 5390, 1), 200, -128, 0), () -> {
            dummy = new NPC(24, instance.getLocation(2470, 5398, 1), Direction.NORTH, 0);
            dummy.spawn();
            imp = new CutsceneImp(player.getLocation().transform(Direction.NORTH, 1));
            imp.spawn();
        });
        addActions(5, unfadeRunnable);
        addActions(8, new CameraPositionAction(player, instance.getLocation(2471, 5407 - 3, 1), 350, 5, 0), new CameraLookAction(player, instance.getLocation(2471, 5390 - 3, 1), 200, 5, 0));
        addActions(12, () -> World.sendObjectAnimation(new WorldObject(46180, 10, 2, instance.getLocation(2470, 5398, 1)), new Animation(15001)), () -> dummy.setInvalidAnimation(new Animation(15001)));
        addActions(21, new CameraPositionAction(player, instance.getLocation(2471, 5400, 1), 550, 255, 100), new CameraLookAction(player, instance.getLocation(2471, 5403, 1), 75, 255, 100), () -> {
            player.setRunSilent(2);
            player.addWalkSteps(instance.getX(2471), instance.getY(5403), 2);
        });
        addActions(22, () -> {
            imp.addWalkSteps(instance.getX(2471), instance.getY(5404), 2, false);
            imp.addWalkSteps(instance.getX(2472), instance.getY(5404), 1, false);
            imp.addWalkSteps(instance.getX(2472), instance.getY(5402), 2, false);
            WorldTasksManager.schedule(this::setImpFacingScourge, 6);
        });
        addActions(28, new DialogueAction(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "Did I not warn you what would happen if you disturbed me again?", Expression.HIGH_REV_MAD);
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "Well? Speak up, ghost.", Expression.HIGH_REV_MAD);
            }
        }));
        addActions(30, new CameraPositionAction(player, instance.getLocation(2475 + 1, 5404 + 1, 1), 500, 255, 100), new CameraLookAction(player, instance.getLocation(2465, 5396, 1), 0, 255, 100));
        addActions(32, () -> imp.setInvalidAnimation(new Animation(15029)), new DialogueAction(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "Psst, don't say anything.", Expression.HIGH_REV_NORMAL).executeAction(() -> turnLeft());
                player("Bu-");
                npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "Shhh!", Expression.HIGH_REV_NORMAL).executeAction(() -> {
                    turnStraight();
                    imp.setInvalidAnimation(new Animation(15028));
                });
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "Ghost, why don't you speak?", Expression.HIGH_REV_MAD);
            }
        }));
        addActions(34, new DialogueAction(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "Well, if you don't wish to speak, allow me. " +
                        "We've had the Ghost of Christmas Past and the Ghost of Christmas Present - that much leave " +
                        "the Ghost of Christmas Yet to Come. Am I correct?", Expression.HIGH_REV_MAD);
                player("Err-").executeAction(() -> imp.setInvalidAnimation(new Animation(15029)));
                npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "Quiets.", Expression.HIGH_REV_NORMAL).executeAction(() -> imp.setInvalidAnimation(new Animation(15028)));
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "Well, what have you come to show me, ghost? More spoilt brats? Another tale of lost love?", Expression.HIGH_REV_MAD);
                npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "Scare hims, " + player.getName() + ". Show him how scary you is.", Expression.HIGH_REV_NORMAL);
            }
        }));
        executeOptions(35, new Emote[] {Emote.LAUGH, Emote.BLOW_KISS, Emote.ANGRY}, Emote.ANGRY, 100);
        addActions(100, new DialogueAction(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "Stop that!", Expression.HIGH_REV_MAD);
            }
        }));
        addActions(101, new CameraPositionAction(player, instance.getLocation(2475 + 1, 5404 + 1, 1), 500, 255, 100), new CameraLookAction(player, instance.getLocation(2465, 5396, 1), 0, 255, 100), new DialogueAction(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "Hehe, that's scared him, matey.", Expression.HIGH_REV_NORMAL);
            }
        }));
        executeOptions(102, new Emote[] {Emote.JIG, Emote.TRICK, Emote.BOW}, Emote.TRICK, 200);
        addActions(200, new DialogueAction(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "Where did they come from?", Expression.HIGH_REV_MAD);
            }
        }));
        addActions(201, new CameraPositionAction(player, instance.getLocation(2475 + 1, 5404 + 1, 1), 500, 255, 100), new CameraLookAction(player, instance.getLocation(2465, 5396, 1), 0, 255, 100), new DialogueAction(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "Boo hoo, Scourge, scared of bats?", Expression.HIGH_REV_NORMAL);
            }
        }));
        executeOptions(202, new Emote[] {Emote.DRAMATIC_POINT, Emote.SPIN, Emote.JUMP_FOR_JOY}, Emote.DRAMATIC_POINT, 300);
        addActions(301, () -> imp.addWalkSteps(instance.getX(2473), instance.getY(5401), 2, false));
        final WorldObject object = new WorldObject(46226, 10, 3, instance.getLocation(2469, 5401, 1));
        addActions(303, () -> {
            player.setRunSilent(3);
            player.addWalkSteps(instance.getX(2472), instance.getY(5401), 3, false);
        });
        addActions(304, () -> {
            imp.setInvalidAnimation(new Animation(15021));
            imp.setGraphics(new Graphics(2506));
            imp.faceObject(object);
        });
        addActions(305, () -> player.faceObject(object));
        addActions(312, () -> {
            player.getMusic().playJingle(1000);
            World.spawnObject(object);
            player.setInvalidAnimation(Emote.DRAMATIC_POINT.getAnimation());
            player.sendMessage("You unlock the Dramatic Point emote.");
            player.getAttributes().put("Christmas 2019 event", 1);
            player.getEmotesHandler().unlock(Emote.DRAMATIC_POINT);
        });
        addActions(316, new CameraPositionAction(player, instance.getLocation(2472, 5401, 1), 220, 255, 100), new CameraLookAction(player, instance.getLocation(2467, 5402, 1), 25, 255, 100));
        addActions(319, new CameraPositionAction(player, instance.getLocation(2475 + 1, 5404 + 1, 1), 500, 255, 100), new CameraLookAction(player, instance.getLocation(2465, 5396, 1), 0, 255, 100));
        addActions(321, new DialogueAction(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "G-g-ghost. The name on that grave...surely there is some way to...", Expression.HIGH_REV_MAD).executeAction(() -> player.addWalkSteps(instance.getX(2471), instance.getY(5401), 1));
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "Do you show what will be or only what may be? For I can change. I-I can learn to be kind...", Expression.HIGH_REV_MAD).executeAction(() -> player.addWalkSteps(instance.getX(2471), instance.getY(5402), 1));
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "I beg you, ghost, do not turn your back on me.", Expression.HIGH_REV_MAD).executeAction(() -> player.addWalkSteps(instance.getX(2471), instance.getY(5403), 1));
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "I shall be generous from now on!", Expression.HIGH_REV_MAD).executeAction(() -> player.addWalkSteps(instance.getX(2471), instance.getY(5404), 1));
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "I'll free that oa- I'll free Santa. I'll return" +
                        " the food!", Expression.HIGH_REV_MAD);
                npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "Hehe.", Expression.HIGH_REV_NORMAL).executeAction(() -> player.addWalkSteps(instance.getX(2471), instance.getY(5405), 1));
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "Oh, ghost! Have mercy on me!", Expression.HIGH_REV_MAD);
            }
        }));
        final FadeScreen fadeScreen = new FadeScreen(player);
        addActions(322, fadeScreen::fade, () -> AChristmasWarble.progress(player, AChristmasWarble.ChristmasWarbleProgress.SANTA_FREED));
        final Location pLoc = instance.getLocation(2464, 5401, 0);
        final Location iLoc = instance.getLocation(2465, 5401, 0);
        addActions(325, () -> {
            player.setLocation(pLoc);
            imp.setLocation(iLoc);
        });
        addActions(326, () -> {
            player.setFaceLocation(iLoc);
            imp.setFaceLocation(pLoc);
        }, new CameraPositionAction(player, instance.getLocation(2464, 5406, 0), 550, 255, 100), new CameraLookAction(player, instance.getLocation(2464, 5400, 0), 75, 255, 100));
        addActions(327, fadeScreen::unfade);
        addActions(330, new DialogueAction(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "Look, Scourges freed Nick! We did it! We did it!", Expression.HIGH_REV_NORMAL);
                player("Hey, it was your plan. I was just the height, remember? Well done, little fella.");
                npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "Let's get back to the feast, matey. We need" +
                        " to make sure Ol' Nick is safe!", Expression.HIGH_REV_NORMAL);
            }
        }));
        final FadeScreen fs = new FadeScreen(player);
        addActions(331, fs::fade);
        addActions(334, player::unlock, new CameraResetAction(player), () -> player.getVarManager().sendBit(4606, 0), () -> closedTabs.forEach(tab -> tab.open(player)), () -> player.setLocation(new Location(2098, 5405, 0)), () -> player.getPacketDispatcher().sendClientScript(10700, 0));
        addActions(335, fs::unfade);
    }

    private void executeOptions(final int startTime, final Emote[] options, final Emote correctOption, final int jumpStage) {
        overrideActions(startTime, new DialogueAction(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                final ObjectArrayList<Dialogue.DialogueOption> optionsList = new ObjectArrayList<DialogueOption>();
                for (int i = 0; i < options.length; i++) {
                    final int index = i;
                    final Emote option = options[i];
                    if (option == lastSelectedEmote) {
                        continue;
                    }
                    optionsList.add(new DialogueOption(label(option), () -> {
                        if (option == Emote.DRAMATIC_POINT) {
                            jump(301);
                            return;
                        }
                        jump(startTime + 1 + (index * 10));
                        lastSelectedEmote = option;
                    }));
                }
                options("Choose an option: ", optionsList.toArray(new DialogueOption[0]));
            }
        }), new CameraPositionAction(player, instance.getLocation(2475 + 1, 5404 + 1, 1), 500, 255, 100), new CameraLookAction(player, instance.getLocation(2465, 5396, 1), 0, 255, 100));
        for (int i = 0; i < 3; i++) {
            overrideActions(startTime + 1 + (i * 10), getEmoteRunnables(options[i]));
            final int duration = AnimationUtil.getDuration(options[i].getAnimation()) / 600;
            overrideActions(startTime + Math.max(2, duration) + (i * 10), getDialogueRunnables(buildRunnable(options, options[i], correctOption, jumpStage, startTime)));
        }
    }

    private final Runnable buildRunnable(@NotNull final Emote[] allEmotes, @NotNull final Emote selectedEmote, @NotNull final Emote correctEmote, final int jumpStage, final int startTime) {
        if (selectedEmote == correctEmote) {
            return () -> jump(jumpStage);
        }
        return new DialogueAction(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "Ha! What was that meant to be?", Expression.HIGH_REV_MAD);
            }
        }) {
            @Override
            public void run() {
                executeOptions(startTime, allEmotes, correctEmote, jumpStage);
                jump(startTime);
                super.run();
            }
        };
    }

    private final String label(@NotNull final Emote emote) {
        if (emote == Emote.TRICK) {
            return "Scare.";
        } else if (emote == Emote.SPIN) {
            return "Twirl.";
        } else if (emote == Emote.JUMP_FOR_JOY) {
            return "Jump with Joy.";
        }
        return format(emote);
    }

    /*
    TLDR:
    Three options each dialogue.
    Each failed option comes through with the same response.
    Upon failing an option, the dialogue pops up without the option you clicked. You could keep circling between the two failed options over and over.
    Upon successfully selecting an option, a different outcome appears every time. This can be excluded from the loop.
     */
    private final String format(@NotNull final Emote emote) {
        return StringFormatUtil.formatString(emote.name()) + ".";
    }

    private final Runnable[] getDialogueRunnables(@NotNull final Runnable runnable) {
        return new Runnable[] {runnable};
    }

    private final Runnable[] getEmoteRunnables(@NotNull final Emote emote) {
        return new Runnable[] {new CameraPositionAction(player, instance.getLocation(2471, 5400, 1), 550, 255, 100), new CameraLookAction(player, instance.getLocation(2471, 5403, 1), 75, 255, 100), () -> Emote.play(player, emote.name().toLowerCase().replace(" ", "_"))};
    }

    private final void setImpFacingScourge() {
        imp.setFaceLocation(instance.getLocation(2471, 5400, 1));
    }

    @Override
    public MinimapState getMinimapState() {
        return MinimapState.MAP_DISABLED;
    }

    public FutureScourgeCutscene(Player player, ScourgeHouseInstance instance, Runnable unfadeRunnable) {
        this.player = player;
        this.instance = instance;
        this.unfadeRunnable = unfadeRunnable;
    }
}
