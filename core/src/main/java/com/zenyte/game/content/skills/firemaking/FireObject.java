package com.zenyte.game.content.skills.firemaking;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

import java.lang.ref.WeakReference;

public class FireObject extends WorldObject {

    private final WeakReference<Player> owner;

    public FireObject(Player owner, int id, int type, int rotation, Location location) {
        super(id, type, rotation, location);
        this.owner = new WeakReference<>(owner);
    }

    public Player getOwner() {
        return owner.get();
    }
}
