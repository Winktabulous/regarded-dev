package com.zenyte.plugins.object;

import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 16 sep. 2018 | 17:33:33
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>
 */
public class PortPhasmatysEnergyBarrierObject implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (object.getRotation() == 1) {
            if (player.getY() >= object.getY() && player.getX() != 3669 && player.getX() != 3670) {
                player.addWalkSteps(player.getX() > 3669 ? 3670 : 3669, object.getY(), 5, false);
            } else if (player.getY() < object.getY() && player.getX() != 3669 && player.getX() != 3670) {
                player.addWalkSteps(player.getX() > 3669 ? 3669 : 3670, object.getY(), 5, false);
            }
        } else if (object.getRotation() == 2) {
            if (player.getY() >= object.getY() && player.getY() != 3485 && player.getY() != 3486) {
                player.addWalkSteps(object.getX(), player.getY() >= 3485 ? 3486 : 3485, 5, false);
            } else if (player.getY() < object.getY() && player.getY() != 3485 && player.getY() != 3486) {
                player.addWalkSteps(object.getX(), player.getY() >= 3485 ? 3486 : 3485, 5, false);
            }
        } else if (object.getRotation() == 3) {
            if (player.getY() >= object.getY() && player.getX() != 3659 && player.getX() != 3660) {
                player.addWalkSteps(player.getX() > 3659 ? 3660 : 3659, object.getY(), 5, false);
            } else if (player.getY() < object.getY() && player.getX() != 3659 && player.getX() != 3660) {
                player.addWalkSteps(player.getX() > 3659 ? 3659 : 3660, object.getY(), 5, false);
            }
        }
        WorldTasksManager.schedule(new WorldTask() {

            int ticks;

            @Override
            public void run() {
                Location location = null;
                int direction = -1;
                if (object.getRotation() == 1) {
                    location = new Location(player.getX(), object.getY() + (player.getY() <= object.getY() ? 1 : -1), 0);
                    direction = player.getY() <= object.getY() ? ForceMovement.NORTH : ForceMovement.SOUTH;
                } else if (object.getRotation() == 2) {
                    location = new Location(object.getX() + (player.getX() <= object.getX() ? 1 : -1), player.getY(), 0);
                    direction = player.getX() <= object.getX() ? ForceMovement.EAST : ForceMovement.WEST;
                } else if (object.getRotation() == 3) {
                    location = new Location(player.getX(), object.getY() + (player.getY() >= object.getY() ? -1 : 1), 0);
                    direction = player.getY() >= object.getY() ? ForceMovement.SOUTH : ForceMovement.NORTH;
                }
                if (ticks == 0) {
                    player.setForceMovement(new ForceMovement(player.getLocation(), 15, location, 90, direction));
                    player.setAnimation(new Animation(819));
                } else if (ticks == 1) {
                    player.setLocation(location);
                    stop();
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public int getDelay() {
        return 2;
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.ENERGY_BARRIER_16105 };
    }
}
