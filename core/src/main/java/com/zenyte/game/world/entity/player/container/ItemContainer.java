package com.zenyte.game.world.entity.player.container;

import com.google.gson.annotations.Expose;
import com.zenyte.game.item.Item;

import java.util.Arrays;
import java.util.List;

public class ItemContainer {

	@Expose
	private Item[] items;

	private boolean alwaysStackable;

	public ItemContainer(final int size, final boolean alwaysStackable) {
		items = new Item[size];
		this.alwaysStackable = alwaysStackable;
	}

	public ItemContainer(final Item[] items) {
		this.items = items;
	}

	public ItemContainer(final ItemContainer container) {
		final Item[] items = new Item[container.getItems().length];
		System.arraycopy(container.items, 0, items, 0, items.length);
		this.items = items;
		alwaysStackable = container.isAlwaysStackable();
	}

	public void shift() {
		final Item[] oldData = items;
		items = new Item[oldData.length];
		int ptr = 0;
		for (int i = 0; i < items.length; i++) {
			if (oldData[i] != null) {
				items[ptr++] = oldData[i];
			}
		}
	}

	public Item get(final int slot) {
		if (slot < 0 || slot >= items.length) {
			return null;
		}
		return items[slot];
	}

	/*
	 * public void set(int slot, T item) { if (slot < 0 || slot >= items.length)
	 * { return; } items[slot] = item; }
	 */

	public void set(final int slot, final Item item) {
		if (slot < 0 || slot >= items.length) {
			return;
		}
		items[slot] = item;
	}

