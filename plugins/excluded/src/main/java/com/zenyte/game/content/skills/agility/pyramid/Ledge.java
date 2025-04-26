package com.zenyte.game.content.skills.agility.pyramid;

import com.zenyte.game.content.skills.agility.IrreversibleDirection;
import com.zenyte.game.content.skills.agility.pyramid.area.AgilityPyramidArea;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

import static com.zenyte.game.content.skills.agility.pyramid.CrossGap.getLocation;

public final class Ledge extends FailableAgilityPyramidObstacle implements IrreversibleDirection {
    private static final Animation startAnimation = new Animation(753);
    private static final Animation slideAnimation = new Animation(756);
    private static final Animation fallAnimation = new Animation(3062);
    private static final Animation finishAnimation = new Animation(759);

    public Ledge() {
        super(2);
    }

    @Override
    public String getFilterableStartMessage(boolean success) {
        return "You put your foot on the ledge and try to edge across...";
    }

    @Override
    public String getFilterableEndMessage(boolean success) {
        return success ? "You skillfully edge across the gap." : "You slip and fall to the level below.";
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
        player.applyHit(new Hit(10, HitType.REGULAR));
    }

    private void cross(final Player player, final WorldObject object, final boolean success) {
        final Direction walkDirection = object.getFaceDirection().getCounterClockwiseDirection(6);
        final Location destination = player.getLocation().transform(walkDirection, 5);
        final SoundEffect sound = new SoundEffect(2451, 1, 20, success ? 5 : 2);
        player.setFaceLocation(destination);
        WorldTasksManager.schedule(new WorldTask() {
            private int ticks;
            @Override
            public void run() {
                if (ticks == 0) {
                    player.setAnimation(new Animation(startAnimation.getId(), 10));
                    player.sendSound(sound);
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
                    if (ticks == 4) {
                        player.setForceMovement(new ForceMovement(fallDestination, 30, walkDirection.getDirection()));
                    }
                    if (ticks == 5) {
                        player.setLocation(fallDestination);
                        stop();
                    }
                }
                if (ticks == 6 && success) {
                    player.getAppearance().resetRenderAnimation();
                    player.setAnimation(finishAnimation);
                    stop();
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 52;
    }

    @Override
    public int getLevel(WorldObject object) {
        return 30;
    }

    @Override
    public Location getRouteEvent(Player player, WorldObject object) {
        return getLocation(object);
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return success ? 7 : 6;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {10860, 10886, 10888};
    }

    @Override
    public double getFailXp(WorldObject object) {
        return 0;
    }

    @Override
    public Direction getReverseDirection(Player player, WorldObject object) {
        return object.getFaceDirection().getCounterClockwiseDirection(6);
    }

    @Override
    public boolean failOnReverse() {
        return false;
    }
}
