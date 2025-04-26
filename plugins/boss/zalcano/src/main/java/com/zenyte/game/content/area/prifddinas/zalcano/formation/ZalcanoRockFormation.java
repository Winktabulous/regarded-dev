package com.zenyte.game.content.area.prifddinas.zalcano.formation;

import com.zenyte.game.world.Position;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.object.WorldObject;

/**
 * The
 */
public class ZalcanoRockFormation extends WorldObject {

    private final ZalcanoRockFormations formation;

    public ZalcanoRockFormation(ZalcanoRockFormations formation, int rotation, Location tile) {
        super(formation.getObjectId(), 10, rotation, tile);
        this.formation = formation;
    }

    public ZalcanoRockFormation(int rotation, Location tile) {
        this(ZalcanoRockFormations.DEPLETED, rotation, tile);
    }

    public ZalcanoRockFormations getFormation() {
        return formation;
    }

    public Location getMiddleLocation() {
        return new Location(getX() + 1, getY() + 1);
    }

    @Override
    public boolean matches(Position other) {
        return super.matches(other);
    }
}
