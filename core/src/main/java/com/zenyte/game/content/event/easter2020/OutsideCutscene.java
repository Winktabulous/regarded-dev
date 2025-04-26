package com.zenyte.game.content.event.easter2020;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.MinimapState;
import com.zenyte.game.model.ui.GameTab;
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
public class OutsideCutscene extends Cutscene {
    private final Player player;
    private final OutsideInstance instance;
    private final Runnable unfadeRunnable;
    private NPC bunny;
    private static final List<GameInterface> closedTabs = Arrays.asList(COMBAT_TAB, SKILLS_TAB, JOURNAL_HEADER_TAB, INVENTORY_TAB, EQUIPMENT_TAB, PRAYER_TAB_INTERFACE, SPELLBOOK, SETTINGS, EMOTE_TAB, GAME_NOTICEBOARD);

    @Override
    public void build() {
        addActions(0, player::lock, () -> closedTabs.forEach(tab -> player.getInterfaceHandler().closeInterface(tab)), () -> player.getInterfaceHandler().openGameTab(GameTab.FRIENDS_TAB));
        addActions(3, () -> instance.spawnAtDoor(player), () -> player.getVarManager().sendBit(4606, 1), () -> player.getPacketDispatcher().sendClientScript(10700, 1), () -> player.getPacketDispatcher().sendMinimapState(MinimapState.DISABLED));
        addActions(4, new CameraPositionAction(player, instance.getLocation(3090 - 2, 3475, 0), 350, -128, 0), new CameraLookAction(player, instance.getLocation(3093, 3471, 0), 200, -128, 0), () -> {
            bunny = new NPC(EasterConstants.EASTER_BUNNY_OUTSIDE_CUTSCENE, instance.getLocation(3093, 3471, 0), Direction.WEST, 0);
            bunny.spawn();
        });
        addActions(5, unfadeRunnable, () -> {
            bunny.setInvalidAnimation(new Animation(15213, 5));
            bunny.setGraphics(new Graphics(2512, 5, 0));
            player.lock();
        });
        final FadeScreen fs = new FadeScreen(player);
        addActions(11, fs::fade);
        addActions(13, player::unlock, new CameraResetAction(player), () -> player.getVarManager().sendBit(4606, 0), () -> closedTabs.forEach(tab -> tab.open(player)), () -> player.setLocation(new Location(3088, 3472, 0)), () -> player.getPacketDispatcher().sendClientScript(10700, 0), () -> player.getPacketDispatcher().sendMinimapState(MinimapState.ENABLED), () -> SplittingHeirs.advanceStage(player, Stage.POST_UNDRESS_CUTSCENE));
        addActions(14, fs::unfade);
    }


    public static final class UndressDialogue extends Dialogue {
        public UndressDialogue(Player player, int npcId) {
            super(player, npcId);
        }

        @Override
        public void buildDialogue() {
            npc("Ahh...that's better, much less sticky. Now, as you've helped me so much, I should reward you. You've" +
                    " been very helpful.", Expression.EASTER_BUNNY_VERY_HAPPY);
            player("Aww, it was nothing, really.");
            npc("Oh? So you don't want your reward?", Expression.EASTER_BUNNY_VERY_HAPPY);
            player("I-");
            npc("Well, I guess it's very much a carrot and stick situation: you've dealt with the sticky, now you get" +
                    " your carrot.", Expression.EASTER_BUNNY_VERY_HAPPY).executeAction(() -> {
                player.getInventory().addOrDrop(new Item(EasterConstants.EasterItem.EASTER_CARROT.getItemId()));
                player.getInventory().addOrDrop(new Item(EasterConstants.EasterItem.CHOCATRICE_CAPE.getItemId()));
                player.getTrackedHolidayItems().add(EasterConstants.EasterItem.EASTER_CARROT.getItemId());
                player.getTrackedHolidayItems().add(EasterConstants.EasterItem.CHOCATRICE_CAPE.getItemId());
                SplittingHeirs.advanceStage(player, Stage.EVENT_COMPLETE);
            });
            doubleItem(new Item(EasterConstants.EasterItem.EASTER_CARROT.getItemId()), new Item(EasterConstants.EasterItem.CHOCATRICE_CAPE.getItemId()), "The Easter Bunny hands you a large carrot along with an easter cape.");
            npc("Here, take mine. I'm off to a nice warm island with no hint of chocolate in sight!", Expression.EASTER_BUNNY_VERY_HAPPY).executeAction(() -> {
                player.getTemporaryAttributes().put("quest completed title", "You have completed Splitting Heirs.");
                player.getTemporaryAttributes().put("quest completed item", new Item(EasterConstants.EasterItem.EASTER_CARROT.getItemId()));
                player.getTemporaryAttributes().put("quest completed rewards", Arrays.asList("Easter Carrot", "Chocatrice Cape", "Around the World in Eggty Days emote", "Rabbit Hop Emote"));
                GameInterface.QUEST_COMPLETED.open(player);
                player.sendMessage("Congratulations! You have completed Splitting Heirs.");
            });
        }
    }

    public OutsideCutscene(Player player, OutsideInstance instance, Runnable unfadeRunnable) {
        this.player = player;
        this.instance = instance;
        this.unfadeRunnable = unfadeRunnable;
    }
}
