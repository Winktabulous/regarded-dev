package com.zenyte.plugins.object;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 27/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class VanillaPlant implements ObjectAction {

    private static final Animation animation = new Animation(832);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.sendFilteredMessage("You search through the leaves of the vanila plant.");
        player.lock(2);
        WorldTasksManager.schedule(() -> {
            player.sendMessage("You pick a vanilla pod.");
            player.sendSound(2581);
            player.setAnimation(animation);
            player.getInventory().addOrDrop(new Item(ItemId.VANILLA_POD));
        }, 1);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {
                ObjectId.VANILLA_PLANT
        };
    }
}
