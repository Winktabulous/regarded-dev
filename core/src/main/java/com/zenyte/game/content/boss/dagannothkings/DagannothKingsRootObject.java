package com.zenyte.game.content.boss.dagannothkings;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 24/04/2019 23:55
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DagannothKingsRootObject implements ObjectAction {

    private static final Animation animation = new Animation(839);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.setAnimation(animation);
        player.lock(3);
        player.autoForceMovement(new Location(player.getX(), player.getY() >= 4367 ? 4365 : 4367, player.getPlane()), 30);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.ROOT_30170 };
    }
}
