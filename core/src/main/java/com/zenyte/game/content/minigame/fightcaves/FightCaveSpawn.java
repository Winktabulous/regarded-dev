package com.zenyte.game.content.minigame.fightcaves;

import com.zenyte.game.world.entity.Location;

/**
 * @author Kris | 8. nov 2017 : 20:49.02
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public enum FightCaveSpawn {

    SE(new Location(2415, 5082, 0)),
    SW(new Location(2379, 5070, 0)),
    C(new Location(2401, 5085, 0)),
    NW(new Location(2385, 5104, 0)),
    S(new Location(2400, 5075, 0));

    private static final FightCaveSpawn[] clock = new FightCaveSpawn[]{
            SE, SW, C, NW, SW, SE, S, NW, C, SE, SW, S, NW, C, S
    };
    private final Location location;

    FightCaveSpawn(Location location) {
        this.location = location;
    }

    public static Location getNextLocation(final int rotation) {
        return clock[rotation % 14].location;
    }

}
