package com.zenyte.game.content.event.christmas2019.cutscenes;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.event.christmas2019.AChristmasWarble;
import com.zenyte.game.content.event.christmas2019.ChristmasConstants;
import com.zenyte.game.content.event.christmas2019.ChristmasUtils;
import com.zenyte.game.model.MinimapState;
import com.zenyte.game.model.ui.GameTab;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.npc.NPC;
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

import java.util.Arrays;
import java.util.List;

import static com.zenyte.game.GameInterface.*;

/**
 * @author Kris | 19/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PresentScourgeCutscene extends Cutscene {
    private final Player player;
    private final ScourgeHouseInstance instance;
    private final Runnable unfadeRunnable;
    private CutsceneImp imp;
    private NPC dummy;
    private NPC selectedNPC;
    private static final List<GameInterface> closedTabs = Arrays.asList(COMBAT_TAB, SKILLS_TAB, JOURNAL_HEADER_TAB, INVENTORY_TAB, EQUIPMENT_TAB, PRAYER_TAB_INTERFACE, SPELLBOOK, SETTINGS, EMOTE_TAB, GAME_NOTICEBOARD);
    private static final List<String> options = Arrays.asList("Jack Frost.", "Tiny Thom.", "Cook.", "Louis the Camel.");
    private String lastOption;

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
        addActions(28, new CameraPositionAction(player, instance.getLocation(2475 + 1, 5404 + 1, 1), 500, 255, 100), new CameraLookAction(player, instance.getLocation(2465, 5396, 1), 0, 255, 100), new DialogueAction(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                player("Greetings, Scourge.").executeAction(() -> turnLeft());
                player("Psst, maybe we should leave, " + impName + ".").executeAction(() -> imp.setInvalidAnimation(new Animation(15029)));
                npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "No way, dude! Say you is the 'Ghost of " +
                        "Christmas Present' - that'll scare 'im.", Expression.HIGH_REV_NORMAL).executeAction(() -> {
                    turnStraight();
                    imp.setInvalidAnimation(new Animation(15028));
                });
                player("I am the Ghost of Christmas Present, Scourge, and I'm here to show you the pain you still " +
                        "cause.");
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "Get out and let me sleep!", Expression.HIGH_REV_MAD);
                options("Select an Option", new DialogueOption("Okay, sorry for disturbing you.", key(20)), new DialogueOption("Not until you have seen what I have to show you!", key(10)));
                player(10, "Not until you have seen what I have to show you!");
                npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "Yeah, you tell 'im.", Expression.HIGH_REV_NORMAL);
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "Ha! You think you're brave, do you, maggot?", Expression.HIGH_REV_MAD);
                npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "We know someone who you've really hurt, " +
                        "Scourge. This is gonna make you feel sooooo bad.", Expression.HIGH_REV_NORMAL);
                player(20, "Okay, sor-").executeAction(() -> imp.setInvalidAnimation(new Animation(15029)));
                npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "Stop!", Expression.HIGH_REV_NORMAL).executeAction(() -> turnLeft());
                npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "We aren't going anywhere.", Expression.HIGH_REV_NORMAL).executeAction(() -> {
                    imp.setInvalidAnimation(new Animation(15028));
                    turnStraight();
                });
                npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "We know someone who you've really hurt, " +
                        "Scourge. This is gonna make you feel sooooo bad.", Expression.HIGH_REV_NORMAL);
            }
        }));
        addActions(30, () -> imp.setInvalidAnimation(new Animation(15029)), new DialogueAction(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "Err, who was it again?", Expression.HIGH_REV_NORMAL).executeAction(() -> turnLeft());
            }
        }));
        jumpToOptions();
        startSpawnSequence(32);
        addActions(44, this::turnRight, new DialogueAction(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                npcWithId(selectedNPC.getId(), "Huh?", Expression.HIGH_REV_MAD).executeAction(() -> selectedNPC.faceEntity(player));
                player("Tiny Thom, would you tell Scourge how he ruined your Christmas?").executeAction(() -> {
                    setSelectedNPCFacingScourge();
                    setImpFacingScourge();
                });
                npcWithId(selectedNPC.getId(), "You ruined everything!", Expression.HIGH_REV_MAD);
                npcWithId(selectedNPC.getId(), "I wanted presents! I wanted food! I wanted a party!", Expression.HIGH_REV_MAD).executeAction(() -> turnStraight());
                npcWithId(selectedNPC.getId(), "I wanted a new pony!", Expression.HIGH_REV_MAD);
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "You bring this spoilt little brat before me as " +
                        "proof of my misdeeds? You've only confirmed the righteousness of my actions.", Expression.HIGH_REV_MAD);
                npcWithId(selectedNPC.getId(), "Err, I'd better be going.", Expression.HIGH_REV_MAD).executeAction(() -> {
                    selectedNPC.finish();
                    player.getPacketDispatcher().sendGraphics(new Graphics(2508), selectedNPC.getLocation());
                });
            }
        }));
        addActions(47, new DialogueAction(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "You're lucky that your attempts have been so " +
                        "feeble they've bordered on the amusing, but I grow tired of your antics. Come back here " +
                        "again, 'ghost', and you shall not find me so merciful.", Expression.HIGH_REV_MAD).executeAction(() -> {
                    imp.setInvalidAnimation(new Animation(15029));
                    turnLeft();
                });
                player("This isn't working. Come on, let's go.");
                npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "But, but... Okies.", Expression.HIGH_REV_NORMAL).executeAction(() -> {
                    imp.setInvalidAnimation(new Animation(15028));
                    turnStraight();
                    AChristmasWarble.progress(player, AChristmasWarble.ChristmasWarbleProgress.CHRISTMAS_PRESENT_FAILED);
                });
            }
        }));
        addActions(49, new CameraPositionAction(player, instance.getLocation(2471, 5400, 1), 550, 255, 100), new CameraLookAction(player, instance.getLocation(2471, 5403, 1), 75, 255, 100));
        final FadeScreen fadeScreen = new FadeScreen(player);
        addActions(50, () -> {
            player.setAnimation(Animation.STOP);
            player.setRunSilent(3);
            player.addWalkSteps(instance.getX(2471), instance.getY(5406), 3);
        });
        addActions(52, fadeScreen::fade, () -> {
            imp.setFaceEntity(player);
            imp.addWalkSteps(instance.getX(2471), instance.getY(5406), 3);
        });
        addActions(55, player::unlock, new CameraResetAction(player), () -> player.getVarManager().sendBit(4606, 0), () -> closedTabs.forEach(tab -> tab.open(player)), () -> player.setLocation(new Location(2465, 5403, 0)), () -> player.getPacketDispatcher().sendClientScript(10700, 0), fadeScreen::unfade, this::finish);
        startSpawnSequence(100);
        addActions(112, this::turnRight, () -> selectedNPC.setFaceLocation(imp.getLocation()));
        addActions(114, this::turnRight, new DialogueAction(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                plain("The camel slowly looks around the room blankly.").executeAction(() -> {
                    turnStraight();
                    imp.setInvalidAnimation(new Animation(15029));
                });
            }
        }));
        addActions(116, new DialogueAction(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "Mebbe we should summon someone more talkative.", Expression.HIGH_REV_NORMAL).executeAction(() -> turnLeft());
            }
        }));
        addActions(117, () -> {
            selectedNPC.finish();
            player.getPacketDispatcher().sendGraphics(new Graphics(2508), selectedNPC.getLocation());
            jumpToOptions();
            jump(31);
        });
        startSpawnSequence(200);
        addActions(212, this::turnRight, new DialogueAction(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                npcWithId(selectedNPC.getId(), "Huh? What am I doing here?", Expression.HIGH_REV_NORMAL).executeAction(() -> selectedNPC.faceEntity(player));
                player("Jack, I need you to tell Scourge how he's ruined your Christmas.");
                npcWithId(selectedNPC.getId(), "Meh.", Expression.HIGH_REV_NORMAL);
                player("He's not ruined your Christmas?");
                npcWithId(selectedNPC.getId(), "I don't care about feasts. I have deeper stuff on my mind.", Expression.HIGH_REV_NORMAL);
                npcWithId(selectedNPC.getId(), "Besides, I won't do something just because you ordered me to.", Expression.HIGH_REV_NORMAL);
                player("Oh.").executeAction(() -> turnStraight());
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "Ha! Is that your proof?", Expression.HIGH_REV_MAD);
                npcWithId(selectedNPC.getId(), "Well, bye.", Expression.HIGH_REV_NORMAL);
            }
        }));
        addActions(213, () -> {
            selectedNPC.finish();
            player.getPacketDispatcher().sendGraphics(new Graphics(2508), selectedNPC.getLocation());
        });
        addActions(215, () -> imp.setInvalidAnimation(new Animation(15029)), new DialogueAction(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "That's not who we were meant to summon!", Expression.HIGH_REV_NORMAL).executeAction(() -> turnLeft());
            }
        }));
        addActions(216, () -> {
            jumpToOptions();
            jump(31);
        });
        startSpawnSequence(300);
        addActions(312, this::turnRight, new DialogueAction(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                npcWithId(selectedNPC.getId(), "By Guthix, no!", Expression.HIGH_REV_SCARED).executeAction(() -> selectedNPC.faceEntity(player));
            }
        }));
        addActions(313, () -> {
            selectedNPC.finish();
            player.getPacketDispatcher().sendGraphics(new Graphics(2508), selectedNPC.getLocation());
        });
        addActions(315, () -> imp.setInvalidAnimation(new Animation(15029)), new DialogueAction(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "Mebbe we should summon someone more talkative.", Expression.HIGH_REV_NORMAL).executeAction(() -> turnLeft());
            }
        }));
        addActions(316, () -> {
            jumpToOptions();
            jump(31);
        });
    }

    private final void jumpToOptions() {
        overrideActions(31, new DialogueAction(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                final ObjectArrayList<Dialogue.DialogueOption> optionsList = new ObjectArrayList<DialogueOption>(options.size());
                for (int i = 0; i < options.size(); i++) {
                    final String option = options.get(i);
                    if (option.equalsIgnoreCase(lastOption)) {
                        continue;
                    }
                    final int index = i;
                    optionsList.add(new DialogueOption(option, () -> {
                        setKey(10 + (index * 10));
                        imp.setInvalidAnimation(new Animation(15028));
                        turnStraight();
                        final int id = option.equalsIgnoreCase("Jack Frost.") ? 15040 : option.equalsIgnoreCase("Tiny Thom.") ? 15059 : option.equalsIgnoreCase("Cook.") ? 15055 : 15043;
                        selectedNPC = new NPC(id, instance.getLocation(2469, 5401, 1), Direction.EAST, 0);
                        selectedNPC.setGraphics(new Graphics(2508));
                    }));
                }
                options("Choose an option:", optionsList.toArray(new DialogueOption[0]));
                player(10, "Jack Frost.").executeAction(() -> {
                    selectedNPC = new NPC(15040, instance.getLocation(2469, 5401, 1), Direction.EAST, 0);
                    selectedNPC.setGraphics(new Graphics(2508));
                    lastOption = "Jack Frost.";
                    jump(200);
                });
                player(20, "Tiny Thom.").executeAction(() -> {
                    selectedNPC = new NPC(15059, instance.getLocation(2469, 5401, 1), Direction.EAST, 0);
                    selectedNPC.setGraphics(new Graphics(2508));
                    lastOption = "Tiny Thom.";
                    jump(32);
                });
                player(30, "Cook.").executeAction(() -> {
                    selectedNPC = new NPC(15055, instance.getLocation(2469, 5401, 1), Direction.EAST, 0);
                    selectedNPC.setGraphics(new Graphics(2508));
                    lastOption = "Cook.";
                    jump(300);
                });
                player(40, "Louis the Camel.").executeAction(() -> {
                    selectedNPC = new NPC(15043, instance.getLocation(2469, 5401, 1), Direction.EAST, 0);
                    selectedNPC.setGraphics(new Graphics(2508));
                    lastOption = "Louis the Camel.";
                    jump(100);
                });
            }
        }));
    }

    private final void startSpawnSequence(final int startTime) {
        addActions(startTime, () -> {
            imp.setInvalidAnimation(new Animation(15021));
            imp.setGraphics(new Graphics(2506));
            imp.setFaceLocation(selectedNPC.getLocation());
        });
        addActions(startTime + 2, new CameraPositionAction(player, instance.getLocation(2469, 5402, 1), 300, 255, 100), new CameraLookAction(player, instance.getLocation(2472, 5402, 1), 50, 255, 100));
        addActions(startTime + 9, new CameraPositionAction(player, instance.getLocation(2475 + 1, 5404 + 1, 1), 500, 255, 100), new CameraLookAction(player, instance.getLocation(2465, 5396, 1), 0, 255, 100), () -> selectedNPC.spawn());
    }

    private final void setImpFacingPlayer() {
        imp.setFaceLocation(player.getLocation());
    }

    private final void setImpFacingScourge() {
        imp.setFaceLocation(instance.getLocation(2471, 5400, 1));
    }

    private final void setSelectedNPCFacingScourge() {
        selectedNPC.setFaceLocation(instance.getLocation(2471, 5400, 1));
    }

    @Override
    public MinimapState getMinimapState() {
        return MinimapState.MAP_DISABLED;
    }

    public PresentScourgeCutscene(Player player, ScourgeHouseInstance instance, Runnable unfadeRunnable) {
        this.player = player;
        this.instance = instance;
        this.unfadeRunnable = unfadeRunnable;
    }
}
