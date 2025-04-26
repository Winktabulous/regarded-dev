package com.zenyte.game.content.skills.hunter.aerialfishing.npc;

import com.zenyte.game.content.skills.hunter.aerialfishing.LakeMolchArea;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;

/**
 * @author Cresinkel
 */

public class FishingSpotNpc extends NPC {

    public FishingSpotNpc(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean isMovementRestricted() {
        return true;
    }

    @Override
    public NPC spawn() {
        NPC npc = super.spawn();
        WorldTasksManager.schedule(() -> {
            if (isDead() || isFinished()) {
                return;
            }
            LakeMolchArea.removeUsedSpawnLocation(getLocation());
            remove();
            LakeMolchArea.spawnSpot();
        }, Utils.random(10,30));
        return npc;
    }
}
