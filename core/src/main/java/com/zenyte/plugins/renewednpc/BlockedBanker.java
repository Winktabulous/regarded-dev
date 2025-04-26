package com.zenyte.plugins.renewednpc;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.pathfinding.events.RouteEvent;
import com.zenyte.game.world.entity.pathfinding.events.player.EntityEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.EntityStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.plugins.dialogue.*;

/**
 * @author Kris | 26/11/2018 19:29
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BlockedBanker extends Banker implements ItemOnNPCAction {

    @Override
    public void handle() {
        bind("Talk-to", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                player.getDialogueManager().start(new BankerD(player, npc));
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new EntityEvent(player, new EntityStrategy(npc, 1, getAccessFlag(npc)), () -> execute(player, npc), false));
            }
        });
        bind("Bank", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                GameInterface.BANK.open(player);
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new EntityEvent(player, new EntityStrategy(npc, 1, getAccessFlag(npc)), () -> execute(player, npc), false));
            }
        });
        bind("Collect", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                GameInterface.GRAND_EXCHANGE_COLLECTION_BOX.open(player);
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new EntityEvent(player, new EntityStrategy(npc, 1, getAccessFlag(npc)), () -> execute(player, npc), false));
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] {
                NpcId.BANKER_1618, NpcId.BANKER_3090, NpcId.BANKER_3091, NpcId.BANKER_2633, 1763,
                NpcId.SQUIRE_1764, NpcId.BANKER_2897, NpcId.BANKER_2898, 3227, 6520,
                6523, NpcId.JADE, 6560, NpcId.SIRSAL_BANKER, NpcId.BANKER, NpcId.BANKER_1634,
                NpcId.BANKER_3089, NpcId.BANKER_1633, 6528, NpcId.BANKER_1613, NpcId.BANKER_8590, NpcId.BANKER_8589,
                1479, 1480
        };
    }

    private int getAccessFlag(final NPC npc) {
        final Direction spawnDir = npc.getSpawnDirection();
        if (spawnDir == null) {
            return 0;
        }
        switch(spawnDir) {
            case NORTH:
            case NORTH_EAST:
                return RouteEvent.NORTH_EXIT;
            case EAST:
            case SOUTH_EAST:
                return RouteEvent.EAST_EXIT;
            case SOUTH:
            case SOUTH_WEST:
                return RouteEvent.SOUTH_EXIT;
            case WEST:
            case NORTH_WEST:
                return RouteEvent.WEST_EXIT;
            default:
                return 0;
        }
    }

    @Override
    public void handle(final Player player, final Item item, final int slot, final NPC npc) {
        player.setRouteEvent(new EntityEvent(player, new EntityStrategy(npc, 1, getAccessFlag(npc)), () -> {
            player.stopAll();
            player.faceEntity(npc);
            handleItemOnNPCAction(player, item, slot, npc);
        }, false));
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
        return new Object[] { ObjectId.POT, ObjectId.POTS, ObjectId.CABINET_397, ObjectId.CHEST_2633, 1763, ObjectId.DUMMY_1764, ObjectId.LEGENDS_GUILD_DOOR_2897, 2898, ObjectId.TRAP, 6520, ObjectId.SMALL_GRAVESTONE, ObjectId.LADDER_6523, ObjectId.TRAWLER_NET_2472, 6560, ObjectId.COMPOST_BIN_3843, 766, 5455, 5456, ObjectId.DOOR_5453, 6528, ObjectId.BOOKCASE_394, ObjectId.DISEASED_ONIONS_8590, ObjectId.DISEASED_ONIONS, 1479, 1480, ObjectId.PITFALL };
    }
}
