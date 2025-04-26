package com.zenyte.game.content.skills.magic.spells.teleports;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.Location;

/**
 * @author Tommeh | 3-2-2019 | 16:21
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public enum ItemTeleport implements Teleport {

    MAX_CAPE_WARRIORS_GUILD(new Location(2865, 3546, 0)),
    MAX_CAPE_CRAFTING_GUILD(new Location(2931, 3286, 0)),
    MAX_CAPE_FISHING_GUILD(new Location(2604, 3401, 0)),
    MAX_CAPE_FARMING_GUILD(new Location(1248, 3724, 0)),
    MAX_CAPE_BLACK_CHINCHOMPAS(new Location(3147, 3758, 0)),
    MAX_CAPE_CARNIVEROUS_CHINCHOMPAS(new Location(2557, 2908, 0)),
    MAX_CAPE_OTTOS_GROTTO(new Location(2504, 3484, 0))
    ;


    private final Location destination;

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public TeleportType getType() {
        return TeleportType.REGULAR_TELEPORT;
    }

    @Override
    public double getExperience() {
        return 0;
    }

    @Override
    public int getRandomizationDistance() {
        return 0;
    }

    @Override
    public int getWildernessLevel() {
        return 20;
    }

    @Override
    public boolean isCombatRestricted() {
        return false;
    }

    @Override
    public Item[] getRunes() {
        return new Item[0];
    }
    
    ItemTeleport(Location destination) {
        this.destination = destination;
    }
    
    public Location getDestination() {
        return destination;
    }

}
