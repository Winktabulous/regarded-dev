package com.zenyte.game.content.grandexchange;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.GameMode;
import mgi.types.config.enums.Enums;
import mgi.types.config.enums.IntEnum;

/**
 * @author Kris | 05/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ItemSetsInterface extends Interface {
    @Override
    protected void attach() {
        put(2, "Interact");
    }

    @Override
    public void open(Player player) {
        final IntEnum e = Enums.ITEM_SETS;
        player.getInterfaceHandler().sendInterface(this);
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Interact"), 0, e.getSize(), AccessMask.CLICK_OP1, AccessMask.CLICK_OP10);
        GameInterface.ITEM_SETS_INVENTORY_INTERFACE.open(player);
    }

    @Override
    protected void build() {
        bind("Interact", (player, slotId, itemId, option) -> {
            if (player.getGameMode().equals(GameMode.ULTIMATE_IRON_MAN)) {
                player.sendMessage("Ultimate Iron Men may not save inventory space with item sets.");
                return;
            }
            if (option == 10) {
                ItemUtil.sendItemExamine(player, itemId);
                return;
            }
            if (player.getInventory().containsItems(ItemSets.getItems(itemId))) {
                ItemSets.pack(player, itemId);
            } else {
                player.sendMessage(ItemSets.contents(itemId));
            }
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.ITEM_SETS;
    }
}
