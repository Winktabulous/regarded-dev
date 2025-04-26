package com.zenyte.game.content.skills.agility.pollnivneach;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.content.skills.agility.MarkOfGrace;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.ImmutableLocation;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Christopher
 * @since 3/30/2020
 */
public final class RoughWall extends AgilityCourseObstacle {
    private static final ImmutableLocation FINISH = new ImmutableLocation(3365, 2983, 2);
    private static final Animation climbingAnim = new Animation(828);

    public RoughWall() {
        super(PollnivneachRooftopCourse.class, 6);
    }

    @Override
    public void startSuccess(Player player, WorldObject object) {
        player.faceDirection(Direction.NORTH);
        player.setAnimation(climbingAnim);
        WorldTasksManager.schedule(() -> {
            player.setLocation(FINISH);
            MarkOfGrace.spawn(player, PollnivneachRooftopCourse.MARK_LOCATIONS, 70, 20);
        });
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 2;
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 5;
    }

    @Override
    public int getLevel(WorldObject object) {
        return 70;
    }

    @Override
    public int[] getObjectIds() {
        return new int[]{ObjectId.ROUGH_WALL_14940};
    }
}
