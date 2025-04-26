package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 26/04/2019 19:30
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TrollStrongholdPathRockyClimb implements Shortcut {

    private static final Animation CLIMB = new Animation(740);

    @Override
    public int getLevel(final WorldObject object) {
        return 0;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {
                3790, 3791
        };
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 2;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        Location destination;
        ForceMovement forceMovement;
        if (player.getX() >= 2862) {
            destination = player.getLocation().transform(object.getId() == 3791 ? 3 : -3, 0, 0);
            forceMovement = new ForceMovement(destination, 60, ForceMovement.EAST);
        } else {
            destination = player.getLocation().transform(object.getId() == 3790 ? 3 : -3, 0, 0);
            forceMovement = new ForceMovement(destination, 60, ForceMovement.WEST);
        }
        player.setAnimation(CLIMB);
        player.setForceMovement(forceMovement);
        WorldTasksManager.schedule(() -> {
            player.setAnimation(Animation.STOP);
            player.setLocation(destination);
        }, 1);
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