	public boolean forceAdd(final Item item) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				items[i] = item;
				return true;
			}
		}
		return false;
	}

	public boolean add(final Item item) {
		if (alwaysStackable || item.getDefinitions() != null && item.getDefinitions().isStackable()) {
			for (int i = 0; i < items.length; i++) {
				if (items[i] != null) {
					if (items[i].getId() == item.getId()) {
						items[i] = new Item(items[i].getId(), items[i].getAmount() + item.getAmount());
						return true;
					}
				}
			}
		} else {
			if (item.getAmount() > 1) {
				if (freeSlots() >= item.getAmount()) {
					for (int i = 0; i < item.getAmount(); i++) {
						final int index = freeSlot();
						items[index] = new Item(item.getId(), 1, item.getCharges());
					}
					return true;
				} else {
					return false;
				}
			}
		}
		final int index = freeSlot();
		if (index == -1) {
			return false;
		}
		items[index] = item;
		return true;
	}

	public int freeSlots() {
		int j = 0;
		for (final Item aData : items) {
			if (aData == null) {
				j++;
			}
		}
		return j;
	}

	public int remove(final Item item) {
		if (item.getId() == -1) {
			return 0;
		}
		int removed = 0, toRemove = item.getAmount();
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				if (items[i].getId() == item.getId()) {
					int amt = items[i].getAmount();
					if (amt > toRemove) {
						removed += toRemove;
						amt -= toRemove;
						toRemove = 0;
						items[i] = new Item(items[i].getId(), amt, items[i].getCharges());
						return removed;
					} else {
						removed += amt;
						toRemove -= amt;
						items[i] = null;
					}
				}
			}
		}
		return removed;
	}

	public void removeAll(final Item item) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				if (items[i].getId() == item.getId()) {
					items[i] = null;
				}
			}
		}
	}

	public boolean containsOne(final Item item) {
		for (final Item aData : items) {
			if (aData != null) {
				if (aData.getId() == item.getId()) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean containsOne(final int id) {
		for (final Item aData : items) {
			if (aData != null) {
				if (aData.getId() == id) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean contains(final Item item) {
		int amtOf = 0;
		for (final Item aData : items) {
			if (aData != null) {
				if (item.getId() == -1) {
					continue;
				}
				if (aData.getId() == item.getId()) {
					amtOf += aData.getAmount();
				}
			}
		}
		return amtOf >= item.getAmount();
	}

	public int freeSlot() {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				return i;
			}
		}
		return -1;
	}

	public void clear() {
		Arrays.fill(items, null);
	}

	public int getSize() {
		return items.length;
	}

	public int getFreeSlots() {
		int s = 0;
		for (final Item aData : items) {
			if (aData == null) {
				s++;
			}
		}
		return s;
	}

	public int getFreeSlots(final Item... items) {
		int s = 0;
		for (final Item aData : items) {
			if (aData != null) {
				for (final Item item : items) {
					if (item.getId() == aData.getId()) {
						s++;
					}
				}
			} else if (aData == null) {
				s++;
			}
		}
		return s;
	}

	public int getUsedSlots() {
		int s = 0;
		for (final Item aData : items) {
			if (aData != null) {
				s++;
			}
		}
		return s;
	}

	public int getNumberOf(final Item item) {
		int count = 0;
		for (final Item aData : items) {
			if (aData != null) {
				if (aData.getId() == item.getId()) {
					count += aData.getAmount();
				}
			}
		}
		return count;
	}

	public int getNumberOf(final int item) {
		int count = 0;
		for (final Item aData : items) {
			if (aData != null) {
				if (aData.getId() == item) {
					count += aData.getAmount();
				}
			}
		}
		return count;
	}

	public Item[] getItems() {
		return items;
	}

	public Item[] getShiftedItem() {
		int ptr = 0;
		for (final Item item : items) {
			if (item == null) {
				continue;
			}
			ptr++;
		}
		final Item[] items = new Item[ptr];
		ptr = 0;
		for (final Item item : items) {
			if (item == null) {
				continue;
			}
			items[ptr++] = item;
		}
		return items;
	}

	public Item[] getItemsCopy() {
		final Item[] newData = new Item[items.length];
		System.arraycopy(items, 0, newData, 0, newData.length);
		return newData;
	}

	public int getFreeSlot() {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				return i;
			}
		}
		return -1;
	}

	public int getThisItemSlot(final Item item) {
		return getThisItemSlot(item.getId());
	}

	public int getThisItemSlot(final int itemId) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				if (items[i].getId() == itemId) {
					return i;
				}
			}
		}
		return -1;
	}

	public Item lookup(final int id) {
		for (final Item aData : items) {
			if (aData == null) {
				continue;
			}
			if (aData.getId() == id) {
				return aData;
			}
		}
		return null;
	}

	public int lookupSlot(final int id) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				continue;
			}
			if (items[i].getId() == id) {
				return i;
			}
		}
		return -1;
	}

	public void reset() {
		items = new Item[items.length];
	}

	public int remove(final int preferredSlot, final Item item) {
		int removed = 0, toRemove = item.getAmount();
		if (items[preferredSlot] != null) {
			if (items[preferredSlot].getId() == item.getId()) {
				int amt = items[preferredSlot].getAmount();
				if (amt > toRemove) {
					removed += toRemove;
					amt -= toRemove;
					toRemove = 0;
					// data[preferredSlot] = new
					// Item(data[preferredSlot].getDefinition().getId(), amt);
					set(preferredSlot, new Item(items[preferredSlot].getId(), amt, items[preferredSlot].getCharges()));
					return removed;
				} else {
					removed += amt;
					toRemove -= amt;
					// data[preferredSlot] = null;
					set(preferredSlot, null);
				}
			}
		}
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				if (items[i].getId() == item.getId()) {
					int amt = items[i].getAmount();
					if (amt > toRemove) {
						removed += toRemove;
						amt -= toRemove;
						toRemove = 0;
						// data[i] = new Item(data[i].getDefinition().getId(),
						// amt);
						set(i, new Item(items[i].getId(), amt, items[i].getCharges()));
						return removed;
					} else {
						removed += amt;
						toRemove -= amt;
						// data[i] = null;
						set(i, null);
					}
				}
			}
		}
		return removed;
	}

	public void addAll(final ItemContainer container) {
		for (int i = 0; i < container.getSize(); i++) {
			final Item item = container.get(i);
			if (item != null) {
				add(item);
			}
		}
	}

	public void addAll(final Item[] container) {
		for (int i = 0; i < container.length; i++) {
			final Item item = container[i];
			if (item != null) {
				add(item);
			}
		}
	}

	public void addAll(final List<Item> container) {
		for (int i = 0; i < container.size(); i++) {
			final Item item = container.get(i);
			if (item != null) {
				add(item);
			}
		}
	}

	public boolean hasSpaceFor(final ItemContainer container) {
		for (int i = 0; i < container.getSize(); i++) {
			final Item item = container.get(i);
			if (item != null) {
				if (!hasSpaceForItem(item)) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean hasSpaceForItem(final Item item) {
		if (alwaysStackable || item.getDefinitions().isStackable()) {
			for (final Item aData : items) {
				if (aData != null) {
					if (aData.getId() == item.getId()) {
						return true;
					}
				}
			}
		} else {
			if (item.getAmount() > 1) {
				return freeSlots() >= item.getAmount();
			}
		}
		final int index = freeSlot();
        return index != -1;
    }

    public Item[] toArray() {
        return items;
    }

    public ItemContainer getContainerCopy() {
        return this;
    }

    public boolean isAlwaysStackable() {
        return alwaysStackable;
    }

}
