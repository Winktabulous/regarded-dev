package com.zenyte.plugins.renewednpc;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.pathfinding.events.player.EntityEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.DistancedEntityStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.plugins.dialogue.*;

/**
 * @author Kris | 10/03/2019 23:40
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DistancedBanker extends NPCPlugin implements ItemOnNPCAction {

    @Override
    public void handle() {
        bind("Talk-to", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                player.getDialogueManager().start(new BankerD(player, npc));
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new EntityEvent(player, new DistancedEntityStrategy(npc, 1), () -> execute(player, npc), true));
            }
        });
        bind("Bank", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                GameInterface.BANK.open(player);
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new EntityEvent(player, new DistancedEntityStrategy(npc, 1), () -> execute(player, npc), true));
            }
        });
        bind("Collect", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                GameInterface.GRAND_EXCHANGE_COLLECTION_BOX.open(player);
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new EntityEvent(player, new DistancedEntityStrategy(npc, 1), () -> execute(player, npc), true));
            }
        });
        bind("Presets", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                GameInterface.PRESET_MANAGER.open(player);
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new EntityEvent(player, new DistancedEntityStrategy(npc, 1), () -> execute(player, npc), true));
            }
        });
        bind("Last-Preset", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                player.getPresetManager().loadLastPreset();
            }
            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new EntityEvent(player, new DistancedEntityStrategy(npc, 1), () -> execute(player, npc), true));
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.BANKER_2117, NpcId.BANKER_2118, NpcId.MAGNUS_GRAM, NpcId.BANKER_16029, NpcId.BANKER_16030 };
    }

    @Override
    public void handle(final Player player, final Item item, final int slot, final NPC npc) {
        player.setRouteEvent(new EntityEvent(player, new DistancedEntityStrategy(npc, 1), () -> {
            player.stopAll();
            player.faceEntity(npc);
            handleItemOnNPCAction(player, item, slot, npc);
        }, true));
    }

    @Override
    public void handleItemOnNPCAction(final Player player, final Item item, final int slot, final NPC npc) {
        if (item.getId() == 995 || item.getId() == 13204) {
            if (item.getId() == 995 && item.getAmount() < 1000) {
                player.sendMessage("You need at least 1,000 coins to convert the coins into platinum token(s).");
                return;
            }
            player.getDialogueManager().start(new CurrencyConversionD(player, item, slot));
            return;
        }
        if (item.getDefinitions().isNoted()) {
            player.getDialogueManager().start(new UnnoteD(player, item));
        } else {
            if (item.getDefinitions().getNotedId() == -1) {
                player.getDialogueManager().start(new PlainChat(player, "You cannot turn this into banknotes, try another item."));
                return;
            }
            player.getDialogueManager().start(new NoteD(player, item));
        }
    }

    @Override
    public Object[] getItems() {
        return null;
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.OLD_WALL, ObjectId.STAIRCASE_2118, ObjectId.SARCOPHAGUS, 10029, 10030 };
    }
}
