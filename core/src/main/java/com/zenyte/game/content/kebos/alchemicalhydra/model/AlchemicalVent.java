package com.zenyte.game.content.kebos.alchemicalhydra.model;

import com.zenyte.game.content.kebos.alchemicalhydra.HydraPhase;
import com.zenyte.game.world.entity.Location;

import static com.zenyte.game.content.kebos.alchemicalhydra.HydraPhase.*;

/**
 * @author Tommeh | 04/11/2019 | 18:35
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public enum AlchemicalVent {

    RED(34568, new Location(1371, 10263, 0), POISON, FLAME),
    GREEN(34569, new Location(1371, 10272, 0), LIGHTNING, POISON),
    BLUE(34570, new Location(1362, 10272, 0), FLAME, LIGHTNING);

    private final int objectId;
    private final Location location;
    private final HydraPhase weakeningPhase, empoweringPhase;

    AlchemicalVent(int objectId, Location location, HydraPhase weakeningPhase, HydraPhase empoweringPhase) {
        this.objectId = objectId;
        this.location = location;
        this.weakeningPhase = weakeningPhase;
        this.empoweringPhase = empoweringPhase;
    }

    public int getObjectId() {
        return objectId;
    }

    public Location getLocation() {
        return location;
    }

    public HydraPhase getWeakeningPhase() {
        return weakeningPhase;
    }

    public HydraPhase getEmpoweringPhase() {
        return empoweringPhase;
    }
}
