package com.zenyte.game.content.event.christmas2019.cutscenes;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.event.christmas2019.AChristmasWarble;
import com.zenyte.game.content.event.christmas2019.ChristmasConstants;
import com.zenyte.game.content.event.christmas2019.ChristmasUtils;
import com.zenyte.game.model.MinimapState;
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

import java.util.Arrays;
import java.util.List;

import static com.zenyte.game.GameInterface.*;

/**
 * @author Kris | 18/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PastScourgeCutsceneP3 extends Cutscene {
    private final Player player;
    private final ScourgeHouseInstance instance;
    private final Runnable unfadeRunnable;
    private CutsceneImp imp;
    private NPC dummy;
    private static final List<GameInterface> closedTabs = Arrays.asList(COMBAT_TAB, SKILLS_TAB, JOURNAL_HEADER_TAB, INVENTORY_TAB, EQUIPMENT_TAB, PRAYER_TAB_INTERFACE, SPELLBOOK, SETTINGS, EMOTE_TAB, GAME_NOTICEBOARD);

    @Override
    public void build() {
        final String impName = ChristmasUtils.getImpName(player);
        addActions(0, () -> player.setLocation(instance.getLocation(2471, 5403, 1)), () -> player.getVarManager().sendBit(4606, 1));
        addActions(1, new CameraPositionAction(player, instance.getLocation(2476, 5405, 1), 500, 255, 100), new CameraLookAction(player, instance.getLocation(2465, 5396, 1), 0, 255, 100), () -> {
            dummy = new NPC(24, instance.getLocation(2470, 5398, 1), Direction.NORTH, 0);
            dummy.spawn();
            player.setFaceLocation(instance.getLocation(2472, 5402, 1));
        }, () -> World.sendObjectAnimation(new WorldObject(46180, 10, 2, instance.getLocation(2470, 5398, 1)), new Animation(15000)));
        addActions(2, unfadeRunnable);
        addActions(4, () -> {
            imp = new CutsceneImp(instance.getLocation(2472, 5402, 1));
            imp.spawn();
            imp.setFaceLocation(player.getLocation());
            imp.setGraphics(new Graphics(2508));
        });
        addActions(7, new DialogueAction(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "He's trapped the Queen of Snow's mates in " +
                        "magical Christmas-y fires!", Expression.HIGH_REV_NORMAL);
                player("We have to go and help.");
                npcWithId(ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID, "And let that teach you busybody, interfering, Christmas-loving fools to stay out of my business!", Expression.HIGH_REV_MAD).executeAction(() -> AChristmasWarble.progress(player, AChristmasWarble.ChristmasWarbleProgress.FROZEN_GUESTS));
            }
        }));
        addActions(8, new CameraPositionAction(player, instance.getLocation(2471, 5400, 1), 550, 255, 100), new CameraLookAction(player, instance.getLocation(2471, 5403, 1), 75, 255, 100));
        final FadeScreen fadeScreen = new FadeScreen(player);
        addActions(9, () -> {
            player.setRunSilent(3);
            player.addWalkSteps(instance.getX(2471), instance.getY(5406), 3);
        });
        addActions(11, fadeScreen::fade, () -> {
            imp.setFaceEntity(player);
            imp.addWalkSteps(instance.getX(2471), instance.getY(5406), 3);
        });
        addActions(13, player::unlock, new CameraResetAction(player), () -> player.getVarManager().sendBit(4606, 0), () -> closedTabs.forEach(tab -> tab.open(player)), () -> player.setLocation(new Location(2465, 5403, 0)), () -> player.getPacketDispatcher().sendClientScript(10700, 0), fadeScreen::unfade, () -> player.getPacketDispatcher().sendMinimapState(MinimapState.ENABLED));
    }

    public PastScourgeCutsceneP3(Player player, ScourgeHouseInstance instance, Runnable unfadeRunnable) {
        this.player = player;
        this.instance = instance;
        this.unfadeRunnable = unfadeRunnable;
    }
}
