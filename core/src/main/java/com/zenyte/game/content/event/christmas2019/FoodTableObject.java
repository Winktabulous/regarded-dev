package com.zenyte.game.content.event.christmas2019;

import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 24/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FoodTableObject implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equalsIgnoreCase("Take-food")) {
            if (!player.getInventory().hasFreeSlots()) {
                return;
            }
            player.setAnimation(new Animation(832));
            final Item item = new Item(Utils.random(ChristmasConstants.TURKEY_DRUMSTICK, ChristmasConstants.MULLED_WINE));
            player.getInventory().addOrDrop(item);
            player.sendMessage("You take " + (item.getId() == ChristmasConstants.ROAST_POTATOES ? "some" : "a") + " " + item.getName().toLowerCase() + " from the table.");
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 46104, 46103 };
    }
}
