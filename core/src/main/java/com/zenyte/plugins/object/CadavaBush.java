package com.zenyte.plugins.object;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.utils.TimeUnit;

/**
 * @author Kris | 10/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CadavaBush implements ObjectAction {

    private static final Animation animation = new Animation(832);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (player.getNumericTemporaryAttribute("berry pick delay").longValue() > System.currentTimeMillis()) {
            return;
        }
        if (!player.getInventory().hasFreeSlots()) {
            player.sendFilteredMessage("You need some more free inventory space.");
            return;
        }
        player.getTemporaryAttributes().put("berry pick delay", System.currentTimeMillis() + TimeUnit.TICKS.toMillis(2));
        player.lock(2);
        player.setAnimation(animation);
        player.getInventory().addOrDrop(new Item(753));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CADAVA_BUSH, ObjectId.CADAVA_BUSH_23626, ObjectId.CADAVA_BUSH_23627, 33183 };
    }
}
