package com.zenyte.game.content.event.easter2020;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.MinimapState;
import com.zenyte.game.model.ui.GameTab;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
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
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;
import com.zenyte.plugins.SkipPluginScan;

import java.util.Arrays;
import java.util.List;

import static com.zenyte.game.GameInterface.*;

/**
 * @author Kris | 11/04/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@SkipPluginScan
public class RedCarpetCutscene extends Cutscene {
    private final Player player;
    private final RedCarpetInstance instance;
    private final Runnable unfadeRunnable;
    private final NPC dialogueBunny;
    private NPC bunny;
    private static final List<GameInterface> closedTabs = Arrays.asList(COMBAT_TAB, SKILLS_TAB, JOURNAL_HEADER_TAB, INVENTORY_TAB, EQUIPMENT_TAB, PRAYER_TAB_INTERFACE, SPELLBOOK, SETTINGS, EMOTE_TAB, GAME_NOTICEBOARD);

    @Override
    public void build() {
        addActions(0, player::lock, () -> closedTabs.forEach(tab -> player.getInterfaceHandler().closeInterface(tab)), () -> player.getInterfaceHandler().openGameTab(GameTab.FRIENDS_TAB));
        addActions(3, () -> instance.spawnAtDoor(player), () -> player.getVarManager().sendBit(4606, 1), () -> player.getPacketDispatcher().sendClientScript(10700, 1), () -> player.getPacketDispatcher().sendMinimapState(MinimapState.DISABLED));
        addActions(4, new CameraPositionAction(player, instance.getLocation(2199, 4396, 0), 250, -128, 0), new CameraLookAction(player, instance.getLocation(2204, 4396, 0), 200, -128, 0), () -> {
            bunny = new NPC(EasterConstants.EASTER_BUNNY_JR_CUTSCENE, instance.getLocation(2204, 4396, 0), Direction.SOUTH, 0);
            bunny.spawn();
        });
        addActions(5, unfadeRunnable, () -> {
            bunny.setInvalidAnimation(new Animation(15211, 5));
            bunny.setGraphics(new Graphics(2511, 5, 0));
            player.lock();
        });
        addActions(7, new CameraPositionAction(player, instance.getLocation(2204, 4392, 0), 250, 3, 0));
        final FadeScreen fs = new FadeScreen(player);
        addActions(11, fs::fade, () -> bunny.setTransformation(EasterConstants.EASTER_BUNNY_CUTSCENE));
        addActions(13, player::unlock, new CameraResetAction(player), () -> player.getVarManager().sendBit(4606, 0), () -> closedTabs.forEach(tab -> tab.open(player)), () -> player.setLocation(new Location(2208, 4394, 0)), () -> player.getPacketDispatcher().sendClientScript(10700, 0), () -> player.getPacketDispatcher().sendMinimapState(MinimapState.ENABLED), () -> SplittingHeirs.advanceStage(player, Stage.POST_MEETING_CUTSCENE));
        addActions(14, fs::unfade, () -> player.getDialogueManager().start(new DressUpDialogue(player, EasterConstants.EASTER_BUNNY_JR_ON_RED_CARPET)));
    }


    public static final class DressUpDialogue extends Dialogue {
        public DressUpDialogue(Player player, int npcId) {
            super(player, npcId);
        }

        @Override
        public void buildDialogue() {
            player("Whoa...");
            npc("Go tell dad for me, I have work to do!", Expression.EASTER_BUNNY_VERY_HAPPY);
            player("You just...");
            npc("What? Stepped into the old man's spare shoes? Yes, it's a bit tight round the middle, though.", Expression.EASTER_BUNNY_VERY_HAPPY);
            player("It's a SUIT!");
            npc("You should calm down, you look a bit shell-shocked. Go talk to dad.", Expression.EASTER_BUNNY_VERY_HAPPY);
            player("Right... Yes...").executeAction(() -> WorldTasksManager.schedule(() -> player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    player("It was a suit...", Expression.ANXIOUS);
                }
            }), 2));
        }
    }

    public RedCarpetCutscene(Player player, RedCarpetInstance instance, Runnable unfadeRunnable, NPC dialogueBunny) {
        this.player = player;
        this.instance = instance;
        this.unfadeRunnable = unfadeRunnable;
        this.dialogueBunny = dialogueBunny;
    }
}
