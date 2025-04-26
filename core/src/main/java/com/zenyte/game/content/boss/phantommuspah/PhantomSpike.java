package com.zenyte.game.content.boss.phantommuspah;

import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Savions.
 */
public class PhantomSpike extends WorldObject {

	private static final Graphics UP_GFX_ID = new Graphics(2326);
	private static final Graphics DOWN_GFX_ID = new Graphics(2325);
	static final int DOWN_OBJECT_ID = 46694;
	private static final int UP_OBJECT_ID = 46697;
	private final PhantomInstance instance;
	private int moveTicks = 100;
	private Location nextLocation;
	private boolean moving;

	public PhantomSpike(PhantomInstance instance, int id, int rotation, Location location) {
		super(id, 10, rotation, location);
		this.instance = instance;
		nextLocation = new Location(this);
	}

	//TODO rotation moving

	public boolean move() {
		if (moveTicks <= 0) {
			setId(PhantomInstance.SPIKE_OBJECT_ID);
			World.spawnObject(this);
			return false;
		}
		if (moveTicks % 2 == 0) {
			setId(DOWN_OBJECT_ID);
			World.spawnObject(this);
			World.sendGraphics(DOWN_GFX_ID, this);
			nextLocation = fetchNextLocation();
		} else {
			World.removeObject(new WorldObject(this));
			setId(UP_OBJECT_ID);
			final int dx = nextLocation.getX() - getX();
			final int dy = nextLocation.getY() - getY();
			if (dx == -1) {
				setRotation(0);
			} else if (dx == 1) {
				setRotation(1);
			} else if (dy == -1) {
				setRotation(2);
			} else if (dy == 1) {
				setRotation(3);
			}
			if (!instance.isSpikeTileBlocked(nextLocation)) {
				setLocation(nextLocation);
			}
			World.sendGraphics(UP_GFX_ID, this);
			World.spawnObject(this);
			if (instance.getPlayer().getLocation().equals(this)) {
				instance.damagePlayerBySpike(this);
			}
		}
		moveTicks--;
		return true;
	}

	public void setOffTick() {
		moveTicks++;
	}

	final Location fetchNextLocation() {
		Location location;
		int dx = 0;
		int dy = 0;
		final Location playerLoc = instance.getPlayer().getLocation();
		if (playerLoc.getX() < getX()) {
			dx = -1;
		} else if (playerLoc.getX() > getX()) {
			dx = 1;
		}
		if (playerLoc.getY() < getY()) {
			dy = -1;
		} else if (playerLoc.getY() > getY()) {
			dy = 1;
		}
		if (dy != 0 && dx != 0) {
			final boolean prioritizeX = Math.abs(playerLoc.getX() - getX()) >= Math.abs(playerLoc.getY() - getY());
			location = prioritizeX ? transform(dx, 0) : transform(0, dy);
			if (instance.isSpikeTileBlocked(location)) {
				location = prioritizeX ? transform(0, dy) : transform(dx, 0);
				if (instance.isSpikeTileBlocked(location)) {
					location = transform(0, 0);
				}
			}
		} else {
			location = transform(dx, dy);
			if (instance.isSpikeTileBlocked(location)) {
				location = transform(0, 0);
			}
		}
		return location;
	}

	boolean isMoving() {
		return moving && moveTicks > 0;
	}

	void setMoving() { moving = true; }
}
