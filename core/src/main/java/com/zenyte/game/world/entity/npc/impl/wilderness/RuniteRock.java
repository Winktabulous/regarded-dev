package com.zenyte.game.world.entity.npc.impl.wilderness;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;

/**
 * @author Tommeh | 8-2-2019 | 20:39
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class RuniteRock extends NPC {

    public RuniteRock(int id, Location tile, boolean spawned) {
        super(id, tile, spawned);
    }

    @Override
    public NPC spawn() {
        WorldTasksManager.schedule(() -> {
            if (!isFinished()) {
                finish();
            }
        }, 99);
        return super.spawn();
    }

    @Override
    public void onFinish(final Entity source) { }
}
