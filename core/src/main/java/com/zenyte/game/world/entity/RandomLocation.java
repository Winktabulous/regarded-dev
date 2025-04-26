package com.zenyte.game.world.entity;

import com.zenyte.game.util.Utils;

public class RandomLocation {

    public static Location create(Location loc, int randomize) {
        return new Location(
                loc.getX() + (Utils.random(1) == 0 ? Utils.random(0, -randomize) : Utils.random(randomize)),
                loc.getY() + (Utils.random(1) == 0 ? Utils.random(0, -randomize) : Utils.random(randomize)),
                loc.getPlane()
        );
    }

    public static Location random(Location location, final int radius) {
        final int xOff = Utils.random(-radius, radius);
        final int yOff = Utils.random(-radius, radius);
        return new Location(location.getX() + xOff, location.getY() + yOff, location.getPlane());
    }
}
