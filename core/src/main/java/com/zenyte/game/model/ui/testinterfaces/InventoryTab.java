package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.item.ItemActionHandler;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.SwitchPlugin;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import mgi.types.config.enums.Enums;

/**
 * @author Kris | 16/04/2019 16:57
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class InventoryTab extends Interface implements SwitchPlugin {
    @Override
    protected void attach() {
        put(0, "Items layer");
    }

    @Override
    public void open(final Player player) {
        player.getInterfaceHandler().sendInterface(this);
        player.getPacketDispatcher().sendComponentSettings(getInterface(),
                getComponent("Items layer"),
                0,
                Container.getSize(ContainerType.INVENTORY),
                AccessMask.CLICK_OP2, AccessMask.CLICK_OP3, AccessMask.CLICK_OP4, AccessMask.CLICK_OP6,
                AccessMask.CLICK_OP7, AccessMask.CLICK_OP10, AccessMask.USE_ON_GROUND_ITEMS,
                AccessMask.USE_ON_NPCS, AccessMask.USE_ON_OBJECTS, AccessMask.USE_ON_PLAYERS,
                AccessMask.USE_ON_INVENTORY, AccessMask.USE_ON_COMPONENT, AccessMask.DRAG_DEPTH1,
                AccessMask.DRAG_TARGETABLE, AccessMask.COMPONENT_TARGETABLE
                );
    }

    @Override
    protected void build() {
        bind("Items layer", (player, slotId, itemId, option) -> {
            final int opheld = Enums.OPHELD_TO_IFBUTTON.getReverseValues().getOrDefault(option, option);
            if (opheld == 0) return;
            ItemActionHandler.handle(player, itemId, slotId, opheld);
        });
        bind("Items layer", "Items layer", ((player, fromSlot, toSlot) -> player.getInventory().switchItem(fromSlot, toSlot)));
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.INVENTORY_TAB;
    }
}
