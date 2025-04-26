package com.near_reality.game.content.gauntlet.objects;

import com.zenyte.game.world.entity.Location;

import java.util.Optional;

/**
 *
 * @author Andys1814.
 * @since 2/7/2022.
 */
public final class FishingSpot extends DepletingResourceObject {

    private static final int INITIAL_AMOUNT = 4;

    public FishingSpot(Location location, boolean corrupted) {
        super(corrupted ? 35971 : 36068, location, 0, 23872, INITIAL_AMOUNT);
    }

    @Override
    public Optional<String> getDepletionMessage() {
        return Optional.of("You catch the last of the fish.");
    }

}
