package com.zenyte.plugins.itemonitem;

import com.zenyte.game.content.boons.impl.ArcaneKnowledge;
import com.zenyte.game.content.skills.magic.Rune;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.RunePouch;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kris | 11. nov 2017 : 0:19.35
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
@SuppressWarnings("unused")
public final class RuneOnRunePouchItemAction implements PairedItemOnItemPlugin {
	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		final int toId = to.getId();
		final int fromId = from.getId();
		final int runeId;
		final int runeSlot;
		final RunePouch runePouch;
		if (toId == RunePouch.RUNE_POUCH.getId() || toId == RunePouch.TOURNAMENT_RUNE_POUCH.getId() || toId == RunePouch.DIVINE_RUNE_POUCH.getId()) {
			runeId = fromId;
			runeSlot = fromSlot;
			runePouch = RunePouch.chooseRunePouch(player, toId);
		} else {
			runeId = toId;
			runeSlot = toSlot;
			runePouch = RunePouch.chooseRunePouch(player, fromId);
		}

		final Item rune = new Item(runeId, player.getInventory().getAmountOf(runeId));
		int amount = rune.getAmount();
		final int inPouch = runePouch.getAmountOf(runeId);
		if ((amount + (long) inPouch) >= 16000) {
			amount = 16000 - inPouch;
		}
		rune.setAmount(amount);
		if (amount <= 0) {
			player.sendMessage("You can't put that many runes in your pouch.");
			return;
		}
		final Rune r = Rune.getRune(rune);
		if (r == null) {
			player.sendMessage("You can only add runes to the rune pouch.");
			return;
		}
		final int capacity = runePouch.runePouchCapacity();
		if(capacity == 5 && runePouch.getContainer().getSize() == 4 && runePouch.getAmountOf(runeId) == 0) {
			perkHandler(player, from, to, fromSlot, toSlot);
			player.getInventory().refreshAll();
			runePouch.getContainer().refresh(player);
			return;
		}
		if (runePouch.getContainer().getSize() == capacity) {
			if (runePouch.getAmountOf(runeId) == 0) {
				player.sendMessage("You can only carry " + (capacity == 3 ? "three" : "four") + " different types of runes in your rune pouch at a time.");
				return;
			}
		}
		runePouch.getContainer().deposit(player, player.getInventory().getContainer(), runeSlot, rune.getAmount());
		player.getInventory().refreshAll();
		runePouch.getContainer().refresh(player);
	}

	public void perkHandler(Player player, Item from, Item to, int fromSlot, int toSlot) {
		if (to.getId() != ItemId.DIVINE_RUNE_POUCH) {
			player.sendMessage("This can only be done on a Divine Rune Pouch.");
			return;
		}
		Rune fromRune = Rune.getRune(from);
		if (fromRune == null) {
			player.sendMessage("You can only use charged runes on this item.");
			return;
		}
		if (!player.getBoonManager().hasBoon(ArcaneKnowledge.class)) {
			player.sendMessage("You must have " + Colour.BLUE.wrap("Arcane Knowledge") + " unlocked to be do this.");
			return;
		}
		if (player.getRunePouch().getContainer().getSize() == 4 && (player.getRunePouch().bonusRuneTypeStored == null || player.getRunePouch().bonusRuneTypeStored == fromRune)) {
			if (player.getRunePouch().bonusRuneQuantityStored >= 16000) {
				player.sendMessage("You cannot add any more of this rune to your bonus slot.");
				return;
			}
			int amount = from.getAmount();
			int resultant;
			if (amount > 16000) {
				resultant = amount - 16000;
			} else {
				resultant = amount;
			}

			player.getRunePouch().bonusRuneTypeStored = fromRune;
			player.getRunePouch().bonusRuneQuantityStored = resultant;
			player.getInventory().deleteItem(from.getId(), resultant);
			return;
		} else if (player.getRunePouch().getContainer().getSize() == 4) {
			player.sendMessage("You already have a bonus rune of another type stored inside.");
			return;
		}
		player.sendDeveloperMessage("Uncaught condition in RunePouchItem");
	}

		@Override
	public ItemPair[] getMatchingPairs() {
		final int runePouchId = RunePouch.RUNE_POUCH.getId();
		final int secondaryRunePouchId = RunePouch.TOURNAMENT_RUNE_POUCH.getId();
		final int divineRunePouchId = RunePouch.DIVINE_RUNE_POUCH.getId();
		final int length = Rune.values.length;
		final List<ItemPair> pairs = new ArrayList<>(length * 3);
		for (int i = 0; i < length; i++) {
			final int runeId = Rune.values[i].getId();
			pairs.add(new ItemPair(runePouchId, runeId));
			pairs.add(new ItemPair(secondaryRunePouchId, runeId));
			pairs.add(new ItemPair(divineRunePouchId, runeId));
		}
		return pairs.toArray(new ItemPair[0]);
	}
}
