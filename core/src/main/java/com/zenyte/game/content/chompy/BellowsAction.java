package com.zenyte.game.content.chompy;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemChain;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Christopher
 * @since 3/6/2020
 */
public class BellowsAction extends Action {
    public static final Animation suckingAnim = new Animation(1026);
    public static final Graphics bellowsGfx = new Graphics(241, 0, 90);
    private static final int[] fillableBellows = ItemChain.OGRE_BELLOWS.getAllButLast();

    static {
        ArrayUtils.reverse(fillableBellows);
    }

    @Override
    public boolean start() {
        final Item bellows = getBellows();
        if (bellows == null) {
            player.sendMessage("You need some ogre bellows to suck the swamp bubbles.");
            return false;
        }
        return true;
    }

    @Override
    public boolean process() {
        return getBellows() != null;
    }

    @Override
    public int processWithDelay() {
        final Item currentBellows = getBellows();
        final int nextBellows = ItemId.OGRE_BELLOWS_3;
        final Inventory inventory = player.getInventory();
        player.lock(2);
        player.setAnimation(suckingAnim);
        player.setGraphics(bellowsGfx);
        inventory.replaceItem(nextBellows, 1, inventory.getContainer().getSlot(currentBellows));
        player.sendFilteredMessage("You collect some gas from the swamp.");
        return 2;
    }

    private Item getBellows() {
        final Inventory inventory = player.getInventory();
        if (player.carryingAny(fillableBellows)) {
            return inventory.getAny(fillableBellows);
        }
        return null;
    }
}
