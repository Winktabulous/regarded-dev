package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import mgi.types.config.ObjectDefinitions;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class CastlewarsSteppingStone implements Shortcut {

    private static final ObjectDefinitions defs = ObjectDefinitions.get(4411);

    // The start is the southern tile, always
    public static final Location WEST_START = new Location(2378, 3084, 0);
    public static final Location WEST_START_FLAG = new Location(2378, 3083, 0);

    public static final Location WEST_FINISH = new Location(2377, 3088, 0);
    public static final Location WEST_FINISH_FLAG = new Location(2377, 3089, 0);

    public static final Location EAST_FINISH = new Location(2418, 3125, 0);
    public static final Location EAST_FINISH_FLAG = new Location(2418, 3126, 0);

    public static final Location EAST_START = new Location(2420, 3123, 0);
    public static final Location EAST_START_FLAG = new Location(2420, 3122, 0);

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        if(player.isFrozen()) {
            player.sendMessage("A magical force stops you from moving.");
            return;
        }

        final float preciseX = object.getPreciseCoordFaceX(defs.getSizeX(), defs.getSizeY(), object.getRotation());
        final float preciseY = object.getPreciseCoordFaceY(defs.getSizeX(), defs.getSizeY(), object.getRotation());
        final int direction = DirectionUtil.getFaceDirection(preciseX - player.getX(), preciseY - player.getY());

        player.faceObject(object);
        player.setAnimation(Animation.JUMP);
        player.setForceMovement(new ForceMovement(player.getLocation(), 15, object, 35, direction));
        WorldTasksManager.schedule(() -> {
            player.setLocation(object);
        }, 0);
    }


    @Override
    public int getLevel(final WorldObject object) {
        return 1;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 4411 };
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 1;
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
