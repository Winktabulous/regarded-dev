package com.near_reality.game.content.boss.nex;

import com.zenyte.game.util.ZoneBorders;
import com.zenyte.game.world.entity.Location;

public enum NexDashData {

	WEST(new Location(2915, 5202), new ZoneBorders(2916, 5202, 2925, 5204)),
	NORTH(new Location(2924, 5211), new ZoneBorders(2924, 5204, 2926, 5211)),
	EAST(new Location(2933, 5202), new ZoneBorders(2926, 5202, 2933, 5204)),
	SOUTH(new Location(2924, 5193), new ZoneBorders(2924, 5194, 2926, 5202));

	public static final NexDashData[] values = values();
	private final Location startLocation;
	private final ZoneBorders hitZone;

	NexDashData(Location startLocation, ZoneBorders hitZone) {
		this.startLocation = startLocation;
		this.hitZone = hitZone;
	}

	public Location getStartLocation() {
		return startLocation;
	}

	public ZoneBorders getHitZone() {
		return hitZone;
	}

}
