package com.zenyte.game.content.boss.nightmare;

import com.zenyte.game.world.entity.Location;

public enum NightmareChargeData {

	FIRST(new Location(3877, 9953, 3), new Location(3863, 9953, 3),
			new Location(3863, 9954, 3), new Location(3881, 9957, 3)),
	SECOND(new Location(3870, 9957, 3), new Location(3870, 9941, 3),
			new Location(3870, 9941, 3), new Location(3874, 9960, 3)),
	THIRD(new Location(3870, 9941, 3), new Location(3870, 9957, 3),
			new Location(3870, 9941, 3), new Location(3874, 9960, 3)),
	FOURTH(new Location(3863, 9945, 3), new Location(3877, 9945, 3),
			new Location(3863, 9945, 3), new Location(3881, 9948, 3));

	public static NightmareChargeData[] values = values();
	private final Location npcStart, npcEnd, checkStart, checkEnd;

	NightmareChargeData(Location npcStart, Location end, Location checkStart, Location checkEnd) {
		this.npcStart = npcStart;
		this.npcEnd = end;
		this.checkStart = checkStart;
		this.checkEnd = checkEnd;
	}

	public Location getNpcStart() {
		return npcStart;
	}

	public Location getNpcEnd() {
		return npcEnd;
	}

	public Location getCheckStart() {
		return checkStart;
	}

	public Location getCheckEnd() {
		return checkEnd;
	}

}
