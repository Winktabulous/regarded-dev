package com.zenyte.game.world.entity.npc.impl;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Kris | 14/01/2019 16:37
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Shade extends NPC implements Spawnable {
    private static final Animation rise = new Animation(1288);
    public static final int[] shades = new int[] {1277, 1280, 1282, 1284, 1286};
    public static final int[] shadows = new int[] {1276, 1279, 1281, 1283, 1285};

    public Shade(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean validate(int id, String name) {
        return ArrayUtils.contains(shades, id) || ArrayUtils.contains(shadows, id);
    }

    @Override
    public void processNPC() {
        super.processNPC();
        if (!this.isUnderCombat() & getAttackedByDelay() < WorldThread.getCurrentCycle()) {
            if (ArrayUtils.contains(shades, getId())) {
                lock(1);
                resetWalkSteps();
                setFaceEntity(null);
                setTransformation(getId() - 1);
                reset();
            }
        } else {
            if (!isDead()) {
                if (ArrayUtils.contains(shadows, getId())) {
                    final Entity target = combat.getTarget();
                    resetWalkSteps();
                    setTransformation(getId() + 1);
                    setAnimation(rise);
                    setFaceEntity(target);
                }
            }
        }
    }
}
