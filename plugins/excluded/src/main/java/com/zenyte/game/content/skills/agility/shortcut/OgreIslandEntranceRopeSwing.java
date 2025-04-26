package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.WorldTask;
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
public class OgreIslandEntranceRopeSwing implements Shortcut {
    public static final Location destination = new ImmutableLocation(2505, 3087, 0);
    public static final Location startPosition = new ImmutableLocation(2501, 3087, 0);
    public static final int BRANCH = 23638;
    private static final int ROPE_BRANCH = 23571;
    private static final Animation throwingRope = new Animation(775, 20);
    private static final Animation ropeAnimation = new Animation(497);
    private static final Animation swingingAnimation = new Animation(751, 10);
    private static final ForceMovement forceMovement = new ForceMovement(startPosition, 40, destination, 20 + 30, ForceMovement.EAST);

    @Override
    public void startSuccess(Player player, WorldObject object) {
        final WorldObject obj = new WorldObject(object);
        obj.setId(ROPE_BRANCH);
        player.setFaceLocation(destination);
        player.getInventory().deleteItem(ItemId.ROPE, 1);
        WorldTasksManager.schedule(new WorldTask() {
            private int ticks;
            @Override
            public void run() {
                if (ticks == 0) {
                    player.setAnimation(throwingRope);
                } else if (ticks == 2) {
                    World.spawnTemporaryObject(obj, object, 3);
                } else if (ticks == 3) {
                    World.sendObjectAnimation(obj, ropeAnimation);
                    player.setAnimation(swingingAnimation);
                    player.setForceMovement(forceMovement);
                } else if (ticks == 5) {
                    player.setLocation(forceMovement.getToSecondTile());
                    stop();
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return startPosition;
    }

    @Override
    public String getStartMessage(final boolean success) {
        return "You tie the rope to the tree...";
    }

    @Override
    public String getEndMessage(final boolean success) {
        return "You skillfully swing across.";
    }

    @Override
    public int getLevel(WorldObject object) {
        return 10;
    }

    @Override
    public int[] getObjectIds() {
        return new int[0];
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 9;
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 0;
    }
}
