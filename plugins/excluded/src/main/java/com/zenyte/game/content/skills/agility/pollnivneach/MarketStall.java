package com.zenyte.game.content.skills.agility.pollnivneach;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.content.skills.agility.MarkOfGrace;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.ImmutableLocation;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Christopher
 * @since 3/30/2020
 */
public final class MarketStall extends AgilityCourseObstacle {
    private static final ImmutableLocation START = new ImmutableLocation(3350, 2968, 1);
    private static final ImmutableLocation OVER_STALL = new ImmutableLocation(3350, 2971, 1);
    private static final ImmutableLocation ROOF = new ImmutableLocation(3352, 2973, 1);
    private static final ForceMovement jumpToStallMovement = new ForceMovement(OVER_STALL, 45, ForceMovement.NORTH);
    private static final ForceMovement jumpToRoofMovement = new ForceMovement(ROOF, 30, ForceMovement.NORTH);
    private static final Animation firstJumpAnim = new Animation(1603);
    private static final SoundEffect jumpToStallSound = new SoundEffect(1936);
    private static final SoundEffect jumpToRoofSound = new SoundEffect(2468);
    private static final SoundEffect landSound = new SoundEffect(2455);

    public MarketStall() {
        super(PollnivneachRooftopCourse.class, 2);
    }

    @Override
    public void startSuccess(Player player, WorldObject object) {
        player.setAnimation(PollnivneachRooftopCourse.runningStartAnim);

        WorldTasksManager.schedule(() -> {
            player.setLocation(START.transform(0, 0, 1));
            player.setForceMovement(jumpToStallMovement);
            player.setAnimation(firstJumpAnim);
            player.sendSound(jumpToStallSound);
        });
        WorldTasksManager.schedule(() -> player.setLocation(OVER_STALL), 2);

        WorldTasksManager.schedule(() -> {
            player.sendSound(jumpToRoofSound);
            player.setForceMovement(jumpToRoofMovement);
            player.setAnimation(PollnivneachRooftopCourse.landAnim);
        }, 3);
        WorldTasksManager.schedule(() -> {
            player.setLocation(ROOF);
            player.sendSound(landSound);
            MarkOfGrace.spawn(player, PollnivneachRooftopCourse.MARK_LOCATIONS, 70, 20);
        }, 4);
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return START;
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 5;
    }

    @Override
    public int getLevel(WorldObject object) {
        return 70;
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 45;
    }

    @Override
    public int[] getObjectIds() {
        return new int[]{ObjectId.MARKET_STALL_14936};
    }
}
