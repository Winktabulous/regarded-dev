package com.zenyte.game.content.event.christmas2019.cutscenes;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.event.christmas2019.ChristmasConstants;
import com.zenyte.game.content.event.christmas2019.ChristmasUtils;
import com.zenyte.game.model.MinimapState;
import com.zenyte.game.model.ui.GameTab;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.Cutscene;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraLookAction;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraPositionAction;
import com.zenyte.game.world.entity.player.cutscene.actions.DialogueAction;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;

import java.util.Arrays;
import java.util.List;

import static com.zenyte.game.GameInterface.*;

/**
 * @author Kris | 18/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PastScourgeCutsceneP1 extends Cutscene {
    private final Player player;
    private final ScourgeHouseInstance instance;
    private final Runnable unfadeRunnable;
    private CutsceneImp imp;
    private NPC dummy;
    private static final List<GameInterface> closedTabs = Arrays.asList(COMBAT_TAB, SKILLS_TAB, JOURNAL_HEADER_TAB, INVENTORY_TAB, EQUIPMENT_TAB, PRAYER_TAB_INTERFACE, SPELLBOOK, SETTINGS, EMOTE_TAB, GAME_NOTICEBOARD);

    @Override
    public void build() {
        final String impName = ChristmasUtils.getImpName(player);
        addActions(0, player::lock, () -> player.getPacketDispatcher().sendClientScript(10700, 1), () -> closedTabs.forEach(tab -> player.getInterfaceHandler().closeInterface(tab)), () -> player.getInterfaceHandler().openGameTab(GameTab.FRIENDS_TAB), () -> player.getPacketDispatcher().sendMinimapState(MinimapState.MAP_DISABLED));
        addActions(3, () -> instance.spawnAtDoor(player), () -> player.getVarManager().sendBit(4606, 1));
        addActions(4, new CameraPositionAction(player, instance.getLocation(2471, 5407, 1), 350, -128, 0), new CameraLookAction(player, instance.getLocation(2471, 5390, 1), 200, -128, 0), () -> {
            dummy = new NPC(24, instance.getLocation(2470, 5398, 1), Direction.NORTH, 0);
            dummy.spawn();
            imp = new CutsceneImp(player.getLocation().transform(Direction.NORTH, 1));
            imp.spawn();
        });
        addActions(5, unfadeRunnable);
        addActions(9, new CameraPositionAction(player, instance.getLocation(2471, 5407 - 3, 1), 350, 5, 0), new CameraLookAction(player, instance.getLocation(2471, 5390 - 3, 1), 200, 5, 0));
        addActions(13, () -> World.sendObjectAnimation(new WorldObject(46180, 10, 2, instance.getLocation(2470, 5398, 1)), new Animation(15001)), () -> dummy.setInvalidAnimation(new Animation(15001)));
        addActions(22, new CameraPositionAction(player, instance.getLocation(2471, 5400, 1), 550, 255, 100), new CameraLookAction(player, instance.getLocation(2471, 5403, 1), 75, 255, 100), () -> {
            player.setRunSilent(2);
            player.addWalkSteps(instance.getX(2471), instance.getY(5403), 2);
        });
        addActions(23, () -> {
            imp.addWalkSteps(instance.getX(2471), instance.getY(5404), 2, false);
            imp.addWalkSteps(instance.getX(2472), instance.getY(5404), 1, false);
            imp.addWalkSteps(instance.getX(2472), instance.getY(5402), 2, false);
            WorldTasksManager.schedule(this::setImpFacingScrouge, 6);
        });
        addActions(26, new DialogueAction(new Dialogue(player, ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID) {
            @Override
            public void buildDialogue() {
                npc("How dare you wake me? What are you doing in those preposterous sheets?", Expression.HIGH_REV_MAD);
            }
        }));
        addActions(27, new CameraPositionAction(player, instance.getLocation(2475 + 1, 5404 + 1, 1), 500, 255, 100), new CameraLookAction(player, instance.getLocation(2465, 5396, 1), 0, 255, 100));
        addActions(29, new DialogueAction(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options("Choose an option:", new DialogueOption("I'm a ghost!", key(10)), new DialogueOption("I'm a " +
                        "human in bedsheets!", key(20)), new DialogueOption("I'm a giant floating napkin!", key(30)));
                player(10, "I'm a ghost!").executeAction(() -> setImpFacingPlayer());
                npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "Psst, say you's the 'Ghost of Christmas " +
                        "Past'. It's spookier.", Expression.HIGH_REV_NORMAL);
                player("I am the Ghost of Christmas Past!").executeAction(() -> setImpFacingScrouge());
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "You're not a ghost, I can see where you've sewn" +
                        " that old bedsheet together.", Expression.HIGH_REV_MAD);
                player("Bedsheets? These are no bedsheets. Those stitches you see represent all the people I've, er...");
                player("Stitched up!");
                player("My punishment for my sins is that I must look like I've been sewn together, poorly, for the " +
                        "rest of eternity.");
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "Ha! A likely story. Then what about those chains; what do they represent?", Expression.HIGH_REV_MAD);
                options("Choose an option:", new DialogueOption("My heavy guilt.", key(90)), new DialogueOption("The weight of my sins.", key(100)), new DialogueOption("Nothing, they're just chains.", key(110)));
                player(20, "I'm a human in bedsheets!").executeAction(() -> setImpFacingPlayer());
                npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "oi, don't say that! You's a ghost remember.", Expression.HIGH_REV_NORMAL);
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "What was that? Stop mumbling, speak properly!", Expression.HIGH_REV_MAD).executeAction(() -> setImpFacingScrouge());
                options("Choose an option:", new DialogueOption("I'm a ghost!", key(10)), new DialogueOption("I'm a " +
                        "giant floating napkin!", key(30)));
                player(30, "I'm a giant floating napkin!").executeAction(() -> setImpFacingPlayer());
                npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "oi, don't say that! You's a ghost remember.", Expression.HIGH_REV_NORMAL);
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "What was that? Stop mumbling, speak properly!", Expression.HIGH_REV_MAD).executeAction(() -> setImpFacingScrouge());
                options("Choose an option:", new DialogueOption("I'm a ghost!", key(10)), new DialogueOption("I'm a " +
                        "human in bedsheets!", key(20)));
                player(90, "These chains represent my heavy guilt.");
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "Pfft, I can't be bothered with self-analyzing " +
                        "ghosts. Begone!", Expression.HIGH_REV_NORMAL);
                player("Let me finish! I was going to say: 'These chains represent my heavy guilt...is what I would " +
                        "say if I were a self-analyzing ghost'. But I'm not. These chains represent...");
                options("Choose an option:", new DialogueOption("The weight of my sins.", key(100)), new DialogueOption("Nothing, they're just chains.", key(110)));
                player(110, "Nothing, they're just chains.");
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "You're telling me those chains are of no " +
                        "significance at all?", Expression.HIGH_REV_NORMAL);
                player("Let me finish! I was going to say: they just look like ordinary chains, except they represent...");
                options("Choose an option:", new DialogueOption("My heavy guilt.", key(90)), new DialogueOption("The weight of my sins.", key(100)));
                player(100, "These chains represent the weight of my sins.");
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "Ah, so you led an evil life?", Expression.HIGH_REV_MAD);
                player("One of the worst. That's why I've come to warn you.");
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "Warn me of what?", Expression.HIGH_REV_MAD);
                player("Warn you that if you don't change your ways you will be doomed to live like me for the rest " +
                        "of eternity!");
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "How dare you! I've done nothing wrong, nothing " +
                        "I tell you!", Expression.HIGH_REV_MAD);
                options("Choose an option:", new DialogueOption("What about ruining the Christmas feast?", key(150)), new DialogueOption("What about betraying the Queen of Snow?", key(200)), new DialogueOption("What about all that dark magic?", key(250)));
                player(150, "What about ruining the Christmas feast?");
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "I hardly think I'll be doomed to spend eternity" +
                        " in chains because I crashed that sappy little party.", Expression.HIGH_REV_MAD);
                options("Choose an option:", new DialogueOption("What about betraying the Queen of Snow?", key(200)), new DialogueOption("What about all that dark magic?", key(250)));
                player(200, "What about betraying the Queen of Snow?");
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "How do you know about that?", Expression.HIGH_REV_MAD);
                player("I am a ghost, Scourge, I see all your mistakes. Your murky past is laid bare before me.");
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "Well...I...", Expression.HIGH_REV_MAD);
                player("You loved her, Scourge, and you left her. Through selfishness you lost what you wanted most.");
                npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "Now we's getting somewhere.", Expression.HIGH_REV_NORMAL);
                player("You regret that mistake, don't you, Scourge?");
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "Regret? That happened over fifty years ago. I stopped caring for her a long time ago.", Expression.HIGH_REV_WONDERING);
                player("Er, you did?");
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "Indeed. Now let me show you what happens to those who meddle in my affairs.", Expression.HIGH_REV_MAD);
                player(250, "What about all that dark magic?");
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "Ha! My studies into dark magic have made me powerful. I regret nothing!", Expression.HIGH_REV_NORMAL);
                options("Choose an option:", new DialogueOption("What about ruining the Christmas feast?", key(150)), new DialogueOption("What about betraying the Queen of Snow?", key(200)));
            }
        }));
        addActions(30, () -> World.sendObjectAnimation(new WorldObject(46180, 10, 2, instance.getLocation(2470, 5398, 1)), new Animation(15002)), () -> dummy.setInvalidAnimation(new Animation(15002)));
        addActions(41, new DialogueAction(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "He didn't do nuthin'.", Expression.HIGH_REV_NORMAL);
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "'Nuthin\"? Your friends at the feast won't " +
                        "think it's 'nuthin\".", Expression.HIGH_REV_MAD).executeAction(() -> setImpFacingPlayer());
                npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "I's best check on 'em.", Expression.HIGH_REV_NORMAL);
            }
        }));
        addActions(43, () -> {
            imp.finish();
            World.sendGraphics(new Graphics(2508), imp.getLocation());
        });
        final FadeScreen fadeScreen = new FadeScreen(player);
        addActions(45, fadeScreen::fade);
        addActions(47, () -> {
            try {
                final LandOfSnowInstance instance = new LandOfSnowInstance(MapBuilder.findEmptyChunk(4, 4));
                instance.constructRegion();
                player.getCutsceneManager().play(new PastScourgeCutsceneP2(player, instance, () -> fadeScreen.unfade(false)));
            } catch (OutOfSpaceException e) {
                e.printStackTrace();
            }
        });
    }

    private final void setImpFacingPlayer() {
        imp.setFaceLocation(player.getLocation());
    }

    private final void setImpFacingScrouge() {
        imp.setFaceLocation(instance.getLocation(2471, 5400, 1));
    }

    public PastScourgeCutsceneP1(Player player, ScourgeHouseInstance instance, Runnable unfadeRunnable) {
        this.player = player;
        this.instance = instance;
        this.unfadeRunnable = unfadeRunnable;
    }
}
