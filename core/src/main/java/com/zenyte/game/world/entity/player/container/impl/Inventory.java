package com.zenyte.game.world.entity.player.container.impl;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.ContainerWrapper;
import com.zenyte.game.world.region.area.plugins.TempPlayerStatePlugin;
import com.zenyte.plugins.Listener;
import com.zenyte.plugins.ListenerType;
import com.zenyte.plugins.PluginManager;
import com.zenyte.plugins.events.InventoryItemSwitchEvent;

import java.util.Optional;

public class Inventory extends ContainerWrapper implements TempPlayerStatePlugin.State {

	public static final int SIZE = 28;
	public static final int INTERFACE = 149;

	private final boolean tempVariant;

	public Inventory(final Player player) {
		this(player, false);
	}

	public Inventory(final Player player, boolean tempVariant) {
		this.player = player;
		this.tempVariant = tempVariant;
		container = new Container(ContainerPolicy.NORMAL, ContainerType.INVENTORY, Optional.of(player));
	}

	public void setInventory(final Inventory inventory) {
		container.setContainer(inventory.container);
	}

	@Listener(type = ListenerType.INTERFACE_SWITCH)
	static void invalidSwitchPlugin(final Player player, final int fromInterface, final int toInterface, final int fromComponent,
			final int toComponent, final int fromSlot, final int toSlot) {
		if (!((fromInterface == 15 || fromInterface == 551 || fromInterface == 192)
				&& fromInterface != toInterface)) {
			return;
		}
		player.getInventory().refreshAll();
	}

	@Listener(type = ListenerType.INTERFACE_SWITCH)
	static void switchPlugin(final Player player, final int fromInterface, final int toInterface, final int fromComponent,
			final int toComponent, final int fromSlot, final int toSlot) {
		if (!(fromInterface == 15 && toInterface == 15
				|| fromInterface == 551 && toInterface == 551 || fromInterface == 192 && toInterface == 192
				|| fromInterface == 85 && toInterface == 85 || fromInterface == 149 && toInterface == 149)) {
			return;
		}
		if (player.isLocked()) {
		    player.getInventory().refreshAll();
		    return;
        }
		if (fromSlot >= 0 && fromSlot <= Inventory.SIZE && toSlot >= 0 && toSlot <= Inventory.SIZE && toSlot != fromSlot) {
			player.getInventory().switchItem(fromSlot, toSlot);
		}
	}

	@Override
	public void switchItem(int fromSlot, int toSlot) {
		super.switchItem(fromSlot, toSlot);
		PluginManager.post(new InventoryItemSwitchEvent(player, fromSlot, toSlot));
	}

	public boolean checkSpace() {
		if (!hasFreeSlots()) {
			player.sendMessage("Not enough space in your inventory.");
			return false;
		}
		return true;
	}

	public boolean checkSpace(final int space) {
		if (getFreeSlots() < space) {
			player.sendMessage("Not enough space in your inventory.");
			return false;
		}
		return true;
	}

	@Override
	public boolean isTempVariant() {
		return tempVariant;
	}

	@Override
	public TempPlayerStatePlugin.StateType tempType() {
		return TempPlayerStatePlugin.StateType.INVENTORY;
	}
}
