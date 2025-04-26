package com.zenyte.game.content.event.christmas2019;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 17/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SnowObject implements ObjectAction {

    private static final Animation animation = new Animation(15095);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (!player.getInventory().hasFreeSlots() && !player.getInventory().containsItem(ItemId.SNOWBALL, 1)) {
            player.sendMessage("You need some free space to collect the snow.");
            return;
        }
        if (player.getNumericTemporaryAttribute("snowball collection delay").intValue() >= WorldThread.WORLD_CYCLE) {
            return;
        }
        player.addTemporaryAttribute("snowball collection delay", WorldThread.WORLD_CYCLE + 2);
        player.faceObject(object);
        player.setInvalidAnimation(animation);
        player.getInventory().addItem(new Item(ItemId.SNOWBALL, Utils.random(3, 10)));
    }

    @Override
    public int getDelay() {
        return 1;
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {
                ChristmasConstants.SNOW_OBJECT
        };
    }
}
