package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

public class BrimhavenVine implements Shortcut {

    private static final Animation UP = new Animation(828);

    private static final Animation DOWN = new Animation(827);

    private static final Location TOP = new Location(2672, 9583, 2);

    private static final Location BOTTOM = new Location(2673, 9583, 0);

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        player.faceObject(object);
        WorldTasksManager.schedule(new WorldTask() {

            private int ticks;

            @Override
            public void run() {
                if (object.getId() == ObjectId.VINE_26880) {
                    if (ticks == 0) {
                        player.setAnimation(UP);
                    } else if (ticks == 1) {
                        player.setLocation(TOP);
                    } else if (ticks == 2) {
                        player.addWalkSteps(TOP.getX() - 2, TOP.getY(), -1, false);
                    }
                } else {
                    if (ticks == 0) {
                        player.addWalkSteps(TOP.getX(), TOP.getY(), -1, false);
                    } else if (ticks == 1) {
                        player.setAnimation(DOWN);
                    } else if (ticks == 2) {
                        player.setLocation(BOTTOM);
                    }
                }
                if (ticks == 3) {
                    stop();
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 87;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 26880, 26882 };
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 3;
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
