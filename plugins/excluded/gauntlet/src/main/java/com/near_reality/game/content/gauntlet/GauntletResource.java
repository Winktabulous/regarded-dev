package com.near_reality.game.content.gauntlet;

import com.near_reality.game.content.gauntlet.objects.CrystalDeposit;
import com.near_reality.game.content.gauntlet.objects.FishingSpot;
import com.near_reality.game.content.gauntlet.objects.LinumTirinum;
import com.near_reality.game.content.gauntlet.objects.PhrenRoots;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.object.WorldObject;

public enum GauntletResource {
    FISHING_SPOT(36069, 35972), // TODO subtract each by one.
    CRYSTAL_DEPOSIT(36064, 35967),
    PHREN_ROOTS(36066, 35969),
    GRYM_ROOT(36070, 35973),
    LINUM_TIRINUM(36072, 35975);

    public static final GauntletResource[] DEPLETING = { FISHING_SPOT, CRYSTAL_DEPOSIT, PHREN_ROOTS, LINUM_TIRINUM };

    private final int objectId;

    private final int corruptedVariant;

    GauntletResource(int objectId, int corruptedVariant) {
        this.objectId = objectId;
        this.corruptedVariant = corruptedVariant;
    }

    public WorldObject toObject(Location location, boolean corrupted)  {
        if (this == FISHING_SPOT) {
            return new FishingSpot(location, corrupted);
        } else if (this == CRYSTAL_DEPOSIT) {
            return new CrystalDeposit(location, corrupted);
        } else if (this == PHREN_ROOTS) {
            return new PhrenRoots(location, corrupted);
        } else if (this == LINUM_TIRINUM) {
            return new LinumTirinum(location, corrupted);
        } else {
            return new WorldObject(corrupted ? 35973 : objectId, WorldObject.DEFAULT_TYPE, Utils.random(0, 3), location);
        }
    }
}
