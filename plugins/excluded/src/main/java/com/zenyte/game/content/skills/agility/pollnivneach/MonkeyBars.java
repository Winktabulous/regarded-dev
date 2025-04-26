package com.zenyte.game.content.skills.agility.pollnivneach;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.content.skills.agility.MarkOfGrace;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.ImmutableLocation;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Christopher
 * @since 3/30/2020
 */
public final class MonkeyBars extends AgilityCourseObstacle {
    private static final ImmutableLocation START = new ImmutableLocation(3358, 2984, 2);
    private static final ImmutableLocation FINISH = new ImmutableLocation(3358, 2991, 2);
    private static final Animation jumpAnim = new Animation(742);
    private static final Animation dropAnim = new Animation(743);
    private static final RenderAnimation RENDER = new RenderAnimation(745, 744, 744, 744, 744, 744, 744);
    private static final SoundEffect crossingSound = new SoundEffect(2466, 0, 40, 15);
    private static final SoundEffect landSound = new SoundEffect(2473);

    public MonkeyBars() {
        super(PollnivneachRooftopCourse.class, 7);
    }

    @Override
    public void startSuccess(Player player, WorldObject object) {
        player.setAnimation(jumpAnim);
        player.sendSound(crossingSound);
        WorldTasksManager.schedule(() -> player.addWalkSteps(FINISH.getX(), FINISH.getY(), -1, false));
        WorldTasksManager.schedule(() -> {
            player.setAnimation(dropAnim);
            player.sendSound(landSound);
            MarkOfGrace.spawn(player, PollnivneachRooftopCourse.MARK_LOCATIONS, 70, 20);
        }, 8);
    }

    @Override
    public RenderAnimation getRenderAnimation() {
        return RENDER;
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return START;
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 9;
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 55;
    }

    @Override
    public int getLevel(WorldObject object) {
        return 70;
    }

    @Override
    public int[] getObjectIds() {
        return new int[]{ObjectId.MONKEYBARS};
    }

}
