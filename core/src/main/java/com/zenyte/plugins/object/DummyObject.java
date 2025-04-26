package com.zenyte.plugins.object;

import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 22. aug 2018 : 00:12:23
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class DummyObject implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.lock();
        WorldTasksManager.schedule(new TickTask() {

            @Override
            public void run() {
                if (ticks == 0) {
                    player.faceObject(object);
                    player.setAnimation(new Animation(player.getEquipment().getAttackAnimation(player.getCombatDefinitions().getStyle())));
                } else if (ticks == 2) {
                    player.sendMessage("There is nothing more you can learn from hitting a dummy.");
                    player.unlock();
                    stop();
                    return;
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.DUMMY_1764 };
    }
}
