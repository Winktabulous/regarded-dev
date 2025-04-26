package com.zenyte.plugins.renewednpc;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.pathfinding.events.player.EntityEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.DistancedEntityStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.GrandExchangeClerkD;

import static com.zenyte.game.GameInterface.GRAND_EXCHANGE_HISTORY;
import static com.zenyte.game.GameInterface.GRAND_EXCHANGE_OFFERS_VIEWER;

/**
 * @author Kris | 11/03/2019 20:48
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class GrandExchangeClerk extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                player.getDialogueManager().start(new GrandExchangeClerkD(player, npc));
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new EntityEvent(player, new DistancedEntityStrategy(npc, 1), () -> execute(player, npc), true));
            }
        });
        bind("Exchange", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                player.getGrandExchange().openOffersInterface();
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new EntityEvent(player, new DistancedEntityStrategy(npc, 1), () -> execute(player, npc), true));
            }
        });
        bind("History", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                GRAND_EXCHANGE_HISTORY.open(player);
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new EntityEvent(player, new DistancedEntityStrategy(npc, 1), () -> execute(player, npc), true));
            }
        });
        bind("Offers Viewer", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                GRAND_EXCHANGE_OFFERS_VIEWER.open(player);
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new EntityEvent(player, new DistancedEntityStrategy(npc, 1), () -> execute(player, npc), true));
            }
        });
        bind("Sets", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                player.getGrandExchange().openItemSetsInterface();
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new EntityEvent(player, new DistancedEntityStrategy(npc, 1), () -> execute(player, npc), true));
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.GRAND_EXCHANGE_CLERK, NpcId.GRAND_EXCHANGE_CLERK_2149, NpcId.GRAND_EXCHANGE_CLERK_2150, NpcId.GRAND_EXCHANGE_CLERK_2151 };
    }
}
