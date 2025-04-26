package com.zenyte.game.content.tombsofamascut.npc;

import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;

/**
 * @author Savions.
 */
public class Osmumten extends NPC {

	private static final Animation SPAWN_ANIM = new Animation(9795);

	public Osmumten(int id, Location tile, Direction facing) {
		super(id, tile, facing, 0);
		setAnimation(SPAWN_ANIM);
	}

	@Override public boolean addWalkStep(int nextX, int nextY, int lastX, int lastY, boolean check) { return false; }

	@Override public void setTarget(Entity target, TargetSwitchCause cause) { }
}
