package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.itemonitem.AbyssalWhipMix.WhipMix;
import com.zenyte.plugins.itemonitem.DarkBowPaint.BowPaint;

import java.util.ArrayList;

/**
 * @author Kris | 28. aug 2018 : 17:10:48
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class CleaningCloth implements ItemOnItemAction {
	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		final Item whip = from.getId() == 3188 ? from : to;
		final Item dye = whip == from ? to : from;
		final AbyssalWhipMix.WhipMix mix = WhipMix.MAPPED_VALUES.get(dye.getId());
		final DarkBowPaint.BowPaint paint = BowPaint.MAPPED_VALUES.get(dye.getId());
		if (mix == null && paint == null) {
			return;
		}
		final int produce = mix == null ? 11235 : 4151;
		player.getDialogueManager().start(new Dialogue(player) {
			@Override
			public void buildDialogue() {
				item(new Item(produce), Colour.RED + "WARNING!" + Colour.END + " You will not be able to retrieve the coating from the " + dye.getName().toLowerCase() + ". Are you sure?");
				options(TITLE, "Yes.", "No.").onOptionOne(() -> {
					final Inventory inventory = player.getInventory();
					if (inventory.getItem(fromSlot) == from && inventory.getItem(toSlot) == to) {
						inventory.deleteItem(from);
						inventory.deleteItem(to);
						inventory.addItem(new Item(produce));
						player.sendMessage("You wipe the coating off of the " + dye.getName().toLowerCase() + ".");
					}
				});
			}
		});
	}

	@Override
	public int[] getItems() {
		return null;
	}

	@Override
	public ItemPair[] getMatchingPairs() {
		final ArrayList<ItemOnItemAction.ItemPair> list = new ArrayList<ItemPair>(WhipMix.VALUES.length + BowPaint.VALUES.length);
		for (final AbyssalWhipMix.WhipMix mix : WhipMix.VALUES) {
			list.add(new ItemPair(3188, mix.getRecolouredWhipId()));
		}
		for (final DarkBowPaint.BowPaint paint : BowPaint.VALUES) {
			list.add(new ItemPair(3188, paint.getRecolouredBowId()));
		}
		return list.toArray(new ItemPair[list.size()]);
	}
}
