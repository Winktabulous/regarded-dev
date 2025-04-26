package com.zenyte.game.world.object;

import com.zenyte.game.world.entity.Location;

public final class LadderObject {
	
	private final int id;
	private final Location location;
	
	public LadderObject(final int id, final Location location) {
		this.id = id;
		this.location = location;
	}
	
	public int getId() {
	    return id;
	}
	
	public Location getLocation() {
	    return location;
	}

}