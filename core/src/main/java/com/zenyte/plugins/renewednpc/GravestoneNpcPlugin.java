package com.zenyte.plugins.renewednpc;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.gravestone.GravestoneExt;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.pathfinding.events.player.EntityEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.DistancedEntityStrategy;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 13/06/2022
 */
public class GravestoneNpcPlugin extends NPCPlugin {
    @Override
    public void handle() {
        bind("Check", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                GameInterface.GRAVESTONE_RECLAIM.open(player);
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new EntityEvent(player, new DistancedEntityStrategy(npc, 7), () -> execute(player, npc), true));
            }

            @Override
            public void execute(Player player, NPC npc) {
                player.stopAll();
                player.setFaceEntity(npc);
                handle(player, npc);
            }
        });

        bind("Loot", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                GravestoneExt.INSTANCE.reclaimGravestoneItems(player);
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new EntityEvent(player, new DistancedEntityStrategy(npc, 7), () -> execute(player, npc), true));
            }

            @Override
            public void execute(Player player, NPC npc) {
                player.stopAll();
                player.setFaceEntity(npc);
                handle(player, npc);
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { 9856, 9857 };
    }
}
