package com.zenyte.game.content.wildernessVault;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;

public enum VaultSpawnDefinition {

    WEST_OF_MAGE_ARENA("west of the Mage Arena", 3076, 3937, 0),
    EDGEVILLE("north of the Edgeville ditch", 3103, 3543, 0),
    CHAOS_ALTAR("at the Chaos altar", 2977, 3819, 0),
    OBELISK_27("at the level 27 Obelisk", 3052, 3737, 0),
    ICE_PLATEAU("at the Ice plateau", 2983, 3961, 0),
    WEST_OF_FEROX_ENCLAVE("west of the Ferox Enclave", 3082, 3622, 0),
    ;

    public static final VaultSpawnDefinition[] VALUES = values();
    private final String name;
    private final Location location;
    private final int entranceRotation;

    VaultSpawnDefinition(String name, int x, int y, int entranceRotation) {
        this.name = name;
        this.location = new Location(x, y);
        this.entranceRotation = entranceRotation;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public Location locationPlayer() {
        return location.transform(Utils.random(0, 3), Utils.random(-2, -2), 0);
    }

    public int getEntranceRotation() {
        return entranceRotation;
    }

}
