package com.zenyte.game.content.event.halloween2019;

import com.zenyte.ContentConstants;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Kris | 03/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HalloweenCrow extends NPC {
    public static final List<HalloweenCrow> crows = new LinkedList<>();

    public HalloweenCrow(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, 64);
        if (tile == null) {
            return;
        }
        crows.add(this);
    }

    @Override
    public void processNPC() {
        final int delay = randomWalkDelay;
        if (delay > 0) {
            randomWalkDelay--;
        }
        if (delay > 0 || radius <= 0 || ContentConstants.SPAWN_MODE) {
            return;
        }
        if (routeEvent != null || !getWalkSteps().isEmpty()) {
            return;
        }
        if (Utils.random(5) != 0 || isFrozen()) {
            return;
        }
        setRun(false);
        final int moveX = Utils.random(-radius, radius);
        final int moveY = Utils.random(-radius, radius);
        final int respawnX = respawnTile.getX();
        final int respawnY = respawnTile.getY();
        addWalkStepsInteract(respawnX + moveX, respawnY + moveY, radius, getSize(), true);
    }

    @Override
    public boolean isEntityClipped() {
        return false;
    }
}
