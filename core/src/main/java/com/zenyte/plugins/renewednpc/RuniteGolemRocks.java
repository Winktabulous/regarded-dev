package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.skills.mining.MiningDefinitions;
import com.zenyte.game.content.skills.mining.actions.Mining;
import com.zenyte.game.content.skills.mining.actions.Prospect;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 8-2-2019 | 20:46
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class RuniteGolemRocks extends NPCPlugin {

    @Override
    public void handle() {
        bind("Mine", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                player.getActionManager().setAction(new Mining(MiningDefinitions.OreDefinitions.RUNITE_GOLEM_ROCKS, npc));
            }

            @Override
            public void execute(final Player player, final NPC npc) {
                player.stopAll();
                player.setFaceEntity(npc);
                handle(player, npc);
            }
        });
        bind("Prospect", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                player.getActionManager().setAction(new Prospect(MiningDefinitions.OreDefinitions.RUNITE_GOLEM_ROCKS));
            }

            @Override
            public void execute(final Player player, final NPC npc) {
                player.stopAll();
                player.setFaceEntity(npc);
                handle(player, npc);
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.ROCKS_6601 };
    }
}
