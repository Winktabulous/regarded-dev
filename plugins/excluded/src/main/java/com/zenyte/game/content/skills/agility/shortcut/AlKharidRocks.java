package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

public class AlKharidRocks implements Shortcut {

    private static final Animation CRAWL = new Animation(1148);

    private static final RenderAnimation RENDER = new RenderAnimation(738, 737, 737, 737, 737, 737, -1);

    private static final Location BOTTOM = new Location(3302, 3315, 0);

    private static final Location TOP = new Location(3306, 3315, 0);

    private static final ForceMovement CRAWL_DOWN = new ForceMovement(BOTTOM, 120, ForceMovement.EAST);

    @Override
    public int getLevel(WorldObject object) {
        return 38;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 16549, 16550 };
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 7;
    }

    @Override
    public void startSuccess(Player player, WorldObject object) {
        if (object.getId() == ObjectId.ROCKS_16550)
            player.getAppearance().setRenderAnimation(RENDER);
        player.addWalkSteps(TOP.getX() - 1, TOP.getY(), -1, false);
        WorldTasksManager.schedule(new WorldTask() {

            private int ticks;

            @Override
            public void run() {
                if (object.getId() == ObjectId.ROCKS_16550) {
                    if (ticks == 3) {
                        player.getAppearance().resetRenderAnimation();
                        player.addWalkSteps(TOP.getX(), TOP.getY(), -1, false);
                    } else if (ticks == 4)
                        stop();
                } else {
                    if (ticks == 0)
                        player.setFaceLocation(TOP);
                    else if (ticks == 2) {
                        player.setAnimation(CRAWL);
                        player.setForceMovement(CRAWL_DOWN);
                    } else if (ticks == 6)
                        player.setLocation(BOTTOM);
                    else if (ticks == 7)
                        stop();
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 0;
    }
}
