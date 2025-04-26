package com.zenyte.game.world.object;

import com.zenyte.game.world.entity.Location;

import java.util.HashMap;
import java.util.Map;

public enum Rope {

	ZEAH_CATACOMBS_CENTER(28894, new Location(1639, 3673, 0)),
	SMOKE_DUNGEON_LEAVE(6439, new Location(3311, 2962, 0));
	
	private final int id;
	private final Location tile;
	
	public static final Rope[] VALUES = values();
	public static final Map<Integer, Rope> ROPES = new HashMap<Integer, Rope>();
	
	Rope(final int id, final Location tile) {
		this.id = id;
		this.tile = tile;
	}
	
	static {
		for(Rope rope : VALUES)
			ROPES.put(rope.getId(), rope);
	}
	
	public int getId() {
	    return id;
	}
	
	public Location getTile() {
	    return tile;
	}

}
