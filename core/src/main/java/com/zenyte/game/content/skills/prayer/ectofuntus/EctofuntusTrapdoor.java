package com.zenyte.game.content.skills.prayer.ectofuntus;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 23/06/2019 15:14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EctofuntusTrapdoor implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equalsIgnoreCase("Open")) {
            final WorldObject obj = new WorldObject(object);
            obj.setId(16114);
            World.spawnObject(obj);
        } else if (option.equalsIgnoreCase("Close")) {
            final WorldObject obj = new WorldObject(object);
            obj.setId(16113);
            World.spawnObject(obj);
        } else if (option.equalsIgnoreCase("Climb-down")) {
            player.lock(1);
            player.setAnimation(new Animation(827));
            WorldTasksManager.schedule(() -> player.setLocation(new Location(3669, 9888, 3)));
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.TRAPDOOR_16113, ObjectId.TRAPDOOR_16114 };
    }
}
