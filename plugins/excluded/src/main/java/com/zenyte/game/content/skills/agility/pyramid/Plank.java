package com.zenyte.game.content.skills.agility.pyramid;

import com.zenyte.game.content.skills.agility.IrreversibleObject;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

public final class Plank extends FailableAgilityPyramidObstacle implements IrreversibleObject {
    private static final Animation walkAnim = new Animation(762);
    private static final Animation standAnim = new Animation(763);
    private static final Animation fallAnim = new Animation(764);
    private static final Animation reverseFallAnim = new Animation(3069);

    public Plank() {
        super(3);
    }

    @Override
    public String getFilterableStartMessage(boolean success) {
        return "You walk carefully across the slippery plank...";
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
        final Direction walkDirection = object.getFaceDirection().getCounterClockwiseDirection(4);
        final Location destination = player.getLocation().transform(walkDirection, 5);
        final SoundEffect sound = new SoundEffect(2470, 1, 10, success ? 4 : 2);
        final boolean reverse = checkForReverse(player, object);
        player.setFaceLocation(destination);
        WorldTasksManager.schedule(new WorldTask() {
            private int ticks;
            @Override
            public void run() {
                if (ticks == 0) {
                    player.setAnimation(new Animation(standAnim.getId(), 10));
                    player.sendSound(sound);
                } else if (ticks == 1) {
                    player.getAppearance().setRenderAnimation(new RenderAnimation(RenderAnimation.STAND, walkAnim.getId(), RenderAnimation.RUN));
                    player.setAnimation(Animation.STOP);
                    player.addWalkSteps(destination.getX(), destination.getY(), -1, false);
                }
                if (!success) {
                    final Direction fallDirection = object.getFaceDirection().getCounterClockwiseDirection(reverse ? 2 : 6);
                    final Location fallDestination = player.getLocation().transform(fallDirection, 2).transform(0, 0, -1);
                    if (ticks == 3) {
                        player.stop(Player.StopType.WALK);
                        player.getAppearance().resetRenderAnimation();
                        player.setAnimation(reverse ? reverseFallAnim : fallAnim);
                    }
                    if (ticks == 4) {
                        player.setForceMovement(new ForceMovement(fallDestination, 30, walkDirection.getDirection()));
                    }
                    if (ticks == 5) {
                        player.setLocation(fallDestination);
                        stop();
                    }
                }
                if (ticks == 6) {
                    player.getAppearance().resetRenderAnimation();
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
        return new Location(object);
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return success ? 7 : 6;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {10867, 10868};
    }

    @Override
    public double getFailXp(WorldObject object) {
        return 0;
    }

    @Override
    public int[] getFailObjectIds() {
        return new int[] {10867};
    }

    @Override
    public boolean failOnReverse() {
        return true;
    }
}
