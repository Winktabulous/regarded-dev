package com.zenyte.game.content.skills.agility.pollnivneach;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.content.skills.agility.MarkOfGrace;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
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
public final class Tree extends AgilityCourseObstacle {
    private static final ImmutableLocation START = new ImmutableLocation(3368, 2976, 1);
    private static final ImmutableLocation FIRST_BRANCH = new ImmutableLocation(3368, 2978, 2);
    private static final ImmutableLocation SECOND_BRANCH = new ImmutableLocation(3368, 2980, 2);
    private static final ImmutableLocation ROOF = new ImmutableLocation(3368, 2982, 1);
    private static final ForceMovement jumpToFirstBranchMovement = new ForceMovement(FIRST_BRANCH, 30, ForceMovement.EAST);
    private static final ForceMovement jumpToSecondBranchMovement = new ForceMovement(SECOND_BRANCH, 30, ForceMovement.EAST);
    private static final ForceMovement jumpToRoofMovement = new ForceMovement(ROOF, 30, ForceMovement.EAST);
    private static final Animation firstBranchAnim = new Animation(1122);
    private static final Animation secondBranchAnim = new Animation(1124);
    private static final SoundEffect jumpToFirstBranchSound = new SoundEffect(2468, 0, 10);
    private static final SoundEffect jumpToSecondBranchSound = new SoundEffect(2459, 0, 35);
    private static final SoundEffect landSound = new SoundEffect(2455);

    public Tree() {
        super(PollnivneachRooftopCourse.class, 5);
    }

    @Override
    public void startSuccess(Player player, WorldObject object) {
        player.setLocation(START.transform(0, 0, 1));

        WorldTasksManager.schedule(() -> {
            player.setForceMovement(jumpToFirstBranchMovement);
            player.sendSound(jumpToFirstBranchSound);
        });
        WorldTasksManager.schedule(() -> {
            player.setAnimation(firstBranchAnim);
            player.setLocation(FIRST_BRANCH);
            player.sendSound(jumpToSecondBranchSound);
        }, 1);

        WorldTasksManager.schedule(() -> player.setForceMovement(jumpToSecondBranchMovement), 2);
        WorldTasksManager.schedule(() -> {
            player.setAnimation(secondBranchAnim);
            player.setLocation(SECOND_BRANCH);
            player.sendSound(jumpToSecondBranchSound);
        }, 3);

        WorldTasksManager.schedule(() -> {
            player.setAnimation(PollnivneachRooftopCourse.landAnim);
            player.setForceMovement(jumpToRoofMovement);
        }, 4);

        WorldTasksManager.schedule(() -> {
            player.setLocation(ROOF);
            player.sendSound(landSound);
            player.faceDirection(Direction.NORTH);
            MarkOfGrace.spawn(player, PollnivneachRooftopCourse.MARK_LOCATIONS, 70, 20);
        }, 5);
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return START;
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 6;
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 75;
    }

    @Override
    public int getLevel(WorldObject object) {
        return 70;
    }

    @Override
    public int[] getObjectIds() {
        return new int[]{ObjectId.TREE_14939};
    }
}
