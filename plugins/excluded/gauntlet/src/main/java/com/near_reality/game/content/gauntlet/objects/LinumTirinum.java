package com.near_reality.game.content.gauntlet.objects;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;

import java.util.Optional;

/**
 * @author Andys1814
 * @since 1/21/2022.
 */
public final class LinumTirinum extends DepletingResourceObject {

    private static final int INITIAL_AMOUNT = 3;

    public LinumTirinum(Location location, boolean corrupted) {
        super(corrupted ? 35975 : 36072, location, Utils.random(0, 3), corrupted ? 23836 : 23876, INITIAL_AMOUNT);
    }

    @Override
    public Optional<String> getDepletionMessage() {
        return Optional.empty();
    }

}
