package com.zenyte.game.content.skills.mining;

import com.zenyte.game.world.entity.Location;

public enum DaeyaltEssence {

    ONE(new Location(3671, 9750, 2), 0),
    TWO(new Location(3674, 9765, 2), 1),
    THREE(new Location(3687, 9755, 2), 2),

    ;

    DaeyaltEssence(Location location, int face) {
        this.location = location;
        this.face = face;
    }

    private Location location;
    private int face;

    public Location getLocation() {
        return location;
    }

    public int getFace() {
        return face;
    }

    public static final DaeyaltEssence[] VALUES = values();

    public static final int ESSENSE_INACTIVE = 39094;
    public static final int ESSENSE = 39095;

}
