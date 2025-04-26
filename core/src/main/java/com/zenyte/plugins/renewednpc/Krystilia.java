package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.skills.slayer.dialogue.KrystiliaAssignmentD;
import com.zenyte.game.content.skills.slayer.dialogue.KrystiliaD;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.pathfinding.events.player.EntityEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.DistancedEntityStrategy;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 26/11/2018 17:38
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Krystilia extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                player.getDialogueManager().start(new KrystiliaD(player, npc));
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new EntityEvent(player, new DistancedEntityStrategy(npc, 1), () -> execute(player, npc), true));
            }
        });
        bind("Assignment", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                player.getDialogueManager().start(new KrystiliaAssignmentD(player, npc));
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new EntityEvent(player, new DistancedEntityStrategy(npc, 1), () -> execute(player, npc), true));
            }
        });
        bind("Trade", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                player.openShop("Slayer Equipment");
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new EntityEvent(player, new DistancedEntityStrategy(npc, 1), () -> execute(player, npc), true));
            }
        });
        bind("Rewards", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                player.getSlayer().openInterface();
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new EntityEvent(player, new DistancedEntityStrategy(npc, 1), () -> execute(player, npc), true));
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.KRYSTILIA };
    }
}
