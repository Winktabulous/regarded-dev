package com.zenyte.game.content.skills.prayer.ectofuntus;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 23/06/2019 15:28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BucketOnSlimePool implements ItemOnObjectAction {

    @Override
    public void handleItemOnObjectAction(final Player player, final Item item, final int slot, final WorldObject object) {
        player.getActionManager().setAction(new SlimeCollecting());
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 1925 };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.POOL_OF_SLIME, ObjectId.POOL_OF_SLIME_17117, ObjectId.POOL_OF_SLIME_17118, ObjectId.POOL_OF_SLIME_17119 };
    }
}
