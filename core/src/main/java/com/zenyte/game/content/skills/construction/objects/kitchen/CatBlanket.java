package com.zenyte.game.content.skills.construction.objects.kitchen;

import com.zenyte.game.content.follower.Follower;
import com.zenyte.game.content.follower.Pet;
import com.zenyte.game.content.follower.PetWrapper;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 24. veebr 2018 : 17:34.48
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class CatBlanket implements ItemOnObjectAction {

    private static final Animation PUTTING_CAT_DOWN = new Animation(827);

    private static final Animation FALLING_ASLEEP = new Animation(2160);

    private static final Animation SLEEPING = new Animation(2159);

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.PET_BLANKET, ObjectId.PET_BASKET, ObjectId.PET_BASKET_13576 };
    }

    @Override
    public void handleItemOnObjectAction(final Player player, final Item item, int slot, final WorldObject object) {
        final Pet pet = PetWrapper.getByItem(item.getId());
        if (pet == null) {
            return;
        }
        if (player.getCurrentHouse() == null) {
            return;
        }
        if (player.getCurrentHouse().isBuildingMode()) {
            return;
        }
        player.getInventory().deleteItem(item);
        player.getCurrentHouse().getCatsOnBlanket().put(player, item.getId());
        player.setAnimation(PUTTING_CAT_DOWN);
        WorldTasksManager.schedule(new WorldTask() {

            private int ticks;

            private Follower cat;

            @Override
            public void run() {
                if (cat != null && !cat.isLocked()) {
                    stop();
                    return;
                }
                switch(ticks++) {
                    case 0:
                        cat = new Follower(pet.petId(), player, object);
                        cat.spawn();
                        cat.lock();
                        cat.setAnimation(FALLING_ASLEEP);
                        break;
                    case 1:
                        cat.setAnimation(SLEEPING);
                        break;
                }
                if (ticks % 3 == 0) {
                    cat.setAnimation(SLEEPING);
                }
            }
        }, 0, 0);
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 1561, 1562, 1563, 1564, 1565, 1566 };
    }
}
