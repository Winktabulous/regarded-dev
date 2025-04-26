package com.zenyte.game.content.wildernessVault.queenreaver;

import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;

public class BloodSpawn extends NPC {

	private final QueenReaver maiden;
	private final Location spawnLocation;
	
	public BloodSpawn(QueenReaver maiden, Location spawnLocation) {
		super(NpcId.BLOOD_SPAWN, spawnLocation, Direction.SOUTH, 5);
		this.maiden = maiden;
		this.spawnLocation = spawnLocation;
		this.radius = 10;
		this.spawned = true;
		this.supplyCache = false;
	}

    @Override
    public void processNPC() {
        if (!maiden.canProcess()) return;

        Location lastTile = lastLocation == null ? location : lastLocation;
        addTrail(lastTile);
        if (shouldNotWalk())
            addTrail(location);

        // custom walk behaviour
        if (!isFrozen() && Utils.random(2) == 0) {
            int moveX = Utils.random(-radius, radius);
            int moveY = Utils.random(-radius, radius);
            int respawnX = spawnLocation.getX();
            int respawnY = spawnLocation.getY();
            addWalkStepsInteract(respawnX + moveX, respawnY + moveY, radius, getSize(), true);
        }
    }

	private void addTrail(Location tile) {
		tile = tile.copy();
		if (maiden.splatExists(tile)) {
			for (BloodTrail trail : maiden.bloodTrails) {
				if (trail.getPositionHash() == tile.getPositionHash()) {
					trail.resetTimer();
					break;
				}
			}
		} else {
			maiden.addSplat(tile);
			BloodTrail t = new BloodTrail(maiden, tile);
			maiden.bloodTrails.add(t);
		}
	}

    private boolean shouldNotWalk() {
        return isFrozen() || isStunned();
    }


    @Override
    public void autoRetaliate(Entity source) {

    }

}