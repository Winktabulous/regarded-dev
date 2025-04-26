package com.zenyte.game.world.entity.player.container.impl.bank;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.*;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import mgi.types.config.items.ItemDefinitions;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static com.zenyte.game.world.entity.player.container.impl.bank.Bank.unbankableItems;

/**
 * @author Kris | 20. mai 2018 : 16:42:02
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class BankContainer extends Container {
	public BankContainer(final Bank bank, final ContainerPolicy policy, final ContainerType type) {
		super(policy, type, Optional.empty());
		this.bank = bank;
	}

	transient Bank bank;

	/**
	 * Flags this specific container as in-need for an update. The system will automatically decide between full or partial update. The
	 * information about the container can be retrieved from {@link Container#type}.
	 * 
	 * @param player
	 *            the player for whom the container is being updated.
	 */
	@Override
	public void refresh(final Player player) {
		if (player == null) {
			return;
		}
		player.getPendingContainers().add(this);
		bank.refreshTabSizes();
	}

	private static Item getItemCopyOrNull(Item item) {
		return item == null ? null : new Item(item.getId(), item.getAmount(), item.getAttributesCopy());
	}

	/**
	 * Attempts to enqueue the requested item into the container, based on the policy of the container. Doesn't affect the item in the
	 * parameter, constructs a new item based on that. If the item or its definitions are null, payload is returned as a failure. If the
	 * item's attributes aren't null, the item is automatically rendered as unstackable.
	 * 
	 * @param itemToAdd
	 *            the item that's being requested to be added to the container.
	 * @return a payload of the transaction, containing information including the amount that was successfully transmitted, the state of
	 *         the transaction, the payload of the transaction and more.
	 */
	@Override
	public ContainerResult add(final Item itemToAdd) {
		final Item item = getItemCopyOrNull(itemToAdd);
		final ContainerResult result = new ContainerResult(item, ContainerState.ADD);

		if (isInvalidItem(result, item)) return result;

		final int id = item.getId();
		if (isUnbankableItem(result, id)) return result;

		final int requestedAmount = item.getAmount();
		if (requestedAmount <= 0) {
			result.setResult(RequestResult.SUCCESS);
			return result;
		}

		final ItemDefinitions definitions = item.getDefinitions();
		final boolean stackable = definitions.isStackable();
		final int placeholderId = ItemDefinitions.getOrThrow(PlaceholderRedirections.builder.getOrDefault(definitions.getId(), definitions.getId())).getPlaceholderId();

		if (availableSlots.isEmpty())
			if (handleEmptySlots(result, item, id, stackable, placeholderId, requestedAmount)) return result;

		Integer placeholderSlot = placeholderId == -1 ? null : getSlotOf(placeholderId);
		final boolean shouldNotStack = item.hasAttributes() && placeholderSlot == null || policy == ContainerPolicy.NEVER_STACK;

		if (shouldNotStack || isSlotOccupied(id, placeholderSlot, stackable)) {
			handleNonStackableItems(result, item, requestedAmount);
			return result;
		}

		finalizeAddingItem(result, item, id, stackable, requestedAmount, placeholderSlot);
		return result;
	}


	private boolean isInvalidItem(ContainerResult result, Item item) {
		final ItemDefinitions definitions = item == null ? null : item.getDefinitions();
		if (definitions == null) {
			result.markFailure();
			return true;
		}
		return false;
	}

	private boolean isUnbankableItem(ContainerResult result, int id) {
		if (unbankableItems.contains(id)) {
			getPlayer().sendMessage("A magical force prevents you from banking this item!");
			result.markFailure();
			return true;
		}
		return false;
	}

	private boolean handleEmptySlots(ContainerResult result, Item item, int id, boolean stackable, int placeholderId, int requestedAmount) {
		if (stackable && policy == ContainerPolicy.NORMAL || policy == ContainerPolicy.ALWAYS_STACK) {
			int slot = getSlotOf(id);
			if (slot != -1 || placeholderId != -1 && (slot = getSlotOf(placeholderId)) != -1) {
				final Item existingItem = get(slot);
				if (!existingItem.hasAttributes()) {
					final int amount = existingItem.getAmount();
					if (amount + item.getAmount() < 0) {
						set(slot, new Item(id, Integer.MAX_VALUE, item.getAttributesCopy()));
						item.setAmount(item.getAmount() - (Integer.MAX_VALUE - amount));
					}
					else {
						set(slot, new Item(id, item.getAmount() + amount, item.getAttributesCopy()));
						item.setAmount(0);
					}
					result.setSucceededAmount(requestedAmount - item.getAmount());
					result.setResult(result.getSucceededAmount() != requestedAmount ? RequestResult.OVERFLOW : RequestResult.SUCCESS);
					return true;
				}
			}
		}
		result.setResult(RequestResult.NOT_ENOUGH_SPACE);
		return true;
	}

	private boolean isSlotOccupied(int id, Integer placeholderSlot, boolean stackable) {
		int slot;
		return (slot = getSlotOf(id)) == -1 && placeholderSlot == null && stackable && policy == ContainerPolicy.NORMAL
			|| slot == -1 && placeholderSlot == null;
	}

	private void handleNonStackableItems(ContainerResult result, Item item, int requestedAmount) {
		final int availableSlotsCount = availableSlots.size();
        IntStream.iterate(item.getAmount() - 1, i -> i >= 0 && !availableSlots.isEmpty(), i -> i - 1)
			.forEach(i -> set(availableSlots.firstInt(), new Item(item.getId(), 1, item.getAttributesCopy())));

		result.setSucceededAmount(Math.min(availableSlotsCount, requestedAmount));
		result.setResult(availableSlotsCount >= requestedAmount ? RequestResult.SUCCESS : RequestResult.NOT_ENOUGH_SPACE);
	}

	private void finalizeAddingItem(ContainerResult result, Item item, int id, boolean stackable, int requestedAmount, Integer placeholderSlot) {
		int slot;
		if ((slot = getSlotOf(id)) == -1) slot = placeholderSlot;

		if (stackable || policy == ContainerPolicy.ALWAYS_STACK) {
			final Item existingItem = get(slot);
			final int amount = existingItem == null ? 0 : existingItem.getAmount();
			if (amount + item.getAmount() < 0) {
				set(slot, new Item(id, Integer.MAX_VALUE, item.getAttributesCopy()));
				item.setAmount(item.getAmount() - (Integer.MAX_VALUE - amount));
			} else {
				set(slot, new Item(id, item.getAmount() + amount, item.getAttributesCopy()));
				item.setAmount(0);
			}
		} else {
			for (int i = item.getAmount() - 1; i >= 0 && !availableSlots.isEmpty(); i--) {
				final int availableSlot = availableSlots.firstInt();
				set(availableSlot, new Item(id, 1, item.getAttributesCopy()));
				item.setAmount(item.getAmount() - 1);
			}
		}
		result.setSucceededAmount(requestedAmount - item.getAmount());
		result.setResult(result.getSucceededAmount() != requestedAmount ? RequestResult.OVERFLOW : RequestResult.SUCCESS);
	}

	/**
	 * Attempts to remove the requested item from this container. Constructs a new item object based on the requested item, therefore
	 * the requested item object will not be affected.
	 * 
	 * @param requestedItem
	 *            the item that's being requested to remove.
	 * @param player
	 * @return a payload of the transaction, containing information including the amount that was successfully transmitted, the state of
	 *         the transaction, the payload of the transaction and more.
	 */
	public ContainerResult remove(final Item requestedItem, final boolean placeholder, Player player) {
		final Item item = requestedItem == null ? null : new Item(requestedItem.getId(), requestedItem.getAmount(), requestedItem.getAttributesCopy());
		final ContainerResult result = new ContainerResult(item, ContainerState.REMOVE);
		final ItemDefinitions definitions = item == null ? null : item.getDefinitions();
		if (definitions == null) {
			result.setResult(RequestResult.FAILURE);
			return result;
		}
		final int id = item.getId();
		final int requestedAmount = item.getAmount();
		final int[] slots = getSlotsOf(id);
		if (slots == null) {
			result.setResult(RequestResult.NOT_ENOUGH_ITEMS);
			return result;
		}
		if (definitions.isPlaceholder()) {
			if (slots.length > 0) {
				final int slot = slots[0];
				set(slot, null);
			}
			result.setResult(RequestResult.SUCCESS);
			return result;
		}
		int predictedPlaceholderSlot = -1;
		final int phId = ItemDefinitions.getOrThrow(PlaceholderRedirections.builder.getOrDefault(definitions.getId(), definitions.getId())).getPlaceholderId();
		final int itemId = placeholder && phId != -1 ? phId : id;
		final int[] phIdSlots = getSlotsOf(phId);
		if (phIdSlots != null && phIdSlots.length > 0) {
			predictedPlaceholderSlot = phIdSlots[0];
		}
		for (final int slot : slots) {
			final Item currentItem = get(slot);
			final int amount = currentItem.getAmount();
			if (predictedPlaceholderSlot == -1) {
				predictedPlaceholderSlot = slot;
			}
			set(slot, amount > item.getAmount() ? new Item(id, amount - item.getAmount(), item.getAttributesCopy()) : null);
			item.setAmount(Math.max(0, item.getAmount() - amount));
			if (item.getAmount() <= 0) {
				break;
			}
		}
		if (placeholder && id != itemId && predictedPlaceholderSlot != -1 && getAmountOf(id) == 0) {
			set(predictedPlaceholderSlot, new Item(itemId, 0));
		} else {
			if (placeholder && id == itemId && getAmountOf(id) == 0) {
				player.sendMessage("You can't set a placeholder for that item.");
			}
		}
		result.setSucceededAmount(requestedAmount - item.getAmount());
		result.setResult(result.getSucceededAmount() != requestedAmount ? RequestResult.NOT_ENOUGH_ITEMS : RequestResult.SUCCESS);
		return result;
	}

	public ContainerResult removeSpecific(final int slot, final Item requestedItem, final boolean placeholder, Player player) {
		final Item item = requestedItem == null ? null : new Item(requestedItem.getId(), requestedItem.getAmount(), requestedItem.getAttributesCopy());
		final ContainerResult result = new ContainerResult(item, ContainerState.REMOVE);
		final ItemDefinitions definitions = item == null ? null : item.getDefinitions();
		if (definitions == null) {
			result.setResult(RequestResult.FAILURE);
			return result;
		}
		final int id = item.getId();
		final int requestedAmount = item.getAmount();
		if (definitions.isPlaceholder()) {
			set(slot, null);
			result.setResult(RequestResult.SUCCESS);
			return result;
		}
		int predictedPlaceholderSlot = -1;
		final int phId = ItemDefinitions.getOrThrow(PlaceholderRedirections.builder.getOrDefault(definitions.getId(), definitions.getId())).getPlaceholderId();
		final int itemId = placeholder && phId != -1 ? phId : id;
		final int[] phIdSlots = getSlotsOf(phId);
		if (phIdSlots != null && phIdSlots.length > 0) {
			predictedPlaceholderSlot = phIdSlots[0];
		}
		final Item currentItem = get(slot);
		final int amount = currentItem.getAmount();
		if (predictedPlaceholderSlot == -1) {
			predictedPlaceholderSlot = slot;
		}
		set(slot, amount > item.getAmount() ? new Item(id, amount - item.getAmount(), item.getAttributesCopy()) : null);
		item.setAmount(Math.max(0, item.getAmount() - amount));
		if (placeholder && id != itemId && predictedPlaceholderSlot != -1 && getAmountOf(id) == 0) {
			set(predictedPlaceholderSlot, new Item(itemId, 0));
		} else {
			if (placeholder && id == itemId && getAmountOf(id) == 0) {
				player.sendMessage("You can't set a placeholder for that item.");
			}
		}
		result.setSucceededAmount(requestedAmount - item.getAmount());
		result.setResult(result.getSucceededAmount() != requestedAmount ? RequestResult.NOT_ENOUGH_ITEMS : RequestResult.SUCCESS);
		return result;
	}

	/**
	 * Clears the item container of all the items, repopulates the available
	 * slots set with all the missing slots.
	 */
	@Override
	public void clear() {
		super.clear();
		bank.tabSizes = new int[10];
	}

	/**
	 * Calculates the total amount of the requested item across the entire
	 * container.
	 * 
	 * @param id
	 *            the id of the item to calculate the amount of.
	 * @return the total amount of this item(by id) in this container.
	 */
	@Override
	public int getAmountOf(final int id) {
		int amount = 0;
		for (final Entry<Item> entry : items.int2ObjectEntrySet()) {
			final Item value = entry.getValue();
			if (value.getId() == id) {
				amount += value.getAmount();
			}
		}
		return amount;
	}

	/**
	 * Deposits an item from the container in the parameters over to this container. Maximum amount will be the item in parameters,
	 * however it may be limited as containers have different policies and sizes.
	 * 
	 * @param player
	 *            the player who is depositing the item to this container - variable can be null; if it isn't null an something goes
	 *            wrong, e.g. there's not enough space, the player is notified about it.
	 * @param container
	 *            the container from which to transmit the item over.
	 * @param slotId
	 *            the slot from which the item is being carried over.
	 * @param amount
	 *            the amount that's being requested.
	 */
	@Override
	public void deposit(final Player player, final Container container, final int slotId, int amount) {
		final Item item = container.get(slotId);
		if (item == null || amount <= 0) {
			return;
		}
		final ItemDefinitions definitions = item.getDefinitions();
		if (definitions == null) {
			return;
		}
		final long inInventory = container.getAmountOf(item.getId());
		if (amount > inInventory) {
			amount = (int) inInventory;
		}
		//If an unstackable item is set to a quantity of > 1 then they can dupe the item indefinitely!
		if (!definitions.isStackable() && container.getPolicy() != ContainerPolicy.ALWAYS_STACK) {
			int i = 0;
			final int length = amount == 1 ? (slotId + 1) : container.getContainerSize();
			for (; i < length; i++) {
				final Item invItem = container.get(i);
				if (invItem == null) {
					continue;
				}
				if (invItem.getId() != item.getId()) {
					continue;
				}
				final ContainerResult containerResult = bank.add(new Item(item.getId(), 1, invItem.getAttributesCopy()));
				final RequestResult result = containerResult.getResult();
				if (result == RequestResult.FAILURE) {
					break;
				}
				amount -= containerResult.getSucceededAmount();
				final int remainder = item.getAmount() - containerResult.getSucceededAmount();
				container.set(i, remainder > 0 ? new Item(item.getId(), remainder, invItem.getAttributesCopy()) : null);
				container.refresh(i);
				if (result == RequestResult.OVERFLOW || result == RequestResult.NOT_ENOUGH_SPACE) {
					if (player != null) {
						player.sendMessage("Not enough space in your " + getType().getName() + ".");
					}
					break;
				}
				if (amount <= 0) {
					break;
				}
			}
		} else {
			final ContainerResult containerResult = bank.add(new Item(item.getId(), amount, item.getAttributesCopy()));
			final RequestResult result = containerResult.getResult();
			if (result == RequestResult.FAILURE) {
				return;
			}
			final int remainder = item.getAmount() - containerResult.getSucceededAmount();
			container.set(slotId, remainder > 0 ? new Item(item.getId(), remainder, item.getAttributesCopy()) : null);
			container.refresh(slotId);
			if (result == RequestResult.OVERFLOW || result == RequestResult.NOT_ENOUGH_SPACE) {
				if (player != null) {
					player.sendMessage("Not enough space in your " + getType().getName() + ".");
				}
			}
		}
		refresh();
	}

	/**
	 * Withdraws an item from this container over to the container in parameters.. Maximum amount will be the item in parameters,
	 * however it may be limited as containers have different policies and sizes.
	 * 
	 * @param player
	 *            the player who is withdrawing the item to this container - variable can be null; if it isn't null an something goes
	 *            wrong, e.g. there's not enough space, the player is notified about it.
	 * @param container
	 *            the container to which to transmit the item over.
	 * @param slot
	 *            the slot from which the item is being carried over.
	 * @param amount
	 *            the amount that's being requested.
	 */
	public void withdraw(final Player player, final Container container, int slot, int amount, final boolean placeholder) {
		final Item item = get(slot);
		if (item == null || (amount <= 0 && !placeholder)) {
			return;
		}
		final ItemDefinitions definitions = item.getDefinitions();
		if (definitions == null) {
			return;
		}
		final boolean stackable = definitions.isStackable() || (!item.hasAttributes() && bank.getSetting(BankSetting.WITHDRAW_MODE) == 1 && definitions.getNotedId() != -1 && ItemDefinitions.get(definitions.getNotedId()).isStackable());
		if (item.hasAttributes()) {
			slot = getSlotOf(item.getId());
		}
		if (!item.hasAttributes() && (stackable || policy == ContainerPolicy.ALWAYS_STACK)) {
			final int existingAmount = (bank.getSetting(BankSetting.WITHDRAW_MODE) == 1 && definitions.getNotedId() != -1) ? container.getAmountOf(definitions.getNotedId()) : container.getAmountOf(item.getId());
			boolean notify = false;
			if (existingAmount + amount < 0) {
				amount = Integer.MAX_VALUE - existingAmount;
				notify = true;
			}
			if (!stackable || existingAmount == 0) {
				final int freeSlots = container.getFreeSlotsSize();
				if (!stackable) {
					if (freeSlots < amount) {
						notify = true;
						amount = freeSlots;
					}
				} else {
					if (freeSlots == 0) {
						amount = 0;
					}
				}
			}
			if (!placeholder && (amount == 0 || notify)) {
				if (player != null) {
					player.sendMessage("Not enough space in your " + container.getType().getName() + ".");
				}
				if (amount == 0) {
					return;
				}
			}
			if (!item.hasAttributes() && bank.getSetting(BankSetting.WITHDRAW_MODE) == 1 && definitions.getNotedId() == -1) {
				if (player != null) {
					player.sendMessage("You can't withdraw this item as a note.");
				}
			}
			final int id = !item.hasAttributes() && bank.getSetting(BankSetting.WITHDRAW_MODE) == 1 && !definitions.isNoted() && definitions.getNotedId() != -1 ? definitions.getNotedId() : item.getId();
			final ContainerResult containerResult = bank.remove(new Item(item.getId(), amount, item.getAttributesCopy()), placeholder);
			final RequestResult result = containerResult.getResult();
			if (result == RequestResult.FAILURE || containerResult.getSucceededAmount() <= 0) {
				return;
			}
			container.add(new Item(id, containerResult.getSucceededAmount(), item.getAttributesCopy()));
		} else {
			final int availableSpace = container.getFreeSlotsSize();
			if (amount > availableSpace) {
				amount = availableSpace;
			}
			if (amount == 0) {
				if (player != null) {
					player.sendMessage("Not enough space in your " + container.getType().getName() + ".");
				}
				return;
			}
			int i = amount == 1 ? slot : 0;
			final int length = amount == 1 ? (slot + 1) : getContainerSize();
			for (; i < length; i++) {
				if (amount <= 0) {
					break;
				}
				final Item containerItem = get(i);
				if (containerItem == null) {
					continue;
				}
				if (containerItem.getId() != item.getId()) {
					continue;
				}
				final ContainerResult containerResult = bank.remove(i--, 1, placeholder);
				final RequestResult result = containerResult.getResult();
				if (result == RequestResult.FAILURE || containerResult.getSucceededAmount() <= 0) {
					break;
				}
				amount -= containerResult.getSucceededAmount();
				if (bank.getSetting(BankSetting.WITHDRAW_MODE) == 1 && definitions.getNotedId() == -1) {
					if (player != null) {
						player.sendMessage("You can't withdraw this item as a note.");
					}
				}
				final int id = bank.getSetting(BankSetting.WITHDRAW_MODE) == 1 && definitions.isNoted() && definitions.getNotedId() != -1 ? definitions.getNotedId() : item.getId();
				container.add(new Item(id, containerResult.getSucceededAmount(), containerItem.getAttributesCopy()));
			}
		}
		refresh();
	}

}
