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
public final class SteppingTree extends AgilityCourseObstacle {
    private static final ImmutableLocation START = new ImmutableLocation(3360, 2995, 2);
    private static final ImmutableLocation TREE = new ImmutableLocation(3360, 2997, 2);
    private static final ImmutableLocation FINISH = new ImmutableLocation(3359, 3000, 2);
    private static final Animation jumpAnim = new Animation(1603);
    private static final ForceMovement jumpToTreeMovement = new ForceMovement(TREE, 30, ForceMovement.NORTH);
    private static final ForceMovement jumpToRoofMovement = new ForceMovement(FINISH, 30, ForceMovement.NORTH);
    private static final SoundEffect jumpToTreeSound = new SoundEffect(1936);
    private static final SoundEffect jumpToRoofSound = new SoundEffect(1936, 0, 10);

    public SteppingTree() {
        super(PollnivneachRooftopCourse.class, 8);
    }

    @Override
    public void startSuccess(Player player, WorldObject object) {
        player.setLocation(START.transform(0, 0, 1));
        player.setAnimation(jumpAnim);
        player.setForceMovement(jumpToTreeMovement);
        player.sendSound(jumpToTreeSound);
        WorldTasksManager.schedule(() -> {
            player.setLocation(TREE);
            player.setAnimation(jumpAnim);
            player.setForceMovement(jumpToRoofMovement);
            player.sendSound(jumpToRoofSound);
        }, 1);
        WorldTasksManager.schedule(() -> {
            player.setLocation(FINISH);
            MarkOfGrace.spawn(player, PollnivneachRooftopCourse.MARK_LOCATIONS, 70, 20);
        }, 2);
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return START;
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 3;
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 60;
    }

    @Override
    public int getLevel(WorldObject object) {
        return 70;
    }

    @Override
    public int[] getObjectIds() {
        return new int[]{ObjectId.TREE_14944};
    }
}
