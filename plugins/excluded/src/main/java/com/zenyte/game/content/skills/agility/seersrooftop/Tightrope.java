package com.zenyte.game.content.skills.agility.seersrooftop;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.content.skills.agility.MarkOfGrace;
import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

public final class Tightrope extends AgilityCourseObstacle implements Shortcut {

    private static final RenderAnimation RENDER = new RenderAnimation(RenderAnimation.STAND, 762, RenderAnimation.WALK);
    private static final Location START = new Location(2710, 3490, 2);

    public Tightrope() {
        super(SeersRooftopCourse.class, 3);
    }

    @Override
    public void startSuccess(Player player, WorldObject object) {
    	MarkOfGrace.spawn(player, SeersRooftopCourse.MARK_LOCATIONS, 60, 20);
        player.addWalkSteps(2710, 3489, -1, false);
        player.addWalkSteps(2710, 3481, -1, false);
    }

    @Override
    public int getLevel(WorldObject object) {
        return 60;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {ObjectId.TIGHTROPE_14932 };
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 9;
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 20;
    }

    @Override
    public RenderAnimation getRenderAnimation() {
        return RENDER;
    }

    @Override
    public Location getRouteEvent(Player player, WorldObject object) {
        return START;
    }
}
