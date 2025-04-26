package com.zenyte.game.content.skills.agility.pollnivneach;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.content.skills.agility.MarkOfGrace;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.ImmutableLocation;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Christopher
 * @since 3/30/2020
 */
public final class Basket extends AgilityCourseObstacle {
    private static final ImmutableLocation OVER_BASKET = new ImmutableLocation(3351, 2962, 1);
    private static final ImmutableLocation FINISH = new ImmutableLocation(3351, 2964, 1);
    private static final Animation jumpAnim = new Animation(2583);
    private static final SoundEffect jumpSound = new SoundEffect(2468, 0, 20);

    public Basket() {
        super(PollnivneachRooftopCourse.class, 1);
    }

    @Override
    public void startSuccess(Player player, WorldObject object) {
        player.setLocation(player.getLocation().transform(0, 0, 1));
        player.setAnimation(jumpAnim);
        player.sendSound(jumpSound);
        WorldTasksManager.schedule(() -> {
            player.setAnimation(PollnivneachRooftopCourse.landAnim);
            player.setLocation(OVER_BASKET);
        });

        WorldTasksManager.schedule(() -> {
            player.setAnimation(jumpAnim);
            player.sendSound(jumpSound);
        }, 1);
        WorldTasksManager.schedule(() -> {
            player.setAnimation(PollnivneachRooftopCourse.landAnim);
            player.setLocation(FINISH);
            MarkOfGrace.spawn(player, PollnivneachRooftopCourse.MARK_LOCATIONS, 70, 20);
        }, 2);
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 3;
    }

    @Override
    public int getLevel(WorldObject object) {
        return 70;
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 10;
    }

    @Override
    public int[] getObjectIds() {
        return new int[]{ObjectId.BASKET_14935};
    }
}
