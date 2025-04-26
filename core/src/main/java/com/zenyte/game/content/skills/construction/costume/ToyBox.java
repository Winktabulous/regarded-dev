package com.zenyte.game.content.skills.construction.costume;

import com.google.gson.annotations.Expose;
import com.zenyte.game.content.magicstorageunit.StorableSetPiece;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import mgi.types.config.enums.EnumDefinitions;
import mgi.types.config.items.ItemDefinitions;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kris | 27. veebr 2018 : 22:24.22
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ToyBox {
	
	private transient Player player;
	@Expose private final Map<Integer, int[]> items = new HashMap<>(ToyBoxData.VALUES.length);
	private transient int page;
	
	/**
	 * Takes an armour set from the toy box if possible.
	 * @param slotId slot id of the armour on the interface.
	 */
	public void takeSet(final int slotId) {
		final Item item = ToyBoxData.CONTAINERS[page].get(slotId / 4);
		if (item == null) {
			return;
		}
		final ToyBoxData set = ToyBoxData.DISPLAY_MAP.get(item.getId());
		if (set == null) {
			return;
		}
		final int display = set.getDisplayItem();
		final int[] items = this.items.get(display);
		if (items == null) {
			return;
		}
		if (player.getInventory().getFreeSlots() < items.length) {
			player.sendMessage("You need at least " + items.length + " free inventory spaces to withdraw this set.");
			return;
		}
		final EnumDefinitions map = EnumDefinitions.get(380);
		final String val = map.getStringValue(display);
		if (val == null) {
			return;
		}
		for (final int it : items) {
			player.getInventory().addItem(it, 1);
		}
		player.sendMessage("You remove the " + val.toLowerCase() + " from the toy box.");
		this.items.remove(display);
		refresh();
	}
	
	/**
	 * Adds a set to the armour box.
	 * @param itemId a piece of the set to enqueue.
	 * @return whether it was successfully added or not.
	 */
	public boolean addSet(final int itemId) {
		final ToyBoxData set = ToyBoxData.MAP.get(itemId);
		if (set == null) {
			return false;
		}
		final int display = set.getDisplayItem();
		if (items.containsKey(display)) {
			player.sendMessage("Your toy box already contains that.");
			return false;
		}
		final StorableSetPiece[] pieces = set.getPieces();
		final int[] items = new int[pieces.length];
		for (int i = pieces.length - 1; i >= 0; i--) {
			final StorableSetPiece piece = pieces[i];
			final int p = getPiece(piece);
			if (p == -1) {
				notify(set);
				return false;
			}
			items[i] = p;
		}
		final EnumDefinitions map = EnumDefinitions.get(380);
		final String val = map.getStringValue(display);
		if (val == null) {
			return false;
		}
		for (int i = pieces.length - 1; i >= 0; i--) {
			final int item = items[i];
			player.getInventory().deleteItem(item, 1);
		}
		player.sendMessage("You place the " + val.toLowerCase() + " into the toy box.");
		this.items.put(display, items);
		refresh();
		return true;
	}
	
	/**
	 * Gets the id of the first piece that the player has in their inventory.
	 * @param piece the Piece object from the enum.
	 * @return id of the piece if the player has, otherwise -1.
	 */
	private int getPiece(final StorableSetPiece piece) {
		final int[] ids = piece.getIds();
		for (int i = ids.length - 1; i >= 0; i--) {
			if (player.getInventory().containsItem(ids[i], 1)) {
				return ids[i];
			}
		}
		return -1;
	}
	
	/**
	 * Notifies the player about the pieces required for this set.
	 * Pieces they have are marked green, whereas the missing ones are red.
	 * @param set the armour set to notify about.
	 */
	private void notify(final ToyBoxData set) {
		final EnumDefinitions map = EnumDefinitions.get(380);
		final String val = map.getStringValue(set.getDisplayItem());
		if (val == null) {
			return;
		}
		final Inventory inventory = player.getInventory();
		player.sendMessage("You need the following items to enqueue the " + val.toLowerCase() + " to your toy box:");
		for (final StorableSetPiece piece : set.getPieces()) {
			final int[] ids = piece.getIds();
			if (ids.length == 1) {
				final String prefix = inventory.containsItem(ids[0], 1) ? "<col=00ff00>" : "<col=ff0000>";
				player.sendMessage(prefix + "1 x " + ItemDefinitions.get(ids[0]).getName());
			} else {
				final String prefix = inventory.containsItem(ids[0], 1) ? "<col=00ff00>" : "<col=ff0000>";
				final String prefix2 = inventory.containsItem(ids[1], 1) ? "<col=00ff00>" : "<col=ff0000>";
				player.sendMessage(prefix + "1 x " + ItemDefinitions.get(ids[0]).getName() + "</col> or " + 
						prefix2 + "1 x " + ItemDefinitions.get(ids[1]).getName());
			}
		}
	}
	
	/**
	 * Refreshes the armour set interface values.
	 */
	private void refresh() {
		final ToyBoxData[] data = ToyBoxData.VALUES;
		int firstHash = 0;
		int secondHash = 0;
		int count = page == 0 ? 0 : 1;
		for (int i = (page * 39); i < (page * 39) + 39; i++) {
			if (i >= data.length) {
				break;
			}
			final ToyBoxData d = data[i];
			if (items.containsKey(d.getDisplayItem())) {
				if (count < 32) {
					firstHash = Utils.getShiftedValue(firstHash, count);
				} else {
					secondHash = Utils.getShiftedValue(firstHash, count - 32);
				}
			}
			count++;
		}
		secondHash = Utils.getShiftedValue(secondHash, 8);
		if (page > 0) {
			firstHash = Utils.getShiftedValue(firstHash, 0);
		}
		player.getPacketDispatcher().sendClientScript(417, 100, firstHash, secondHash, "Toy box");
		player.getPacketDispatcher().sendComponentSettings(592, 2, 0, 156, AccessMask.CLICK_OP1, AccessMask.CLICK_OP10);
	}
	
	/**
	 * Opens the toy box interface on the specificied page.
	 * @param page the page to open up at.
	 */
	public void open(final int page) {
		player.getTemporaryAttributes().put("costumeRoomObject", "TOY_BOX");
		this.page = page;
		player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 592);
		player.getPacketDispatcher().sendUpdateItemContainer(100, ToyBoxData.CONTAINERS[page]);
		refresh();
	}
	
	public void setPlayer(Player player) {
	    this.player = player;
	}
	
	public Map<Integer, int[]> getItems() {
	    return items;
	}
	
	public int getPage() {
	    return page;
	}

}
