package com.zenyte.plugins.object;

import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.*;

/**
 * @author Kris | 12. dets 2017 : 17:52.57
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>}
 */
public final class WildernessCourseDoorObject implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        final WorldObject door = Door.handleGraphicalDoor(object, null);
        player.setRunSilent(true);
        player.lock();
        player.freeze(16, 16);
        player.addWalkSteps(2998, 3931, -1, false);
        player.getAppearance().setRenderAnimation(new RenderAnimation(RenderAnimation.STAND, 762, RenderAnimation.WALK));
        WorldTasksManager.schedule(new WorldTask() {

            int ticks;

            DoubleDoor gate;

            @Override
            public void run() {
                switch(ticks++) {
                    case 1:
                        Door.handleGraphicalDoor(door, object);
                        break;
                    case 13:
                        player.getAppearance().resetRenderAnimation();
                        gate = DoubleDoor.handleGraphicalDoubleDoor(player, new WorldObject(23552, 0, 3, new Location(2998, 3931, 0)), null);
                        break;
                    case 15:
                        player.setRunSilent(false);
                        player.unlock();
                        DoubleDoor.handleGraphicalDoubleDoor(player, object, gate);
                        stop();
                        break;
                }
            }
        }, 0, 0);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.DOOR_23555 };
    }
}
