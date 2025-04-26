package com.zenyte.game.content.minigame.pestcontrol.npc;

import com.zenyte.game.content.minigame.pestcontrol.PestControlInstance;
import com.zenyte.game.content.minigame.pestcontrol.PestNPC;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.pathfinding.events.npc.NPCObjectEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.ObjectStrategy;
import com.zenyte.game.world.object.WorldObject;

import java.util.List;

/**
 * @author Kris | 27. juuni 2018 : 22:14:47
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class RavagerNPC extends PestNPC {
	private static final int LOCATING = 0;
	private static final int RAVAGING = 1;
	private static final int IN_COMBAT = 2;
	private static final Animation RAVAGE = new Animation(3915);

	public RavagerNPC(final PestControlInstance instance, final PestPortalNPC portal, final int id, final Location tile) {
		super(instance, portal, id, tile);
	}

	private WorldObject trackedObject;
	private int state;
	private int delay;

	@Override
	public boolean isEntityClipped() {
		return false;
	}

	@Override
	public void processNPC() {
		if (delay > 0) {
			delay--;
			return;
		}
		if (state == IN_COMBAT) {
			super.processNPC();
			return;
		}
		if (state == RAVAGING && trackedObject != null) {
			if (!World.containsObjectWithId(trackedObject, trackedObject.getId())) {
				trackedObject = null;
				state = LOCATING;
				return;
			}
			faceObject(trackedObject);
			setAnimation(RAVAGE);
			trackedObject = instance.destroyRavagableObject(trackedObject);
			final int id = trackedObject.getId();
			if (id < 14233 && id >= 14227 || id >= 14245) {
				state = LOCATING;
				trackedObject = null;
				delay = 1;
				return;
			}
			delay = 2;
			return;
		}
		if (trackedObject == null) {
			final List<WorldObject> ravagableObjects = instance.getRavagableObjects();
			if (ravagableObjects.isEmpty()) {
				state = IN_COMBAT;
				resetWalkSteps();
				return;
			}
			final int x = getX();
			final int y = getY();
			ravagableObjects.sort((final WorldObject o1, final WorldObject o2) -> o1.getDistance(x, y) > o2.getDistance(x, y) ? 1 : -1);
			trackedObject = ravagableObjects.get(0);
			setRouteEvent(new NPCObjectEvent(this, new ObjectStrategy(trackedObject), () -> state = RAVAGING));
		} else if (!hasWalkSteps()) {
			trackedObject = null;
		}
	}

	@Override
	public void setFaceEntity(final Entity entity) {
		if (entity != null && state != IN_COMBAT) {
			return;
		}
		super.setFaceEntity(entity);
	}
}
