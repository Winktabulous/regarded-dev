package com.zenyte.game.content.tombsofamascut.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import mgi.types.config.items.ItemDefinitions;

@SuppressWarnings("unused")
public class ArmadyleanPlateOnMasori implements PairedItemOnItemPlugin {

	private static final Animation ANIM = new Animation(3676);
	private static final Item HAMMER = new Item(ItemId.HAMMER);

	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		if (player.getSkills().getLevel(Skills.CRAFTING) < 90) {
			player.sendMessage("You need a Crafting level of 90 to do that.");
			return;
		}

		if (!player.getInventory().containsItem(HAMMER)) {
			player.sendMessage("You need a hammer to fortify your armor.");
			return;
		}

		final int index = getMatchingPairIndex(from, to, MasoriData.pairs);
		final MasoriData data = MasoriData.values[index];
		final int id = data.item.getId();
		final int plateAmount = data.plates.getAmount();
		player.getDialogueManager().start(new Dialogue(player) {
			@Override
			public void buildDialogue() {
				options("Fortify your " + ItemDefinitions.nameOf(id) + " with " + Utils.pluralized(ItemDefinitions.nameOf(ItemId.ARMADYLEAN_PLATE), plateAmount) + "?",
						new DialogueOption("Yes.", () -> {
							if (!player.getInventory().containsItem(data.item) || !player.getInventory().containsItem(data.plates)) {
								return;
							}

							player.lock();
							player.setAnimation(ANIM);
							player.delay(2, () -> {
								player.unlock();
								player.getInventory().deleteItem(data.item);
								player.getInventory().deleteItem(data.plates);
								player.getInventory().addItem(data.fortified);
								player.getSkills().addXp(Skills.CRAFTING, data.xp);
								player.getDialogueManager().start(new Dialogue(player) {
									@Override
									public void buildDialogue() {
										item(data.fortified,"You use " + Utils.pluralized(ItemDefinitions.nameOf(ItemId.ARMADYLEAN_PLATE), plateAmount) + " to fortify " + ItemDefinitions.nameOf(id) + ".");
									}
								});
							});
						}),
						new DialogueOption("No."));
			}
		});
	}

	@Override
	public ItemPair[] getMatchingPairs() {
		return MasoriData.pairs;
	}

	private enum MasoriData {
		MASK(ItemId.MASORI_MASK, ItemId.MASORI_MASK_F, 1, 830),
		BODY(ItemId.MASORI_BODY, ItemId.MASORI_BODY_F, 4, 3320),
		CHAPS(ItemId.MASORI_CHAPS, ItemId.MASORI_CHAPS_F, 3, 2490);

		public static final MasoriData[] values = values();
		public static final ItemPair[] pairs;
		private final Item item, fortified, plates;
		private final int xp;

		MasoriData(final int id, final int fortified, final int plates, final int xp) {
			this.item = new Item(id);
			this.fortified = new Item(fortified);
			this.plates = new Item(ItemId.ARMADYLEAN_PLATE, plates);
			this.xp = xp;
		}

		static {
			final int len = values.length;
			pairs = new ItemPair[len];
			for (int i = 0; i < len; i++) {
				pairs[i] = new ItemPair(ItemId.ARMADYLEAN_PLATE, values[i].item.getId());
 			}
		}

	}

}
