package com.zenyte.game.world.region.area.forthos;

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
 * @author Andys1814
 */
public final class ForthosShortcutObstacle implements Shortcut {

    private static final ObjectDefinitions defs = ObjectDefinitions.get(34834);

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        if (player.isFrozen()) {
            player.sendMessage("A magical force stops you from moving.");
            return;
        }

        float preciseX = object.getPreciseCoordFaceX(defs.getSizeX(), defs.getSizeY(), object.getRotation());
        float preciseY = object.getPreciseCoordFaceY(defs.getSizeX(), defs.getSizeY(), object.getRotation());
        int direction = DirectionUtil.getFaceDirection(preciseX - player.getX(), preciseY - player.getY());

        player.faceObject(object);
        player.setAnimation(Animation.JUMP);

        int endY = player.getY() == 9945 ? 9947 : 9945;
        Location end = new Location(player.getX(), endY);

        player.setForceMovement(new ForceMovement(player.getLocation(), 15, end, 35, direction));
        WorldTasksManager.schedule(() -> {
            player.setLocation(end);
        }, 0);
    }


    @Override
    public int getLevel(final WorldObject object) {
        return 75;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 34834 };
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
