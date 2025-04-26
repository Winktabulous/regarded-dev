package com.zenyte.game.content.minigame.inferno.model;

import com.zenyte.game.world.entity.Location;

/**
 * An enum containing all of the constant locations used in the Inferno.
 * 
 * @author Kris | 13. apr 2018 : 15:23.35
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum InfernoLocation {

	NORTH_WESTERN(new Location(2258, 5353, 0)),
	NORTH_EASTERN(new Location(2279, 5353, 0)),
	WESTERN_NORTHERN(new Location(2260, 5347, 0)),
	EASTERN_NORTHERN(new Location(2280, 5346, 0)),
	CENTER(new Location(2273, 5341, 0)),
	WESTERN_SOUTHERN(new Location(2262, 5335, 0)),
	SOUTH_WESTERN(new Location(2258, 5330, 0)),
	SOUTH_EASTERN(new Location(2272, 5330, 0)),
	EASTERN_SOUTHERN(new Location(2280, 5333, 0)),
	WESTERN_PILLAR(new Location(2257, 5349, 0)),
	NORTHERN_PILLAR(new Location(2274, 5351, 0)),
	SOUTHERN_PILLAR(new Location(2267, 5335, 0));
	
	public static final InfernoLocation[] values = values();
	
	private final Location tile;
	
	InfernoLocation(final Location tile) {
		this.tile = tile;
	}
	
	public Location getTile() {
	    return tile;
	}
}
