package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.chambersofxeric.greatolm.GreatOlm;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Christopher
 * @since 3/18/2020
 */
public class DouseOlmFireWall extends NPCPlugin {
    @Override
    public void handle() {
        bind("Douse", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                GreatOlm.douseFirewall(player, npc);
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                execute(player, npc);
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[]{NpcId.FIRE};
    }
}
