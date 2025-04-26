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
public final class Gap extends AgilityCourseObstacle {
    private static final ImmutableLocation START = new ImmutableLocation(3362, 2977, 1);
    private static final ImmutableLocation HANG_LOCATION = new ImmutableLocation(3365, 2976, 2);
    private static final ImmutableLocation ROOF = new ImmutableLocation(3366, 2976, 1);
    private static final ForceMovement jumpMovement = new ForceMovement(HANG_LOCATION, 30, ForceMovement.EAST);
    private static final Animation climbAnim = new Animation(2585);
    private static final SoundEffect jumpToEdge = new SoundEffect(2468, 0, 15);

    public Gap() {
        super(PollnivneachRooftopCourse.class, 4);
    }

    @Override
    public void startSuccess(Player player, WorldObject object) {
        player.setLocation(START.transform(0, 0, 1));
        WorldTasksManager.schedule(() -> {
            player.setForceMovement(jumpMovement);
            player.sendSound(jumpToEdge);
        });
        WorldTasksManager.schedule(() -> {
            player.setLocation(HANG_LOCATION);
            player.setAnimation(climbAnim);
        }, 1);
        WorldTasksManager.schedule(() -> {
            player.setLocation(ROOF);
            player.faceDirection(Direction.EAST);
            MarkOfGrace.spawn(player, PollnivneachRooftopCourse.MARK_LOCATIONS, 70, 20);
        }, 3);
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return START;
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 4;
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 35;
    }

    @Override
    public int getLevel(WorldObject object) {
        return 70;
    }

    @Override
    public int[] getObjectIds() {
        return new int[]{ObjectId.GAP_14938};
    }
}
