package com.zenyte.game.content.skills.construction.objects.kitchen;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 24. veebr 2018 : 16:47.36
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class Barrel implements ItemOnObjectAction {

    @Override
    public void handleItemOnObjectAction(final Player player, final Item item, int slot, final WorldObject object) {
        player.sendInputInt("How many glasses would you like to fill?", amount -> player.getActionManager().setAction(new BarrelFillingAction(object, amount)));
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 1919 };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.BEER_BARREL, ObjectId.CIDER_BARREL, ObjectId.ASGARNIAN_ALE_13570, ObjectId.GREENMANS_ALE_13571, ObjectId.DRAGON_BITTER_13572, ObjectId.CHEFS_DELIGHT_13573 };
    }

    private static final class BarrelFillingAction extends Action {

        private static final Animation START_ANIM = new Animation(3660);

        private static final Animation[] ANIMS = new Animation[] { new Animation(3661), new Animation(3666), new Animation(3662), new Animation(3663), new Animation(3664), new Animation(3665) };

        private static final Item[] DRINKABLES = new Item[] { new Item(7740), new Item(7752), new Item(7744), new Item(7746), new Item(7748), new Item(7754) };

        public BarrelFillingAction(final WorldObject object, final int quantity) {
            this.object = object;
            this.quantity = quantity;
        }

        private final WorldObject object;

        private int quantity;

        @Override
        public boolean start() {
            return true;
        }

        @Override
        public boolean process() {
            if (!player.getInventory().containsItem(1919, 1)) {
                player.sendMessage("You've ran out of glasses.");
                return false;
            }
            return true;
        }

        @Override
        public int processWithDelay() {
            if (quantity-- <= 0) {
                return -1;
            }
            WorldTasksManager.schedule(new WorldTask() {

                private int loop;

                @Override
                public void run() {
                    if (loop == 0) {
                        player.setAnimation(START_ANIM);
                    } else {
                        player.getInventory().deleteItem(1919, 1);
                        final int index = object.getId() - 13568;
                        player.setAnimation(ANIMS[index]);
                        final Item item = DRINKABLES[index];
                        player.getInventory().addItem(item);
                        player.sendMessage("You pour the " + item.getName().toLowerCase() + " into the glass.");
                        stop();
                        return;
                    }
                    loop++;
                }
            }, 0, 0);
            return 2;
        }
    }
}
