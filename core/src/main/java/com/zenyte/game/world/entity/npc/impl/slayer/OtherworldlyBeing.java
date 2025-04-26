package com.zenyte.game.world.entity.npc.impl.slayer;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;

/**
 * @author Tommeh | 23-3-2019 | 16:29
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class OtherworldlyBeing extends NPC implements Spawnable {

    public OtherworldlyBeing(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public int getRespawnDelay() {
        return 17;
    }

    @Override
    public boolean validate(int id, String name) {
        return id == 2843;
    }
}
