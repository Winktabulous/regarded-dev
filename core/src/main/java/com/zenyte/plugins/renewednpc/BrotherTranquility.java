package com.zenyte.plugins.renewednpc;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;

/**
 * @author Kris | 30/07/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BrotherTranquility extends NPCPlugin {
    @Override
    public void handle() {
        bind("Transport", (player, npc) -> new FadeScreen(player, () -> player.setLocation(new Location(3797, 2869, 0))).fade(3));
    }

    @Override
    public int[] getNPCs() {
        return new int[] {
                NpcId.BROTHER_TRANQUILITY, NpcId.BROTHER_TRANQUILITY_551, NpcId.BROTHER_TRANQUILITY_552, 1953
        };
    }
}
