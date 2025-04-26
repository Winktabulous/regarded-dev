package com.zenyte.game.content.muddychest;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Joe
 */
@SuppressWarnings("unused")
public final class MuddyChest implements ObjectAction {

    private static final Item[] REWARDS = {
            new Item(ItemId.MITHRIL_BAR), new Item(ItemId.LAW_RUNE, 2), new Item(ItemId.ANCHOVY_PIZZA), new Item(ItemId.MITHRIL_DAGGER),
            new Item(ItemId.COINS_995, 50), new Item(ItemId.DEATH_RUNE, 2), new Item(ItemId.CHAOS_RUNE, 10), new Item(ItemId.UNCUT_RUBY)
    };
    private static final int muddyKeyItemId = ItemId.MUDDY_KEY;
    private static final Location CHEST_LOCATION = new Location(3089, 3859, 0);
    private static final WorldObject openChestObject = new WorldObject(171, 10, 1, CHEST_LOCATION);
    private static final Animation openAnimation = new Animation(536);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        Item muddyKey = new Item(muddyKeyItemId);
        if (!player.getInventory().containsItem(muddyKey)) {
            player.sendMessage("This chest is locked.");
            return;
        }

        player.getInventory().deleteItem(muddyKey);
        player.sendSound(52);
        player.setAnimation(openAnimation);
        player.lock(1);
        final WorldObject obj = new WorldObject(object);
        obj.setLocked(true);
        WorldTasksManager.schedule(() -> {
            player.sendMessage("You find some treasure in the chest!");
            player.getInventory().addOrDrop(REWARDS);

            final WorldObject openChest = new WorldObject(openChestObject);

            World.spawnObject(openChest);
            WorldTasksManager.schedule(() -> {
                World.spawnObject(obj);
                obj.setLocked(false);
            }, 1);
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{ObjectId.CLOSED_CHEST_170};
    }

}
