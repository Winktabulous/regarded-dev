package com.zenyte.game.world.entity.player.container;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.plugins.ContainerPlugin;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import mgi.types.config.InventoryDefinitions;
import mgi.types.config.items.ItemDefinitions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.zenyte.game.GameConstants.isOwner;

/**
 * @author Kris | 3. mai 2018 : 16:43:23
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class Container extends _Container<Item> {
	private static final Logger log = LoggerFactory.getLogger(Container.class);

	public Container(final ContainerPolicy policy, final ContainerType type, final Optional<Player> player) {
        this(type, policy, getSize(type), player);
	}

    public Container(final ContainerType type, final ContainerPolicy policy, final int size, final Optional<Player> player) {
        super(type, policy, new Int2ObjectLinkedOpenHashMap<>(size));
        this.size = size;
        items = new Int2ObjectLinkedOpenHashMap<>(size);
        availableSlots = new IntAVLTreeSet();
        modifiedSlots = new IntOpenHashSet(size);
        for (int i = 0; i < size; i++) {
            availableSlots.add(i);
        }
        player.ifPresent(value -> this.player = value);
    }

    public static int getSize(ContainerType containerType) {
        final InventoryDefinitions defs = InventoryDefinitions.get(containerType.getId());
        return defs == null ? 0 : defs.getSize();
    }

    /**
	 * Sets the variables of this container with the variables from the container in the parameters.
	 * 
	 * @param container
	 *            the container whose variables to copy.
	 */
	public void setContainer(final Container container) {
		items = new Int2ObjectLinkedOpenHashMap<>(size);
		availableSlots = new IntAVLTreeSet();
		modifiedSlots = new IntOpenHashSet(size);
        setType(container.type);
        size = getSize(type);
        setPolicy(container.policy);
        for (int i = 0; i < size; i++) {
			availableSlots.add(i);
		}
		for (final Entry<Item> entry : container.items.int2ObjectEntrySet()) {
			set(entry.getIntKey(), new Item(entry.getValue()));
		}
	}

	protected transient int size;
    protected transient IntAVLTreeSet availableSlots;
	protected transient boolean fullUpdate;
	protected transient IntOpenHashSet modifiedSlots;
	protected transient float weight;
	/**
	 * The owner of the container, can be null for shared containers.
	 */
	private transient Player player;

	public void linkPlayer(Player player) {
		this.player = player;
	}

	public void unlinkPlayer() {
		player = null;
	}

	/**
	 * Sets a specific slot in the container to the requested object.
	 * 
	 * @param slot
	 *            the slot to modify.
	 * @param item
	 *            the object to set it to, or null if removing.
	 */
	public void set(final int slot, final Item item) {
		final Item oldItem = item == null ? items.remove(slot) : items.put(slot, item);
		if (oldItem != null) {
			final float oldWeight = oldItem.getDefinitions().getWeight();
			if (oldWeight > 0) {
				weight -= oldWeight;
			}
		}
		if (player != null) {
			final RegionArea area = player.getArea();
			if (area instanceof ContainerPlugin) {
				final ContainerPlugin plugin = (ContainerPlugin) area;
				plugin.onContainerModification(player, this, oldItem, item);
			}
		}
		if (item == null) {
			modifiedSlots.add(slot);
			availableSlots.add(slot);
		} else {
			final ItemDefinitions itemDefinitions = item.getDefinitions();
			if (itemDefinitions != null) {
				final float itemWeight = itemDefinitions.getWeight();
				if (itemWeight > 0) {
					weight += itemWeight;
				}
			}
			modifiedSlots.add(slot);
			availableSlots.remove(slot);
		}
	}

	/**
	 * Calculates the total amount of the requested item across the entire container.
	 * 
	 * @param id
	 *            the id of the item to calculate the amount of.
	 * @return the total amount of this item(by id) in this container.
	 */
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


	public Map<Integer, Item> findAllById(int id) {
		var map = new LinkedHashMap<Integer, Item>();
		for(int i = 0; i < getContainerSize(); i++) {
			Item item = items.get(i);
			if (item == null) continue;

			if (item.getId() == id) {
				map.put(i, item);
			}
		}

		return map;
	}


	/**
	 * Finds the first slot which contains an item of the same id that's requested.
	 * 
	 * @param id
	 *            the id of the item that's requested.
	 * @return the slot of the first occurrence of the item, or null if there's none.
	 */
	public int getSlotOf(final int id) {
		for (int i = 0; i < getContainerSize(); i++) {
			final Item item = get(i);
			if (item == null || item.getId() != id) {
				continue;
			}
			return i;
		}
		return -1;
	}

	public int findSlotFor(final int id) {
		return getSlotOf(id);
	}

	public int getSlot(final Item a) {
		for (int i = 0; i < getContainerSize(); i++) {
			final Item item = get(i);
			if (item != a) {
				continue;
			}
			return i;
		}
		return -1;
	}

	/**
	 * Finds all the slots that contain an item of the id that's requested.
	 * 
	 * @param id
	 *            the item id that's being requested.
	 * @return an Integer array containing all the slots which contain the specific if, or null if there are none.
	 */
	public int[] getSlotsOf(final int id) {
		IntArrayList list = null;
		for (final Entry<Item> entry : items.int2ObjectEntrySet()) {
			final Item val = entry.getValue();
			if (val.getId() == id) {
				if (list == null) {
					list = new IntArrayList();
				}
				list.add(entry.getIntKey());
			}
		}
		if (list == null) {
			return null;
		}
		final int[] array = list.toArray(new int[0]);
		Arrays.sort(array);
		return array;
	}

	public Item getAny(final int... items) {
		for (int itemId : items) {
			final Int2ObjectMap.Entry<Item> item = getFirst(itemId);
			if (item != null) {
				return item.getValue();
			}
		}
		return null;
	}

	/**
	 * Gets an item at the requested slot. If there is no item available in the given slot, returns null.
	 * 
	 * @param slot
	 *            the slot which to check.
	 * @return the Item in that slot, or null if there is none.
	 */
	public Item get(final int slot) {
		return items.get(slot);
	}

	/**
	 * Gets the first entry for the requested item. If there is no item which matches the given argument, returns null.
	 *
	 * @param itemId the item id to find.
	 * @return the Entry (slot, Item) for that item, or null if there is none.
	 */
	public Entry<Item> getFirst(final int itemId) {
		for (final Entry<Item> entry : items.int2ObjectEntrySet()) {
			if (entry.getValue().getId() == itemId) {
				return entry;
			}
		}
		return null;
	}

	/**
	 * Gets the first entry for the requested item. If there is no item which matches the given argument, returns null.
	 *
	 * @param item the item to find.
	 * @return the Entry (slot, Item) for that item, or null if there is none.
	 */
	public Entry<Item> getFirst(final Item item) {
		for (final Entry<Item> entry : items.int2ObjectEntrySet()) {
			if (entry.getValue() == item) {
				return entry;
			}
		}
		return null;
	}


	/**
	 * Returns a list of missing items from a list of requirements
	 * @param required - the required resources
	 * @return - a list of items we are missing with amounts
	 */
	public List<Item> getMissingItems(Item... required) {
		var itemsMissing = new ArrayList<Item>();

		for (var item : required) {
			if (contains(item)) continue;
			var amountWithin = getAmountOf(item.getId());
			var amountNeeded = item.getAmount() - amountWithin;
			itemsMissing.add(new Item(item.getId(), amountNeeded));
		}

		return itemsMissing;
	}

	/**
	 * Clears the item container of all the items, repopulates the available slots set with all the missing slots.
	 */
	public void clear() {
		items.clear();
		weight = 0;
		for (int i = 0; i < size; i++) {
			availableSlots.add(i);
			modifiedSlots.add(i);
		}
	}

	/**
	 * Checks whether this item container contains the requested item. If the amount within the container is less than the parameters,
	 * returns false.
	 * 
	 * @param id
	 *            the id of the item that's being checked for.
	 * @param amount
	 *            the minimum amount of the given item for the method to return as true.
	 * @return true if the amount of the item in the container is at least equal to the amount in the parameter.
	 */
	public boolean contains(final int id, final int amount) {
		long currentAmount = 0;
		for (final Entry<Item> entry : items.int2ObjectEntrySet()) {
			final Item value = entry.getValue();
			if (value.getId() == id) {
				currentAmount += value.getAmount();
				if (currentAmount >= amount) {
					return true;
				}
			}
		}
		return currentAmount >= amount;
	}

	/**
	 * Checks whether this item container contains the requested item. If the amount within the container is less than the parameters,
	 * returns false.
	 * 
	 * @param item
	 *            the item to check
	 * @return true if the amount of the item in the container is at least equal to the amount of the item in the parameter.
	 */
	public boolean contains(final Item item) {
		long currentAmount = 0;
		for (final Entry<Item> entry : items.int2ObjectEntrySet()) {
			final Item value = entry.getValue();
			if (value.getId() == item.getId()) {
				currentAmount += value.getAmount();
				if (currentAmount >= item.getAmount()) {
					return true;
				}
			}
		}
		return currentAmount >= item.getAmount();
	}

	/**
	 * Shifts all the items in the {@link Container#items} map. The items will then appear from slot 0-size, in the same order that they
	 * existed in the map previously. Reallocates the available slots set.
	 */
	public void shift() {
		final int itemsSize = items.size();
		final Int2ObjectOpenHashMap<Item> shiftedMap = new Int2ObjectOpenHashMap<Item>(itemsSize);
		int index = 0;
		final int length = getContainerSize();
		for (int i = 0; i < length; i++) {
			final Item item = get(i);
			if (item == null) {
				continue;
			}
			shiftedMap.put(index++, item);
			if (index >= itemsSize) {
				break;
			}
		}
		items.clear();
		availableSlots.clear();
		items.putAll(shiftedMap);
		for (int i = shiftedMap.size(); i < size; i++) {
			availableSlots.add(i);
			modifiedSlots.add(i);
		}
	}

	/**
	 * Gets the maximum size of the container
	 * 
	 * @return the size of the container.
	 */
	public int getContainerSize() {
		return size;
	}

	public void setContainerSize(final int size) {
		this.size = size;
	}

	/**
	 * @return whether or not the container is empty.
	 */
	public boolean isEmpty() {
		return getSize() == 0;
	}

	/**
	 * Gets the amount of used slots currently in this container.
	 * 
	 * @return amount of used slots.
	 */
	public int getSize() {
		return items.size();
	}

	/**
	 * Gets the amount of available slots in this container.
	 * 
	 * @return amount of available slots.
	 */
	public int getFreeSlotsSize() {
		return availableSlots.size();
	}

	public void addAll(Collection<Item> items) {
		for (Item item : items) {
			add(item);
		}
	}

	/**
	 * Attempts to enqueue the requested item into the container, based on the policy of the container. Doesn't affect the item in the
	 * parameter, constructs a new item based on that. If the item or its definitions are null, payload is returned as a failure. If the
	 * item's attributes aren't null, the item is automatically rendered as unstackable.
	 * 
	 * @param requestedItem
	 *            the item that's being requested to be added to the container.
	 * @return a payload of the transaction, containing information including the amount that was successfully transmitted, the state of the
	 *         transaction, the payload of the transaction and more.
	 */
	public ContainerResult add(final Item requestedItem) {
		final Item item = requestedItem == null ? null : new Item(requestedItem.getId(), requestedItem.getAmount(), requestedItem.getAttributesCopy());
		final ContainerResult result = new ContainerResult(item, ContainerState.ADD);
		final ItemDefinitions definitions = item == null ? null : item.getDefinitions();
		if (definitions == null) {
			result.setResult(RequestResult.FAILURE);
			return result;
		}
		final int id = item.getId();
		if(!eligibleForItem(requestedItem)) {
			player.sendMessage("The item slips through your hands once more, you are not worthy.");
			result.setSucceededAmount(requestedItem.getAmount());
			requestedItem.setAmount(0);
			result.setResult(RequestResult.SUCCESS);
			return result;
		}
		final int requestedAmount = item.getAmount();
		if (requestedAmount <= 0) {
			result.setResult(RequestResult.SUCCESS);
			return result;
		}
		final boolean stackable = definitions.isStackable();
		if (availableSlots.isEmpty()) {
			if (stackable && policy == ContainerPolicy.NORMAL || policy == ContainerPolicy.ALWAYS_STACK) {
				final int slot = getSlotOf(id);
				if (slot != -1) {
					final Item existingItem = get(slot);
					if (!existingItem.hasAttributes()) {
						final int amount = existingItem.getAmount();
						if (amount + item.getAmount() < 0) {
							set(slot, new Item(id, Integer.MAX_VALUE, item.getAttributesCopy()));
							item.setAmount(item.getAmount() - (Integer.MAX_VALUE - amount));
						} else {
							set(slot, new Item(id, item.getAmount() + amount, item.getAttributesCopy()));
							item.setAmount(0);
						}
						result.setSucceededAmount(requestedAmount - item.getAmount());
						result.setResult(result.getSucceededAmount() != requestedAmount ? RequestResult.OVERFLOW : RequestResult.SUCCESS);
						return result;
					}
				}
			}
			result.setResult(RequestResult.NOT_ENOUGH_SPACE);
			return result;
		}
		int slot;
		if (item.hasAttributes() || policy == ContainerPolicy.NEVER_STACK || (slot = getSlotOf(id)) == -1 && stackable && policy == ContainerPolicy.NORMAL || slot == -1) {
			if (policy != ContainerPolicy.ALWAYS_STACK && !stackable || policy == ContainerPolicy.NEVER_STACK) {
				final int amt = availableSlots.size();
				for (int i = item.getAmount() - 1; i >= 0; i--) {
					if (availableSlots.isEmpty()) {
						break;
					}
					set(availableSlots.firstInt(), new Item(item.getId(), 1, item.getAttributesCopy()));
				}
				result.setSucceededAmount(Math.min(amt, requestedAmount));
				result.setResult(amt >= requestedAmount ? RequestResult.SUCCESS : RequestResult.NOT_ENOUGH_SPACE);
				return result;
			}
			set(availableSlots.firstInt(), item);
			result.setSucceededAmount(requestedAmount);
			result.setResult(RequestResult.SUCCESS);
			return result;
		}
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
			for (int i = item.getAmount() - 1; i >= 0; i--) {
				if (availableSlots.isEmpty()) {
					result.setSucceededAmount(requestedAmount - item.getAmount());
					result.setResult(RequestResult.NOT_ENOUGH_SPACE);
					return result;
				}
				final int availableSlot = availableSlots.firstInt();
				set(availableSlot, new Item(id, 1, item.getAttributesCopy()));
				item.setAmount(item.getAmount() - 1);
			}
		}
		result.setSucceededAmount(requestedAmount - item.getAmount());
		result.setResult(result.getSucceededAmount() != requestedAmount ? RequestResult.OVERFLOW : RequestResult.SUCCESS);
		return result;
	}

	private boolean eligibleForItem(Item requestedItem) {
		if(player == null)
			return true;
		if(requestedItem.getId() != 33243 && requestedItem.getId() != 33241 && requestedItem.getId() != 33242)
			return true;
		if(player.getPrivilege().eligibleTo(PlayerPrivilege.MODERATOR) && requestedItem.getId() == 33241)
			return true;
		if(player.getPrivilege().eligibleTo(PlayerPrivilege.ADMINISTRATOR) && requestedItem.getId() == 33242)
			return true;
		if(player.getPrivilege().eligibleTo(PlayerPrivilege.TRUE_DEVELOPER) && requestedItem.getId() == 33243)
			return true;
		return false;
	}

	public int getMaximumTransferrableAmount(final Item item) {
		final ItemDefinitions definitions = item == null ? null : item.getDefinitions();
		if (definitions == null) {
			return 0;
		}
		final int id = item.getId();
		final int requestedAmount = item.getAmount();
		if (requestedAmount <= 0) {
			return 0;
		}
		final boolean stackable = definitions.isStackable();
		if (availableSlots.isEmpty()) {
			if (stackable && policy == ContainerPolicy.NORMAL || policy == ContainerPolicy.ALWAYS_STACK) {
				final int slot = getSlotOf(id);
				if (slot != -1) {
					final Item existingItem = get(slot);
					if (!existingItem.hasAttributes()) {
						final int amount = existingItem.getAmount();
						if (amount + item.getAmount() < 0) {
							return Integer.MAX_VALUE - amount;
						}
						return item.getAmount();
					}
				}
			}
			return 0;
		}
		int slot;
		if (item.hasAttributes() || policy == ContainerPolicy.NEVER_STACK || (slot = getSlotOf(id)) == -1 && stackable && policy == ContainerPolicy.NORMAL || slot == -1) {
			if (policy != ContainerPolicy.ALWAYS_STACK && !stackable || policy == ContainerPolicy.NEVER_STACK) {
				return Math.min(availableSlots.size(), item.getAmount());
			}
			return item.getAmount();
		}
		if (stackable || policy == ContainerPolicy.ALWAYS_STACK) {
			final Item existingItem = get(slot);
			final int amount = existingItem.getAmount();
			if (amount + item.getAmount() < 0) {
				return Integer.MAX_VALUE - amount;
			}
			return item.getAmount();
		}
		return Math.min(availableSlots.size(), item.getAmount());
	}

	/**
	 * Tries to enqueue an array of items to this container.
	 * 
	 * @param items
	 *            the items to enqueue.
	 * @return the payload containing information about every single item that was attempted to enqueue.
	 */
	public MultiResult add(final Item... items) {
		final ArrayList<ContainerResult> results = new ArrayList<ContainerResult>(items.length);
		for (int i = 0; i < items.length; i++) {
			final Item item = items[i];
			results.add(this.add(item));
		}
		final MultiResult multiresult = new MultiResult(ContainerState.ADD, results.toArray(new ContainerResult[0]));
		return multiresult;
	}

	/**
	 * Attempts to remove the requested item from this container. Constructs a new item object based on the requested item, therefore the
	 * requested item object will not be affected.
	 * 
	 * @param requestedItem
	 *            the item that's being requested to remove.
	 * @return a payload of the transaction, containing information including the amount that was successfully transmitted, the state of the
	 *         transaction, the payload of the transaction and more.
	 */
	public ContainerResult remove(final Item requestedItem) {
		final Item item = requestedItem == null ? null : new Item(requestedItem.getId(), requestedItem.getAmount(), requestedItem.getAttributesCopy());
		final ContainerResult result = new ContainerResult(item, ContainerState.REMOVE);
		final ItemDefinitions definitions = item == null ? null : item.getDefinitions();
		if (definitions == null) {
			result.setResult(RequestResult.FAILURE);
			return result;
		}
		final int id = item.getId();
		final int requestedAmount = item.getAmount();
		if(!eligibleForItem(requestedItem)) {
			player.sendMessage("The item slips through your hands once more, you are not worthy.");
			result.setSucceededAmount(requestedItem.getAmount());
			requestedItem.setAmount(0);
			result.setResult(RequestResult.SUCCESS);
			return result;
		}
		final int[] slots = getSlotsOf(id);
		if (slots == null) {
			result.setResult(RequestResult.NOT_ENOUGH_ITEMS);
			return result;
		}
		for (final int slot : slots) {
			final Item currentItem = get(slot);
			final int amount = currentItem.getAmount();
			set(slot, amount > item.getAmount() ? new Item(id, amount - item.getAmount(), item.getAttributesCopy()) : null);
			item.setAmount(Math.max(0, item.getAmount() - amount));
			if (item.getAmount() <= 0) {
				break;
			}
		}
		result.setSucceededAmount(requestedAmount - item.getAmount());
		result.setResult(result.getSucceededAmount() != requestedAmount ? RequestResult.NOT_ENOUGH_ITEMS : RequestResult.SUCCESS);
		return result;
	}

	/**
	 * Tries to remove an array of items from this container.
	 * 
	 * @param items
	 *            the items to remove.
	 * @return the payload containing information about every single item that was attempted to move.
	 */
	public MultiResult remove(final Item... items) {
		final ArrayList<ContainerResult> results = new ArrayList<ContainerResult>(items.length);
		for (int i = 0; i < items.length; i++) {
			final Item item = items[i];
			results.add(this.remove(item));
		}
		final MultiResult multiresult = new MultiResult(ContainerState.REMOVE, results.toArray(new ContainerResult[0]));
		return multiresult;
	}

	/**
	 * Removes the item from the slot requested. If the slot is null, the payload is returned as failure.
	 * 
	 * @param slot
	 *            the slot in the container that needs to be removed.
	 * @param amount
	 *            the amount to remove from that given slot. If the amount is less than there is of that item in the slot, only the
	 *            requested amount is removed. If the amount is below or equal to the amount of that item, the payload is marked as
	 *            successful. Otherwise, the payload will be marked as {@link RequestResult#NOT_ENOUGH_ITEMS}.
	 * @return a payload containing information about the transaction, whether it was a failure (if slot or item is null), or success
	 *         otherwise.
	 */
	public ContainerResult remove(final int slot, int amount) {
		final Item requestedItem = slot == -1 ? null : get(slot);
		final Item item = requestedItem == null ? null : new Item(requestedItem.getId(), requestedItem.getAmount(), requestedItem.getAttributesCopy());
		if (getPolicy() == ContainerPolicy.NEVER_STACK) {
			final int existingAmount = getAmountOf(item.getId());
			if (amount > existingAmount) {
				amount = existingAmount;
			}
			final ContainerResult result = this.remove(new Item(requestedItem.getId(), amount, requestedItem.getAttributesCopy()));
			return result;
		}
		final ContainerResult result = new ContainerResult(item, ContainerState.REMOVE);
		final ItemDefinitions definitions = item == null ? null : item.getDefinitions();
		if (definitions == null) {
			result.setResult(RequestResult.FAILURE);
			return result;
		}
		final int originalAmount = item.getAmount();
		if (amount < item.getAmount()) {
			set(slot, new Item(item.getId(), item.getAmount() - amount, item.getAttributesCopy()));
			item.setAmount(item.getAmount() - amount);
		} else {
			set(slot, null);
			item.setAmount(0);
		}
		result.setSucceededAmount(originalAmount - item.getAmount());
		result.setResult(result.getSucceededAmount() == amount ? RequestResult.SUCCESS : RequestResult.NOT_ENOUGH_ITEMS);
		return result;
	}

	/**
	 * Flags this specific container as in-need for an update. The system will automatically decide between full or partial update. The
	 * information about the container can be retrieved from {@link Container#type}.
	 * 
	 * @param player
	 *            the player for whom the container is being updated.
	 */
	public void refresh(final Player player) {
		if (player == null || player.isNulled()) {
			return;
		}
		player.getPendingContainers().add(this);
		if (this.type == ContainerType.INVENTORY || this.type == ContainerType.EQUIPMENT) {
			recalculateWeight();
		}
	}

	private void recalculateWeight() {
		weight = 0;
		for (final Int2ObjectMap.Entry<Item> entry : this.items.int2ObjectEntrySet()) {
			final Item item = entry.getValue();
			if (item == null) {
				continue;
			}
			final ItemDefinitions definitions = item.getDefinitions();
			if (definitions == null) {
				continue;
			}
			final float weight = definitions.getWeight();
			if (weight == 0 || type == ContainerType.INVENTORY && weight < 0) {
				continue;
			}
			this.weight += weight;
		}
	}

	/**
	 * Flags a list of slots to be forced updated.
	 * 
	 * @param slots
	 *            the slots that need to be force updated.
	 */
	public void refresh(final int... slots) {
		if (slots != null && slots.length > 0) {
			for (final int i : slots) {
				modifiedSlots.add(i);
			}
		}
	}

	/**
	 * Deposits an item from the container in the parameters over to this container. Maximum amount will be the item in parameters, however
	 * it may be limited as containers have different policies and sizes.
	 * 
	 * @param player
	 *            the player who is depositing the item to this container - variable can be null; if it isn't null an something goes wrong,
	 *            e.g. there's not enough space, the player is notified about it.
	 * @param container
	 *            the container from which to transmit the item over.
	 * @param slotId
	 *            the slot from which the item is being carried over.
	 * @param amount
	 *            the amount that's being requested.
	 */
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
		if (!definitions.isStackable() && container.getPolicy() != ContainerPolicy.ALWAYS_STACK) {
			int i = amount == 1 ? slotId : 0;
			final int length = amount == 1 ? (slotId + 1) : container.getContainerSize();
			for (; i < length; i++) {
				final Item invItem = container.get(i);
				if (invItem == null) {
					continue;
				}
				if (invItem.getId() != item.getId()) {
					continue;
				}
				final ContainerResult containerResult = add(new Item(item.getId(), 1, invItem.getAttributesCopy()));
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
			final ContainerResult containerResult = add(new Item(item.getId(), amount, item.getAttributesCopy()));
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
	 * Withdraws an item from this container over to the container in parameters.. Maximum amount will be the item in parameters, however it
	 * may be limited as containers have different policies and sizes.
	 * 
	 * @param player
	 *            the player who is withdrawing the item to this container - variable can be null; if it isn't null an something goes wrong,
	 *            e.g. there's not enough space, the player is notified about it.
	 * @param container
	 *            the container to which to transmit the item over.
	 * @param slot
	 *            the slot from which the item is being carried over.
	 * @param amount
	 *            the amount that's being requested.
	 */
	public void withdraw(final Player player, final Container container, int slot, int amount) {
		final Item item = get(slot);
		if (item == null || amount <= 0) {
			return;
		}
		final ItemDefinitions definitions = item.getDefinitions();
		if (definitions == null) {
			return;
		}
		if (item.hasAttributes()) {
			slot = getSlotOf(item.getId());
		}
		if (!item.hasAttributes() && (definitions.isStackable() || policy == ContainerPolicy.ALWAYS_STACK)) {
			final int existingAmount = container.getAmountOf(item.getId());
			boolean notify = false;
			final int realAmount = getAmountOf(item.getId());
			if ((Math.min(amount, realAmount)) + existingAmount < 0) {
				amount = Integer.MAX_VALUE - existingAmount;
				notify = true;
			}
			if (!definitions.isStackable() || existingAmount == 0) {
				final int freeSlots = container.getFreeSlotsSize();
				if (!definitions.isStackable()) {
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
			if (amount == 0 || notify) {
				if (player != null) {
					player.sendMessage("Not enough space in your " + container.getType().getName() + ".");
				}
				if (amount == 0) return;
			}
			final ContainerResult containerResult = definitions.isStackable() ? remove(slot, amount) : remove(new Item(item.getId(), amount, item.getAttributesCopy()));
			final RequestResult result = containerResult.getResult();
			if (result == RequestResult.FAILURE || containerResult.getSucceededAmount() <= 0) {
				return;
			}
			container.add(new Item(item.getId(), containerResult.getSucceededAmount(), item.getAttributesCopy()));
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
				final ContainerResult containerResult = remove(i--, 1);
				final RequestResult result = containerResult.getResult();
				if (result == RequestResult.FAILURE || containerResult.getSucceededAmount() <= 0) {
					break;
				}
				amount -= containerResult.getSucceededAmount();
				container.add(new Item(item.getId(), containerResult.getSucceededAmount(), containerItem.getAttributesCopy()));
			}
		}
		refresh();
	}

	public static final Set<Container> AWAITING_RESET_CONTAINERS = new HashSet<>();

	public static final void resetContainer() {
		if (AWAITING_RESET_CONTAINERS.isEmpty()) {
			return;
		}
		for (final Container c : AWAITING_RESET_CONTAINERS) {
			try {
				c.setFullUpdate(false);
				c.modifiedSlots.clear();
			} catch (Exception e) {
				log.error("", e);
			}
		}
		AWAITING_RESET_CONTAINERS.clear();
	}

    public List<Item> getItemsAsList() {
		var newList = new ArrayList<>(items.values());
		return newList;
	}

	public IntAVLTreeSet getAvailableSlots() {
		return availableSlots;
	}

	public boolean isFullUpdate() {
		return fullUpdate;
	}

	public void setFullUpdate(boolean fullUpdate) {
		this.fullUpdate = fullUpdate;
	}

	public IntOpenHashSet getModifiedSlots() {
		return modifiedSlots;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public Player getPlayer() {
		return player;
	}
}
