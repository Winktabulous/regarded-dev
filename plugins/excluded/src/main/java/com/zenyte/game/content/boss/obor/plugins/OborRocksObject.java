package com.zenyte.game.content.boss.obor.plugins;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 14/05/2019 | 14:41
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class OborRocksObject implements ObjectAction {

    private static final Animation DOWN = new Animation(1148);

    private static final Animation UP = new Animation(740);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.lock(3);
        final Location location = new Location(player.getLocation());
        if (player.getY() > object.getY()) {
            location.setLocation(location.getX(), location.getY() - 3, 0);
            player.sendMessage("You climb into the pit.");
            player.setAnimation(DOWN);
        } else {
            location.setLocation(location.getX(), location.getY() + 3, 0);
            player.sendMessage("You climb out of the pit.");
            player.setAnimation(UP);
            WorldTasksManager.schedule(() -> player.setAnimation(Animation.STOP), 2);
        }
        player.autoForceMovement(location, 0, 90, ForceMovement.NORTH);
    }

    @Override
    public int getDelay() {
        return 2;
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.ROCKS_29491 };
    }
}
