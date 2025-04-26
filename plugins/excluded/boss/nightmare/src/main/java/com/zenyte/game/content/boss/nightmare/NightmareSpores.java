package com.zenyte.game.content.boss.nightmare;

import com.zenyte.game.world.entity.Location;

public enum NightmareSpores {
	YELLOW(new Location(27, 23), new Location(37, 23), new Location(32, 25), new Location(24, 26),
			new Location(40, 26), new Location(28, 27), new Location(36, 27), new Location(26, 31),
			new Location(32, 31), new Location(38, 31), new Location(28, 35), new Location(36, 35),
			new Location(24, 36), new Location(40, 36), new Location(32, 37), new Location(27, 39),
			new Location(37, 39)),
	RED(new Location(28, 23), new Location(36, 23), new Location(32, 25), new Location(24, 27),
			new Location(40, 27), new Location(29, 28), new Location(35, 28), new Location(26, 31),
			new Location(32, 31), new Location(38, 31), new Location(29, 34), new Location(35, 34),
			new Location(24, 35), new Location(40, 35), new Location(32, 37), new Location(28, 39),
			new Location(36, 39)),
	BLUE(new Location(32, 22), new Location(29, 25), new Location(35, 25), new Location(26, 28),
			new Location(38, 28), new Location(30, 29), new Location(34, 29), new Location(23, 31),
			new Location(41, 31), new Location(30, 33), new Location(34, 33), new Location(26, 34),
			new Location(38, 34), new Location(29, 37), new Location(35, 37), new Location(32 , 40)),
	ORANGE(new Location(32, 23), new Location(27, 26), new Location(37, 26), new Location(32, 28),
			new Location(24, 31), new Location(29, 31), new Location(35, 31), new Location(40, 31),
			new Location(32, 34), new Location(27, 36), new Location(37, 36), new Location(32, 39));

	public static final NightmareSpores[] values = values();
	private final Location[] locations;

	NightmareSpores(Location... locations) {
		this.locations = locations;
	}

	public Location[] getLocations() {
		return locations;
	}

}
