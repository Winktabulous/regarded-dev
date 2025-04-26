package com.zenyte.game.world.entity.npc.impl.wilderness;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;

/**
 * @author Tommeh | 8-2-2019 | 20:39
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class RuniteGolem extends NPC implements Spawnable {
    public RuniteGolem(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public void onFinish(final Entity source) {
        super.onFinish(source);
        final RuniteRock rock = new RuniteRock(getId() + 1, getLocation(), true);
        rock.lock();
        rock.spawn();
    }

    @Override
    public int getRespawnDelay() {
        return 500;
    }

    @Override
    public boolean validate(int id, String name) {
        return id == 6600;
    }
}
