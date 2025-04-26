package com.zenyte.game.content.tombsofamascut.raid;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.tombsofamascut.encounter.MainHallEncounter;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.plugins.dialogue.PlainChat;

import java.util.List;
import java.util.Optional;

/**
 * @author Savions.
 */
public class TOASupplySelectInterface extends Interface {

	@Override protected void attach() {
		put(4, "Life container");
		put(6, "Life");
		put(7, "Chaos container");
		put(9, "Chaos");
		put(10, "Power container");
		put(12, "Power");
	}

	@Override public void open(Player player) {
		if (player.getArea() instanceof final MainHallEncounter mainHallEncounter && mainHallEncounter.getSupplyContainers() != null) {
			player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Life"), 0, 9, AccessMask.CLICK_OP1);
			player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Life container"), 2, 8, AccessMask.CLICK_OP1);
			player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Chaos"), 0, 9, AccessMask.CLICK_OP1);
			player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Chaos container"), 2, 8, AccessMask.CLICK_OP1);
			player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Power"), 0, 9, AccessMask.CLICK_OP1);
			player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Power container"), 2, 8, AccessMask.CLICK_OP1);
			for (Container container : mainHallEncounter.getSupplyContainers()) {
				player.getPacketDispatcher().sendUpdateItemContainer(container);
			}
		}
		super.open(player);
	}

	@Override protected void build() {
		bind("Life", player -> addContainer(player, 0));
		bind("Chaos", player -> addContainer(player, 1));
		bind("Power", player -> addContainer(player, 2));
	}

	private void addContainer(Player player, int index) {
		if (player.getArea() instanceof final MainHallEncounter mainHallEncounter && mainHallEncounter.getSupplyContainers() != null) {
			final List<Item> toAdd = mainHallEncounter.getSupplyContainers()[index].getItemsAsList();
			if (!player.getInventory().containsItem(ItemId.SUPPLIES)) {
				if (!player.getInventory().hasSpaceFor(ItemId.SUPPLIES)) {
					player.getDialogueManager().start(new PlainChat(player, "You need at least one inventory spot for a supply bag."));
				} else {
					final Container container = new Container(ContainerType.TOA_SUPPLY_BAG, ContainerPolicy.NEVER_STACK, 28, Optional.of(player));
					container.addAll(toAdd);
					player.getTOAManager().setSuppliesContainer(container);
					player.getTOAManager().setCanClaimSupplies(false);
					player.getInventory().addItem(new Item(ItemId.SUPPLIES));
				}
			} else if (player.getTOAManager().getSuppliesContainer() != null) {
				if (player.getTOAManager().getSuppliesContainer().getFreeSlotsSize() < toAdd.size()) {
					player.getDialogueManager().start(new PlainChat(player, "You need more space in your supply bag for additional supplies."));
				} else {
					player.getTOAManager().getSuppliesContainer().addAll(toAdd);
					player.getTOAManager().setCanClaimSupplies(false);
				}
			} else {
				player.getInventory().deleteItem(new Item(ItemId.SUPPLIES));
			}
			close(player);
		}
	}

	@Override public GameInterface getInterface() {
		return GameInterface.TOA_SUPPLIES_SHOP;
	}
}
