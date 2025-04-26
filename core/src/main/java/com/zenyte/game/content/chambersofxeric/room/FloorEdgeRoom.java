package com.zenyte.game.content.chambersofxeric.room;

import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.chambersofxeric.map.RaidArea;
import com.zenyte.game.content.chambersofxeric.map.RaidRoom;
import com.zenyte.game.world.entity.Location;

/**
 * @author Kris | 16. nov 2017 : 2:22.39
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class FloorEdgeRoom extends RaidArea {

    /**
     * The coordinates to the static storage units found in this room.
     */
    private static final Location[] storageUnits = new Location[]{
            new Location(3287, 5168, 0), new Location(3308, 5164, 0)
    };

    public FloorEdgeRoom(final RaidRoom type, final Raid raid, final int rotation, final int size, final int regionX, final int regionY,
                         final int chunkX, final int chunkY, final int fromPlane, final int toPlane) {
        super(type, raid, rotation, size, regionX, regionY, chunkX, chunkY, fromPlane, toPlane);
    }

    @Override
    public String name() {
        return "Chambers of Xeric: Edge room";
    }

    @Override
    public Location[] getStorageUnitLocations() {
        return storageUnits;
    }

}
