package com.zenyte.game.content.skills.magic.spells.teleports;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.Location;
import org.jetbrains.annotations.NotNull;

public class RegularTeleport implements Teleport {

    public RegularTeleport(@NotNull final Location location) {
        this.location = location;
    }

    private final Location location;

    @Override
    public TeleportType getType() {
        return TeleportType.REGULAR_TELEPORT;
    }

    @Override
    public Location getDestination() {
        return location;
    }

    @Override
    public int getLevel() {
        return 0;
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
    public Item[] getRunes() {
        return null;
    }

    @Override
    public int getWildernessLevel() {
        return WILDERNESS_LEVEL;
    }

    @Override
    public boolean isCombatRestricted() {
        return UNRESTRICTED;
    }
}
