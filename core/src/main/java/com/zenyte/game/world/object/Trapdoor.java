package com.zenyte.game.world.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.utilities.CollectionUtils;

/**
 * @author Kris | 28. aug 2018 : 02:24:30
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */

public enum Trapdoor {

	EDGEVILLE_TRAPDOOR(1581, 1579, tile(3100, 3487, 0), tile(3096, 9867, 0)),
	DRAYNOR_TRAPDOOR_A(6435, 6434, tile(3084, 3272, 0), tile(3084, 9673, 0)),
    DRAYNOR_TRAPDOOR_B(6435, 6434, tile(3118, 3244, 0), tile(3118, 9644, 0)),
	ROGUES_DEN_TRAPDOOR(7257, -1, tile(2905, 3537, 0), tile(3061, 4985, 1)),
    PATERDOMUS_TEMPLE_TRAPDOOR(1581, 1579, tile(3405, 3507, 0), tile(3405, 9906, 0)),
    PATERDOMUS_TRAPDOOR_OTHERSIDE(3433, 3432, tile(3422, 3485, 0), tile(3440, 9887, 0)),
    APE_ATOLL(4880, 4879, tile(2807, 2785, 0), tile(2807, 9201, 0)),

	CASTLEWARS_MIDDLE_SOUTH(1581, 1580, tile(2399, 3099, 0), tile(2399, 9500, 0)),
	CASTLEWARS_MIDDLE_NORTH(1581, 1580, tile(2400, 3108, 0), tile(2400, 9507, 0)),
    ;

    public static final Trapdoor[] values = values();
    public static final Int2ObjectOpenHashMap<Trapdoor> trapdoors;
    public static final Animation CLIMB_DOWN = new Animation(827);

    static {
        CollectionUtils.populateMap(values, trapdoors = new Int2ObjectOpenHashMap<>(values.length),
                trapdoor -> trapdoor.trapdoorLocation.getPositionHash());
    }

    private final int open;
    private final int closed;
    private final Location trapdoorLocation;
    private final Location destination;

    Trapdoor(int open, int closed, Location trapdoorLocation, Location destination) {
        this.open = open;
        this.closed = closed;
        this.trapdoorLocation = trapdoorLocation;
        this.destination = destination;
    }

    private static final Location tile(final int x, final int y, final int z) {
        return new Location(x, y, z);
    }

    public int getOpen() {
        return open;
    }

    public int getClosed() {
        return closed;
    }

    public Location getTrapdoorLocation() {
        return trapdoorLocation;
    }

    public Location getDestination() {
        return destination;
    }


}
