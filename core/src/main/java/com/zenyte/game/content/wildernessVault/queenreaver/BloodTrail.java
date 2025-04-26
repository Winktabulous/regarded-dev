package com.zenyte.game.content.wildernessVault.queenreaver;


import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Jire
 * @author Tommeh
 */
public class BloodTrail extends WorldObject {

	private static final int initialTicks = 30;

	private final QueenReaver queen;
	private int ticks = initialTicks;

	public BloodTrail(QueenReaver queen, Location tile) {
		super(32984, DEFAULT_TYPE, Utils.random(3), tile);
		this.queen = queen;
		this.queen.addSplat(getLocation());
		World.spawnObject(this);
	}

	public boolean process() {
		if (!queen.canProcess()) return false;

		if (--ticks == 0) {
			queen.removeSplat(getLocation());
			World.removeObject(this);
			return false;
		}

		return true;
	}

	public void resetTimer() {
		ticks = initialTicks;
	}

}