package com.zenyte.plugins.itemonitem;

import com.zenyte.game.content.MaxCape;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 19 aug. 2018 | 20:25:01
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
@SuppressWarnings("unused")
public class ItemOnMaxCapeAction implements ItemOnItemAction {
	private static final Item MAX_HOOD = new Item(13281);

	@Override
	public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
		final Item cape = from.getId() == 13342 ? from : to;
		final MaxCape upgraded = MaxCape.get(from.getId() != 13342 ? from.getId() : to.getId());
		if (upgraded == null) {
			return;
		}
		final Item upgrade = new Item(upgraded.getUpgrade());
		final Item upgradedCape = new Item(upgraded.getCape());
		final Item upgradedHood = new Item(upgraded.getHood());
		if (!player.getInventory().containsItem(MAX_HOOD)) {
			player.sendMessage("You need your max cape hood to be able to combine those.");
			return;
		}
		player.getDialogueManager().start(new Dialogue(player) {
			@Override
			public void buildDialogue() {
				options("You will lose your " + upgrade.getName() + " in the process.", "Yes - combine this item with your max cape and lose it.", "No! I don't want to lose my item.").onOptionOne(() -> {
					setKey(5);
					player.getInventory().deleteItem(cape);
					player.getInventory().deleteItem(upgrade);
					player.getInventory().deleteItem(MAX_HOOD);
					player.getInventory().addItem(upgradedCape);
					player.getInventory().addItem(upgradedHood);
				});
				item(5, upgradedCape, "You combine your " + upgrade.getName() + " and max cape as one.");
			}
		});
	}

	@Override
	public ItemPair[] getMatchingPairs() {
		final ItemOnItemAction.ItemPair[] pairs = new ItemPair[MaxCape.values.length];
		for (int i = 0; i < pairs.length; i++) {
			final MaxCape cape = MaxCape.values[i];
			pairs[i] = new ItemPair(13342, cape.getUpgrade());
		}
		return pairs;
	}

	@Override
	public int[] getItems() {
		return null;
	}
}
