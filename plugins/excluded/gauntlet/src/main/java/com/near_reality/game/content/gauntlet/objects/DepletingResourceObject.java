package com.near_reality.game.content.gauntlet.objects;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.object.WorldObject;

import java.util.Optional;

/**
 * An instance of WorldObject that stores state for a resource object that depletes on each yield up to a given amount.
 *
 * 1/21/2022
 */
public abstract class DepletingResourceObject extends WorldObject {

    private int amount;

    private final int resource;

    public DepletingResourceObject(int id, Location location, int rotation, int resource, int initialAmount) {
        super(id, WorldObject.DEFAULT_TYPE, rotation, location);
        this.amount = initialAmount;
        this.resource = resource;
    }

    public int getAmount() {
        return amount;
    }

    public int getResource() {
        return resource;
    }

    public boolean isDepleted() {
        return amount <= 0;
    }

    public void deplete() {
        this.amount = Math.max(0, this.amount - 1);
    }

    public abstract Optional<String> getDepletionMessage();

}
