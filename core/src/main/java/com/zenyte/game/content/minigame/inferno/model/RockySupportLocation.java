package com.zenyte.game.content.minigame.inferno.model;

import com.zenyte.game.world.entity.Location;

/**
 * @author Tommeh | 26/11/2019 | 19:16
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public enum RockySupportLocation {

    NORTH(30354, new Location(2274, 5351, 0)),
    SOUTH(30355, new Location(2267, 5335, 0)),
    WEST(30353, new Location(2257, 5349, 0));

    private final int id;
    private final Location location;

    public static final RockySupportLocation[] values = values();
    
    RockySupportLocation(int id, Location location) {
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
