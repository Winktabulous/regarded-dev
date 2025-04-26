package com.zenyte.game.content.grandexchange;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 05/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ItemSetsInventoryInterface extends Interface {
    @Override
    protected void attach() {
        put(0, "Interact");
    }

    @Override
    public void open(Player player) {
        if (!player.getInterfaceHandler().isPresent(GameInterface.ITEM_SETS)) {
            throw new IllegalStateException();
        }
        player.getInterfaceHandler().sendInterface(this);
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Interact"), 0, 27, AccessMask.CLICK_OP1, AccessMask.CLICK_OP10);
    }

    @Override
    protected void build() {
        bind("Interact", (player, slotId, itemId, option) -> {
            if (option == 10) {
                ItemUtil.sendItemExamine(player, itemId);
            } else {
                ItemSets.unpack(player, itemId);
            }
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.ITEM_SETS_INVENTORY_INTERFACE;
    }
}
