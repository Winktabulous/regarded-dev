package com.zenyte.game.world.entity.npc.impl.slayer;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;

/**
 * @author Kris | 28/05/2019 19:51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ChaoticDeathSpawn extends NPC implements Spawnable {
    public ChaoticDeathSpawn(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    private int ticks;

    @Override
    public void processNPC() {
        if (++ticks == 100) {
            finish();
            return;
        }
        super.processNPC();
    }


    @Override
    public boolean validate(final int id, final String name) {
        return id == 6716 || id == 6723 || id == 7649;
    }
}
