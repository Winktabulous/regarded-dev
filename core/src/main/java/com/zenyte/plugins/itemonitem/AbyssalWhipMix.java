package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * @author Kris | 28. aug 2018 : 16:16:11
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class AbyssalWhipMix implements ItemOnItemAction {

	public enum WhipMix {
		VOLCANIC_MIX(12771, 12773, "lava"), FROZEN_MIX(12769, 12774, "ice");
		public static final WhipMix[] VALUES = values();
		public static final Int2ObjectOpenHashMap<WhipMix> MAPPED_VALUES = new Int2ObjectOpenHashMap<WhipMix>(VALUES.length);

		static {
			for (final AbyssalWhipMix.WhipMix value : VALUES) {
				MAPPED_VALUES.put(value.mixId, value);
				MAPPED_VALUES.put(value.recolouredWhipId, value);
			}
		}

		private final int mixId;
		private final int recolouredWhipId;
		private final String substanceName;

		WhipMix(int mixId, int recolouredWhipId, String substanceName) {
			this.mixId = mixId;
			this.recolouredWhipId = recolouredWhipId;
			this.substanceName = substanceName;
		}

		public int getRecolouredWhipId() {
			return recolouredWhipId;
		}
	}

	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		final Item whip = from.getId() == 4151 ? from : to;
		final Item dye = whip == from ? to : from;
		final AbyssalWhipMix.WhipMix mix = WhipMix.MAPPED_VALUES.get(dye.getId());
		if (mix == null) {
			return;
		}
		player.getDialogueManager().start(new Dialogue(player) {
			@Override
			public void buildDialogue() {
				item(new Item(mix.recolouredWhipId), Colour.RED + "WARNING!" + Colour.END + " changing the colour of your Abyssal Whip will render it untradeable. You will need to use a cleaning cloth to revert the changes. Are you sure?");
				options(TITLE, "Yes.", "No.").onOptionOne(() -> {
					final Inventory inventory = player.getInventory();
					inventory.deleteItem(fromSlot, from);
					inventory.deleteItem(toSlot, to);
					inventory.addItem(new Item(mix.recolouredWhipId));
					player.sendMessage("You coat your whip in " + mix.substanceName + ".");
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
		return new ItemPair[] {new ItemPair(4151, WhipMix.VOLCANIC_MIX.mixId), new ItemPair(4151, WhipMix.FROZEN_MIX.mixId)};
	}
}
