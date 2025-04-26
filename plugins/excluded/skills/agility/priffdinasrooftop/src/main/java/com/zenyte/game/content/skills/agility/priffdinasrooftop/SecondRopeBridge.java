package com.zenyte.game.content.skills.agility.priffdinasrooftop;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author R-Y-M-R
 * @date 2/11/2022
 * @see <a href="https://www.rune-server.ee/members/necrotic/">RuneServer</a>
 */
public final class SecondRopeBridge extends AgilityCourseObstacle {

    private static final RenderAnimation RENDER = new RenderAnimation(RenderAnimation.STAND, 762, RenderAnimation.WALK);
    private static final Location START_LOC = new Location(3270, 6150, 2);
    private static final Location END_LOC = new Location(3270, 6158, 2);
    private static final int WALK_TIME = 7;
    private static final SoundEffect ROPE_BRIDGE_SOUND = new SoundEffect(2470);

    public SecondRopeBridge() {
        super(PriffdinasRooftopCourse.class, 9);
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return START_LOC;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        if (player.getNumericTemporaryAttribute("prif_portal").intValue() == 5) {
            player.getVarManager().sendBit(9298, 5);
        }
        player.getPacketDispatcher().sendSoundEffect(ROPE_BRIDGE_SOUND);
        player.addWalkSteps(END_LOC.getX(), END_LOC.getY(), -1, false);
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 75;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 36235 };
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 25.6;
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return WALK_TIME;
    }

    @Override
    public RenderAnimation getRenderAnimation() {
        return RENDER;
    }

}