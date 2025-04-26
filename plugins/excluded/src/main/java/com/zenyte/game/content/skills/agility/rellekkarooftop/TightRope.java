package com.zenyte.game.content.skills.agility.rellekkarooftop;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.content.skills.agility.MarkOfGrace;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 09/06/2019 | 14:43
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public final class TightRope extends AgilityCourseObstacle {

    private static final RenderAnimation RENDER = new RenderAnimation(RenderAnimation.STAND, 762, RenderAnimation.WALK);

    private static final Animation ANIM1 = new Animation(1995, 15);
    private static final Animation ANIM2 = new Animation(1603);

    private static final Location LOCATION1 = new Location(2622, 3668, 3);
    private static final SoundEffect EFFECT1 = new SoundEffect(2495, 3, 0);

    public TightRope() {
        super(RellekkaRooftopCourse.class, 3);
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 80;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {ObjectId.TIGHTROPE_14987 };
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 3;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        player.addWalkSteps(2626, 3654, -1, false);
        MarkOfGrace.spawn(player, RellekkaRooftopCourse.MARK_LOCATIONS, 40, 80);
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 40;
    }

    @Override
    public RenderAnimation getRenderAnimation() {
        return RENDER;
    }
}
