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
public final class Banner extends AgilityCourseObstacle {
    private static final ImmutableLocation START = new ImmutableLocation(3354, 2976, 1);
    private static final ImmutableLocation BANNER = new ImmutableLocation(3357, 2977, 2);
    private static final ImmutableLocation FINISH = new ImmutableLocation(3360, 2977, 1);
    private static final ForceMovement jumpToBannerMovement = new ForceMovement(BANNER, 30, ForceMovement.NORTH);
    private static final ForceMovement jumpToRoofMovement = new ForceMovement(FINISH, 30, ForceMovement.NORTH);
    private static final Animation hangingAnim = new Animation(1118);
    private static final SoundEffect jumpToBannerSound = new SoundEffect(2468, 0, 10);
    private static final SoundEffect jumpToRoofSound = new SoundEffect(2459, 0, 35);
    private static final SoundEffect landSound = new SoundEffect(2455);

    public Banner() {
        super(PollnivneachRooftopCourse.class, 3);
    }

    @Override
    public void startSuccess(Player player, WorldObject object) {
        player.setAnimation(PollnivneachRooftopCourse.runningStartAnim);

        WorldTasksManager.schedule(() -> {
            player.setAnimation(Animation.STOP);
            player.setLocation(START.transform(0, 0, 1));
            player.setForceMovement(jumpToBannerMovement);
            player.sendSound(jumpToBannerSound);
        });
        WorldTasksManager.schedule(() -> {
            player.setLocation(BANNER);
            player.setAnimation(hangingAnim);
        }, 1);

        WorldTasksManager.schedule(() -> {
            player.setAnimation(PollnivneachRooftopCourse.landAnim);
            player.setForceMovement(jumpToRoofMovement);
            player.sendSound(jumpToRoofSound);
        }, 3);
        WorldTasksManager.schedule(() -> {
            player.setLocation(FINISH);
            player.sendSound(landSound);
            player.faceDirection(Direction.NORTH_EAST);
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
        return 65;
    }

    @Override
    public int[] getObjectIds() {
        return new int[]{ObjectId.BANNER_14937};
    }
}
