package com.zenyte.game.content.treasuretrails.clues;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import mgi.utilities.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.BitSet;

/**
 * @author Kris | 30. march 2018 : 3:51.56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class LightBox {
	/**
	 * Constructs the light box object per player on login.
	 *
	 * @param player the player who to consruct the box for.
	 */
	public LightBox(final Player player) {
		this.player = player;
	}

	/**
	 * The player who's managing this box.
	 */
	private final Player player;
	/**
	 * A bitset containing 25 bits; one bit for each lightbulb.
	 */
	private final BitSet bits = new BitSet(25);
	/**
	 * A map of <buttonIndex, LightButtonObject> containing information about
	 * each button. Initialized under the capacity of eight as there are only
	 * eight buttons.
	 */
	private final Int2ObjectMap<LightButton> lightButtons = new Int2ObjectOpenHashMap<>(8);
	/**
	 * Whether the light box has already been solved or not.
	 */
	private boolean complete;
	/**
	 * Counters to determine how many buttons are toggled, and how many aren't.
	 * Used to ensure that the puzzle has at least two buttons toggled/two
	 * buttons not toggled.
	 */
	private int unlitButtonCount;
	private int litButtonCount;

	/**
	 * Constructs the light box itself, associates all of the bits to each
	 * button and ensures that the puzzle itself becomes solvable. Ensures that
	 * all the bulbs have at least one button associated to them. Additionally,
	 * for the purpose of having a strategic way of solving the puzzle, each
	 * button has one light bulb associated to it that can only be toggled by
	 * this said button.
	 */
	private void construct() {
		final IntArrayList availableBits = new IntArrayList(25);
		/* Populating the available bits with values from 0 to 24(inclusive) */
		for (int i = 0; i < 25; i++) {
			availableBits.add(i);
		}
		/* A list of bits that can only be toggled by one specific button. */
		final IntOpenHashSet uniqueBits = new IntOpenHashSet(8);
		/*
         * A map of <bitIndex, amount> showing how many times each bit is used. Constructing the map with a capacity of 17, as eight of the bulbs are
         * unique to specific buttons.
         */
		final Int2IntOpenHashMap usedBits = new Int2IntOpenHashMap(17);
		/* Filling the list of unique bits with random values from the available bits, removing them from the available bits list. */
		for (int i = 0; i < 8; i++) {
			uniqueBits.add(availableBits.removeInt(Utils.random(availableBits.size() - 1)));
		}
		/*
         * Variables for defining the number of bulbs to attach to the button,
         * the random value generated and the amount of bits it's been associated to.
         */
		int amount;
		int value;
		int num;
		/*
         * Populating the light buttons map with a random - odd amount of bits
         * associated to it; each button will have one unique light button that
         * can only be switched on or off using it
         */
		for (int i = 0; i < 8; i++) {
			/* Generating a random number(between 3 and 10) of bits that will be associated to this button */
			amount = Utils.random(3, 10);
			int tryCount = 100;
			while (amount > 0) {
				if (--tryCount <= 0) {
					break;
				}
				/* Generating a random value from 0 to 24(inclusive) */
				value = Utils.random(24);
				/* If the specific value is a unique value and already  associated to a button, we continue and generate a new value. */
				if (uniqueBits.contains(value)) {
					continue;
				}
				/* Obtain the number of times the specific bit has already been used, or 0 if the bit hasn't been touched yet. */
				num = usedBits.getOrDefault(value, 0);
				/* Limiting each lightbulb to have a maximum of 4 buttons attached to it. */
				if (num >= 4 || !addBit(i, value)) {
					continue;
				}
				amount--;
				usedBits.put(value, num + 1);
			}
		}
		/* Loop over all of the bits one more time to ensure each light bulb is associated to at least one button. */
		for (int i = 0; i < 25; i++) {
			/* Get the number of times the specific bit has been associated to a button, or 0 if the bit is untouched. */
			num = usedBits.getOrDefault(i, 0);
			/* If the number of times this bit has been associated to a button is above 0, we skip the bit. */
			if (num > 0) {
				continue;
			}
			/* If the bit is listed as a unique bit, we skip it. */
			if (uniqueBits.contains(num)) {
				continue;
			}
			/* Add the bit to two random buttons, as each bulb needs to be associated to an odd amount of buttons, except for the uniques. */
			addBit(Utils.random(7), i);
		}
		/*
         * An iterator for the used bits map; we need to iterate over all of the keys to ensure that all of the values are in fact in even quantities;
         * if the value isn't an even number, we're going to associate the specific bit to a random button out of the eight.
         */
		associate(usedBits);
	}

	/**
	 * Associates the un-even buttons to random buttons on the interface.
	 *
	 * @param usedBits the used bits map.
	 */
	private void associate(@NotNull final Int2IntOpenHashMap usedBits) {
		final ObjectIterator<Int2IntMap.Entry> it = usedBits.int2IntEntrySet().fastIterator();
		/* The entry object from the map. */
		Int2IntMap.Entry entry;
		/* The key and value representative ints for the map. */
		int k;
		int v;
		int value;
		while (it.hasNext()) {
			entry = it.next();
			v = entry.getIntValue();
			/* If the value is an odd amount, we continue on to the next bit in the map. */
			if ((v & 1) == 1) {
				continue;
			}
			k = entry.getIntKey();
			/* Generate a random button index to associate with the bit. */
			value = Utils.random(7);
			addBit(value, k);
		}
	}

	/**
	 * Adds the requested bit to the requested button, if it doesn't already
	 * contain it.
	 *
	 * @param buttonIndex the index of the button to associate the bit with.
	 * @param bit         the index of the bit to associate with the button.
	 * @return false if the button already contains the bit,
	 * true if the bit was added to the button successfully
	 */
	private boolean addBit(final int buttonIndex, final int bit) {
		LightButton button = lightButtons.get(buttonIndex);
		if (button == null) {
			boolean lit = Utils.random(1) == 0;
			/* To ensure we have at least 2 buttons lit and unlit simultaneously, so the puzzle doesn't automatically solve itself. */
			if (!lit) {
				if (unlitButtonCount > 6) {
					lit = true;
				} else {
					unlitButtonCount++;
				}
			} else {
				if (litButtonCount > 6) {
					lit = false;
				} else {
					litButtonCount++;
				}
			}
			button = new LightButton(lit, new IntArrayList(4));
			lightButtons.put(buttonIndex, button);
		}
		/* If this specific bit has already been associated to the button, we return false meaning the action was cancelled. */
		if (button.associatedBits.contains(bit)) {
			return false;
		}
		/* If the button has been toggled, we flip all of the bits that are associated to it. */
		if (button.toggled) {
			bits.flip(bit);
		}
		return button.associatedBits.add(bit);
	}

	private void reset() {
		complete = false;
		bits.clear();
		lightButtons.clear();
		construct();
	}

	/**
	 * Opens the light box interface, makes the buttons clickable
	 *
	 * @param reset whether to reset the box statistics or not.
	 */
	public void open(final boolean reset) {
		if (lightButtons.isEmpty() || reset) {
			reset();
		}
		refresh();
		GameInterface.LIGHT_BOX.open(player);
	}

	/**
	 * Refreshes the toggled bits on the interface; if none of the bits are
	 * toggled(as they're all toggled by default at value 0), we set the puzzle
	 * state finished and inform the player, as well as lock the box.
	 */
	private void refresh() {
		final long[] longBit = bits.toLongArray();
		player.getVarManager().sendVar(1356, longBit.length == 0 ? 0 : (int) longBit[0]);
		if (longBit.length == 0) {
			final Item item = findLightboxItem();
			if (item == null) {
				return;
			}
			item.setCharges(2);
			player.sendMessage("As the last light turns on, you hear the latch release.");
			complete = true;
		}
	}

	/**
	 * Finds the light box item in the player's inventory that matches this light box.
	 *
	 * @return the light box item.
	 */
	private Item findLightboxItem() {
		final int lightBox = ItemId.LIGHT_BOX;
		final Int2ObjectSortedMap.FastSortedEntrySet<Item> entrySet = player.getInventory().getContainer().getItems().int2ObjectEntrySet();
		final Int2ObjectMap.Entry<Item> item = CollectionUtils.findMatching(entrySet, entry -> entry.getValue().getId() == lightBox);
		return item.getValue();
	}

	/**
	 * Handles pressing a button
	 *
	 * @param slot the button that was clicked.
	 */
	public void press(final int slot) {
		if (complete) {
			player.sendMessage("You've already completed the light box puzzle.");
			return;
		}
		final LightBox.LightButton button = lightButtons.get(slot);
		if (button == null) {
			return;
		}
		button.toggled = !button.toggled;
		for (int i = button.associatedBits.size() - 1; i >= 0; i--) {
			bits.flip(button.associatedBits.getInt(i));
		}
		refresh();
	}


	/**
	 * A class holding all the attributes of one button on the interface.
	 *
	 * @author Kris | 30. march 2018 : 21:35.54
	 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
	 */
	private static class LightButton {
		/**
         * Whether this button has been toggled or not.
         */
		private boolean toggled;
		/**
         * A list of bits that have been associated to this button.
         */
		private final IntList associatedBits;

		/**
         * Constructs a light button with the said state and the said list of
         * bits associated to it.
         *
         * @param toggled        whether the button should be enabled or not.
         * @param associatedBits a list of bits associated to this button.
         */
		public LightButton(final boolean toggled, final IntList associatedBits) {
			this.toggled = toggled;
			this.associatedBits = associatedBits;
		}
	}
}
