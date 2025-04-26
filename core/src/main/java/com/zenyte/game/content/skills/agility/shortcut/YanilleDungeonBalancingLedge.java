package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 15/09/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class YanilleDungeonBalancingLedge implements Shortcut {
    private static final RenderAnimation RENDER = new RenderAnimation(RenderAnimation.STAND, 756, RenderAnimation.WALK);
    private static final RenderAnimation BACKWARDS_RENDER = new RenderAnimation(RenderAnimation.STAND, 754, RenderAnimation.WALK);
    private static final Location northernStartLocation = new Location(2580, 9520, 0);
    private static final Location southernStartLocation = new Location(2580, 9512, 0);
    private static final Animation goingOnLedgeFromSouth = new Animation(753, 15);
    private static final Animation goingOnLedgeFromNorth = new Animation(752, 15);

    @Override
    public String getStartMessage(final boolean success) {
        return "You put your foot on the ledge and try to edge across...";
    }

    @Override
    public String getEndMessage(final boolean success) {
        return "You skillfully edge across the gap.";
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        if (player.getY() >= northernStartLocation.getY()) {
            return northernStartLocation;
        }
        return southernStartLocation;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        player.faceObject(object);
        final boolean fromNorth = getRouteEvent(player, object).matches(northernStartLocation);
        player.setAnimation(fromNorth ? goingOnLedgeFromNorth : goingOnLedgeFromSouth);
        player.getAppearance().setRenderAnimation(fromNorth ? BACKWARDS_RENDER : RENDER);
        final Location destination = fromNorth ? southernStartLocation : northernStartLocation;
        WorldTasksManager.schedule(() -> {
            player.setAnimation(Animation.STOP);
            player.addWalkSteps(destination.getX(), destination.getY(), -1, false);
        });
    }

    @Override
    public void endSuccess(final Player player, final WorldObject object) {
        player.getAppearance().resetRenderAnimation();
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 40;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {ObjectId.BALANCING_LEDGE_23548};
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 22;
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 8;
    }
}
