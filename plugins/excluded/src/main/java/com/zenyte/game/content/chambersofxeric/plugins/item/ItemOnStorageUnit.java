package com.zenyte.game.content.chambersofxeric.plugins.item;

import com.zenyte.game.content.chambersofxeric.storageunit.StorageUnit;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 06/08/2019 13:18
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ItemOnStorageUnit implements ItemOnObjectAction {
    @Override
    public void handleItemOnObjectAction(final Player player, final Item item, final int slot, final WorldObject object) {
        player.getRaid().ifPresent(raid -> {
            if (object.getId() == StorageUnit.UNIT_HOTSPOT_OBJECT) {
                StorageUnit.openCreationMenu(player, object);
            } else {
                raid.constructOrGetSharedStorage().open(player);
            }
        });
    }

    @Override
    public Object[] getItems() {
        return null;
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{
                StorageUnit.UNIT_HOTSPOT_OBJECT, StorageUnit.SMALL_STORAGE_UNIT.getObjectId(), StorageUnit.MEDIUM_STORAGE_UNIT.getObjectId(),
                StorageUnit.LARGE_STORAGE_UNIT.getObjectId()
        };
    }
}
