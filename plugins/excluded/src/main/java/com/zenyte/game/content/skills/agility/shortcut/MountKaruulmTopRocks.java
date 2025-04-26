package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 10/05/2019 20:07
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MountKaruulmTopRocks implements Shortcut {
    private static final Animation CLIMB = new Animation(740);

    @Override
    public int getLevel(final WorldObject object) {
        return 62;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {34396};
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 7;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        if (object.getY() <= 3788) {
            final Location destination = new Location(1324, 3790, 0);
            ForceMovement forceMovement = new ForceMovement(destination, 60, ForceMovement.NORTH);
            player.setAnimation(CLIMB);
            player.setForceMovement(forceMovement);
            WorldTasksManager.schedule(() -> {
                player.setAnimation(Animation.STOP);
                player.setLocation(destination);
                WorldTasksManager.schedule(() -> {
                    player.addWalkSteps(player.getX(), player.getY() + 1, 1, false);
                    WorldTasksManager.schedule(() -> {
                        final Location dest = new Location(1324, 3795, 0);
                        ForceMovement fm = new ForceMovement(dest, 90, ForceMovement.NORTH);
                        player.setAnimation(CLIMB);
                        player.setForceMovement(fm);
                        WorldTasksManager.schedule(() -> {
                            player.setAnimation(Animation.STOP);
                            player.setLocation(dest);
                        }, 2);
                    });
                });
            }, 1);
        } else {
            final Location destination = new Location(1324, 3791, 0);
            ForceMovement forceMovement = new ForceMovement(destination, 90, ForceMovement.NORTH);
            player.setAnimation(CLIMB);
            player.setForceMovement(forceMovement);
            WorldTasksManager.schedule(() -> {
                player.setAnimation(Animation.STOP);
                player.setLocation(destination);
                WorldTasksManager.schedule(() -> {
                    player.addWalkSteps(player.getX(), player.getY() - 1, 1, false);
                    WorldTasksManager.schedule(() -> {
                        final Location dest = new Location(1324, 3787, 0);
                        ForceMovement fm = new ForceMovement(dest, 60, ForceMovement.NORTH);
                        player.setAnimation(CLIMB);
                        player.setForceMovement(fm);
                        WorldTasksManager.schedule(() -> {
                            player.setAnimation(Animation.STOP);
                            player.setLocation(dest);
                        }, 1);
                    });
                });
            }, 2);
        }
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
