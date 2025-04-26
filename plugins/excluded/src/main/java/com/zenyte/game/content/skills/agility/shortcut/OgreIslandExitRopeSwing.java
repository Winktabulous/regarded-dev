package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.ImmutableLocation;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Christopher
 * @since 1/26/2020
 */
public class OgreIslandExitRopeSwing implements Shortcut {
    private static final ImmutableLocation startLocation = new ImmutableLocation(2511, 3092, 0);
    private static final ImmutableLocation destination = new ImmutableLocation(2511, 3096, 0);
    private static final Animation swingingAnim = new Animation(751);
    private static final Animation ropeAnim = new Animation(497);
    private static final ForceMovement forceMovement = new ForceMovement(startLocation,
            30, destination, 60, ForceMovement.NORTH);
    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        player.setAnimation(swingingAnim);
        World.sendObjectAnimation(object, ropeAnim);
        player.setFaceLocation(forceMovement.getToFirstTile());
        player.setForceMovement(forceMovement);
        WorldTasksManager.schedule(() -> player.setLocation(forceMovement.getToSecondTile()), 1);
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return startLocation;
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 10;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 23570 };
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 2;
    }
}
