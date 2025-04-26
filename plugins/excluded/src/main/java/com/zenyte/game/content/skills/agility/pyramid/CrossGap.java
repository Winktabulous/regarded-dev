package com.zenyte.game.content.skills.agility.pyramid;

import com.zenyte.game.content.skills.agility.pyramid.area.AgilityPyramidArea;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

public final class CrossGap extends FailableAgilityPyramidObstacle {
    private static final Animation startAnimation = new Animation(3057);
    private static final Animation slideAnimation = new Animation(3060);
    private static final Animation fallAnimation = new Animation(3056);
    private static final Animation finishAnimation = new Animation(3058);

    public CrossGap() {
        super(4);
    }

    @Override
    public String getFilterableStartMessage(boolean success) {
        return "You put your foot on the ledge and try to edge across...";
    }

    @Override
    public String getFilterableEndMessage(boolean success) {
        return "You skillfully edge across the gap";
    }

    @Override
    public void startSuccess(Player player, WorldObject object) {
        cross(player, object, true);
    }

    @Override
    public void startFail(Player player, WorldObject object) {
        cross(player, object, false);
    }

    @Override
    public void endFail(Player player, WorldObject object) {
        player.applyHit(new Hit(8, HitType.REGULAR));
    }

    private void cross(final Player player, final WorldObject object, final boolean success) {
        final Direction walkDirection = object.getFaceDirection().getCounterClockwiseDirection(6);
        final Location destination = player.getLocation().transform(walkDirection, 5);
        player.setFaceLocation(destination);
        WorldTasksManager.schedule(new WorldTask() {
            private int ticks;
            @Override
            public void run() {
                if (ticks == 0) {
                    player.setAnimation(new Animation(startAnimation.getId(), 10));
                    player.sendSound(new SoundEffect(2450, 1, 10));
                } else if (ticks == 1) {
                    player.getAppearance().setRenderAnimation(new RenderAnimation(RenderAnimation.STAND, slideAnimation.getId(), RenderAnimation.RUN));
                    player.setAnimation(Animation.STOP);
                    player.addWalkSteps(destination.getX(), destination.getY(), -1, false);
                }
                if (!success) {
                    final Location fallDestination = AgilityPyramidArea.getLowerTile(player.getLocation().transform(object.getFaceDirection(), 2));
                    if (ticks == 3) {
                        player.stop(Player.StopType.WALK);
                        player.getAppearance().resetRenderAnimation();
                        player.setAnimation(fallAnimation);
                    }
                    if (ticks == 5) {
                        player.setForceMovement(new ForceMovement(fallDestination, 30, walkDirection.getDirection()));
                    }
                    if (ticks == 6) {
                        player.setLocation(fallDestination);
                        stop();
                    }
                }
                if (ticks == 6 && success) {
                    player.getAppearance().resetRenderAnimation();
                    player.setAnimation(finishAnimation);
                    player.sendSound(new SoundEffect(2455));
                    stop();
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 56.4;
    }

    @Override
    public int getLevel(WorldObject object) {
        return 30;
    }

    @Override
    public Location getRouteEvent(Player player, WorldObject object) {
        return getLocation(object);
    }

    static Location getLocation(WorldObject object) {
        if (object.getFaceDirection() == Direction.WEST) {
            return object.getPosition().transform(Direction.SOUTH_EAST);
        } else if (object.getFaceDirection() == Direction.SOUTH) {
            return object.transform(2, 1, 0);
        } else if (object.getFaceDirection() == Direction.NORTH) {
            return object.transform(Direction.WEST);
        } else {
            return object.transform(Direction.NORTH, 2);
        }
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 7;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {10861, 10882, 10884};
    }

    @Override
    public double getFailXp(WorldObject object) {
        return 0;
    }
}
