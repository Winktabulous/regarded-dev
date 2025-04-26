package com.zenyte.game.content.skills.agility.ardougnerooftop;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.content.skills.agility.MarkOfGrace;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 08/06/2019 | 12:04
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public final class Plank extends AgilityCourseObstacle {

    private static final RenderAnimation RENDER = new RenderAnimation(RenderAnimation.STAND, 762, RenderAnimation.WALK);
    private static final SoundEffect EFFECT1 = new SoundEffect(2495, 4, 0);

    public Plank() {
        super(ArdougneRooftopCourse.class, 3);
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 90;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {ObjectId.PLANK_26635 };
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 5;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        player.sendSound(EFFECT1);
        player.addWalkSteps(2656, 3318, -1, false);
        MarkOfGrace.spawn(player, ArdougneRooftopCourse.MARK_LOCATIONS, 40, 90);
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 50;
    }

    @Override
    public RenderAnimation getRenderAnimation() {
        return RENDER;
    }

}
