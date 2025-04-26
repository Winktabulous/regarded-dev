package com.zenyte.game.world.region.area.apeatoll;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 21/03/2019 16:02
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Skeleton extends NPC implements Spawnable {
    private static final Animation spawn = new Animation(3994);

    public Skeleton(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
        delayUntilBurning = -1;
    }

    Skeleton(final Location tile) {
        super(5237, tile, Direction.SOUTH, 8);
        delayUntilBurning = 200;
        setAnimation(spawn);
        this.setSpawned(true);
        lock(1);
        this.setRandomWalkDelay(2);
    }

    private int delayUntilBurning;

    @Override
    public boolean isTolerable() {
        return false;
    }

    @Override
    public boolean validate(int id, String name) {
        return id == 5237;
    }

    @Override
    public void processNPC() {
        super.processNPC();
        if (delayUntilBurning > 0) {
            if (--delayUntilBurning == 0) {
                this.reset();//Reset so no one gets the drop and it will not re-respawn.
                this.sendDeath();
            }
        }
    }

    @Override
    public void onFinish(final Entity source) {
        if (delayUntilBurning == 0) {
            final Location tile = getLocation();
            final WorldObject existingObject = World.getObjectOfSlot(tile, 10);
            if (existingObject == null) {
                final WorldObject bones = new WorldObject(3862, 10, 0, tile);
                World.spawnTemporaryObject(bones, 30);
            }
        }
        super.onFinish(source);
    }
}
