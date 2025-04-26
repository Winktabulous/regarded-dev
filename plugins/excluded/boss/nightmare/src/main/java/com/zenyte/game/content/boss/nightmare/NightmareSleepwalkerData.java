package com.zenyte.game.content.boss.nightmare;

import com.zenyte.game.world.entity.Location;

public enum NightmareSleepwalkerData {

	WALKER1(new Location(3866, 9959, 3)),
	WALKER2(new Location(3867, 9959, 3)),
	WALKER3(new Location(3868, 9959, 3)),

	WALKER4(new Location(3878, 9959, 3)),
	WALKER5(new Location(3877, 9959, 3)),
	WALKER6(new Location(3876, 9959, 3)),

	WALKER7(new Location(3881, 9956, 3)),
	WALKER8(new Location(3881, 9955, 3)),
	WALKER9(new Location(3881, 9954, 3)),

	WALKER10(new Location(3881, 9946, 3)),
	WALKER11(new Location(3881, 9945, 3)),
	WALKER12(new Location(3881, 9944, 3)),

	WALKER13(new Location(3878, 9941, 3)),
	WALKER14(new Location(3877, 9941, 3)),
	WALKER15(new Location(3876, 9941, 3)),

	WALKER16(new Location(3866, 9941, 3)),
	WALKER17(new Location(3867, 9941, 3)),
	WALKER18(new Location(3868, 9941, 3)),

	WALKER19(new Location(3863, 9944, 3)),
	WALKER20(new Location(3863, 9945, 3)),
	WALKER21(new Location(3863, 9946, 3)),

	WALKER22(new Location(3863, 9956, 3)),
	WALKER23(new Location(3863, 9955, 3)),
	WALKER24(new Location(3863, 9954, 3));

	public static NightmareSleepwalkerData[] values = values();
	private final Location location;

	NightmareSleepwalkerData(Location location) {
		this.location = location;
	}

	public Location getLocation() {
		return location;
	}

}
