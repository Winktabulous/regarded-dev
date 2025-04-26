package com.zenyte.game.content.skills.prayer.ectofuntus;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 26/06/2019 13:00
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SilverBarOnLoader implements ItemOnObjectAction {

    private static final Animation deposit = new Animation(1649);

    @Override
    public void handleItemOnObjectAction(final Player player, final Item item, final int slot, final WorldObject object) {
        player.getActionManager().setAction(new Action() {

            @Override
            public boolean start() {
                return true;
            }

            @Override
            public boolean process() {
                return player.getInventory().containsItem(item);
            }

            @Override
            public int processWithDelay() {
                player.setFaceLocation(new Location(3660, 3525, 1));
                player.setAnimation(deposit);
                player.getInventory().deleteItem(item);
                WorldTasksManager.schedule(() -> player.getInventory().addOrDrop(new Item(7650, 1)), 1);
                return 4;
            }
        });
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 2355 };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LOADER };
    }
}
