package com.near_reality.game.content.gauntlet.objects;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;

import java.util.Optional;

/**
 * 1/25/2022
 */
public final class CrystalDeposit extends DepletingResourceObject {

    private static final int INITIAL_AMOUNT = 3;

    public CrystalDeposit(Location location, boolean corrupted) {
        super(corrupted ? 35967 : 36064, location, Utils.random(0, 3), corrupted ? 23837 : 23877, INITIAL_AMOUNT);
    }

    @Override
    public Optional<String> getDepletionMessage() {
        return Optional.of("You mine the last of the salvageable ore from the rock.");
    }

}
