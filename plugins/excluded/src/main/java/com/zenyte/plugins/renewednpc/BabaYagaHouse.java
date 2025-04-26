package com.zenyte.plugins.renewednpc;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;

/**
 * @author Kris | 26/11/2018 19:47
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BabaYagaHouse extends NPCPlugin {

    @Override
    public void handle() {
        bind("Go-inside", (player, npc) -> player.setLocation(new Location(2451, 4645, 0)));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.HOUSE };
    }
}
