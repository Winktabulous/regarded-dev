package com.zenyte.game.content.tombsofamascut.raid;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.SwitchPlugin;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;

/**
 * @author Savions.
 */
public class TOASupplyBagInterface extends Interface implements SwitchPlugin {

	@Override public void open(Player player) {
		if (player.getTOAManager().getSuppliesContainer() != null) {
			player.getPacketDispatcher().sendUpdateItemContainer(player.getTOAManager().getSuppliesContainer());
		}
		super.open(player);
		player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Container"), 0, 27, AccessMask.CLICK_OP1, AccessMask.CLICK_OP2, AccessMask.CLICK_OP3, AccessMask.CLICK_OP4, AccessMask.CLICK_OP9, AccessMask.DRAG_DEPTH1, AccessMask.DRAG_TARGETABLE);
	}

	@Override protected void attach() {
		put(5, "Container");
	}

	@Override protected void build() {
		bind("Container", (player, slotId, itemId, option) -> player.getTOAManager().withdrawSpecificSupplies(slotId, option));
	}

	@Override public GameInterface getInterface() {
		return GameInterface.TOA_SUPPLIES_INV;
	}

	@Override public boolean switchItem(Player player, int fromComponent, int toComponent, int fromSlot, int toSlot) {
		final Container container = player.getTOAManager().getSuppliesContainer();
		if (container != null) {
			final Item from = container.get(fromSlot);
			final Item to = container.get(toSlot);
			if (from != null && to != null) {
				container.set(fromSlot, to);
				container.set(toSlot, from);
				container.refresh(player);
			}
		}
		return true;
	}
}
