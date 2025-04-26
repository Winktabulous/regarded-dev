package com.zenyte.game.content.godwars.objects;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.ProjectileUtils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.CharacterLoop;

/**
 * @author Kris | 13/04/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class InstanceCrevice implements Shortcut {
    private static final Animation crawlInAnimation = new Animation(2594);
    private static final Animation crawlOutAnimation = new Animation(2595);

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        final int objectRotation = object.getRotation();

        Direction direction = null;
        if (objectRotation == 0) {
            direction = Direction.EAST;
        }
        if (objectRotation == 1) {
            direction = Direction.SOUTH;
        }
        if (objectRotation == 2) {
            direction = Direction.WEST;
        }
        if (objectRotation == 3) {
            direction = Direction.NORTH;
        }

        final Location closeExit = object.transform(direction, 2);
        final Location farExit = object.transform(direction, 3);
        //The other crevice will always be either 2 or 3 tiles away from the player, therefore let's depth-test it to find the destination.
        final Location destination = ProjectileUtils.isProjectileClipped(null, null, closeExit, farExit, true) ? farExit : closeExit;
        final Location center = object.transform((destination.getX() - object.getX()) / 2, (destination.getY() - object.getY()) / 2, 0);
        player.setMaximumTolerance(true);
        CharacterLoop.forEach(player.getLocation(), 12, NPC.class, npc -> {
            if (npc.getCombat().getTarget() == player) {
                npc.getCombat().removeTarget();
            }
        });
        player.setAnimation(crawlInAnimation);
        player.autoForceMovement(center, 60);
        WorldTasksManager.schedule(() -> player.getAppearance().setHideEquipment(true));
        WorldTasksManager.schedule(() -> player.autoForceMovement(destination, 60), 2);
        WorldTasksManager.schedule(() -> {
            player.getAppearance().setHideEquipment(false);
            player.setAnimation(crawlOutAnimation);
        }, 3);
    }

    @Override
    public void endSuccess(final Player player, final WorldObject object) {
        player.setMaximumTolerance(false);
        player.blockIncomingHits();
    }

    @Override
    public String getEndMessage(final boolean success) {
        return success ? "You climb your way through the narrow crevice." : null;
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 0;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {35013};
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 4;
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
