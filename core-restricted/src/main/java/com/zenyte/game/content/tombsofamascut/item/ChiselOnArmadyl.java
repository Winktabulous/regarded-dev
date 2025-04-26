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
public class ChiselOnArmadyl implements PairedItemOnItemPlugin {

	public static final Animation ANIM = new Animation(8833);
	private static final Item HAMMER = new Item(ItemId.HAMMER);

	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		if (player.getSkills().getLevel(Skills.CRAFTING) < 90) {
			player.sendMessage("You need a Crafting level of 90 to do that.");
			return;
		}

		if (!player.getInventory().containsItem(HAMMER)) {
			player.sendMessage("You need a hammer to break apart your armor.");
			return;
		}

		final int index = getMatchingPairIndex(from, to, ArmadyleanPlateData.pairs);
		final ArmadyleanPlateData data = ArmadyleanPlateData.values[index];
		final int id = data.item.getId();
		final int plateAmount = data.plates.getAmount();
		player.getDialogueManager().start(new Dialogue(player) {
			@Override
			public void buildDialogue() {
				options("Break apart your " + ItemDefinitions.nameOf(id) + " into " + Utils.pluralized(ItemDefinitions.nameOf(ItemId.ARMADYLEAN_PLATE), plateAmount) + "?",
						new DialogueOption("Yes.", () -> player.getDialogueManager().start(new Dialogue(player) {
							@Override
							public void buildDialogue() {
								options("Really break apart your " + ItemDefinitions.nameOf(id) + " into " + Utils.pluralized(ItemDefinitions.nameOf(ItemId.ARMADYLEAN_PLATE), plateAmount) + "?",
										new DialogueOption("No."), new DialogueOption("Yes.", () -> {
											if (!player.getInventory().checkSpace(plateAmount - 1)) {
												return;
											}

											if (!player.getInventory().containsItem(data.item)) {
												return;
											}

											player.lock();
											player.setAnimation(ANIM);
											player.sendMessage("You use your chisel to break apart the armor down into its base components.");
											player.delay(2, () -> {
												player.unlock();
												player.getInventory().deleteItem(data.item);
												player.getInventory().addItem(data.plates);
												player.getSkills().addXp(Skills.CRAFTING, data.xp);
											});
										}));
							}
						})), new DialogueOption("No."));
			}
		});
	}

	@Override
	public ItemPair[] getMatchingPairs() {
		return ArmadyleanPlateData.pairs;
	}

	private enum ArmadyleanPlateData {
		HELM(ItemId.ARMADYL_HELMET, 1, 210),
		CHESTPLATE(ItemId.ARMADYL_CHESTPLATE, 4, 840),
		CHAINSKIRT(ItemId.ARMADYL_CHAINSKIRT, 3, 630);

		public static final ArmadyleanPlateData[] values = values();
		public static final ItemPair[] pairs;
		private final Item item, plates;
		private final int xp;

		ArmadyleanPlateData(final int id, final int plates, final int xp) {
			this.item = new Item(id);
			this.plates = new Item(ItemId.ARMADYLEAN_PLATE, plates);
			this.xp = xp;
		}

		static {
			final int len = values.length;
			pairs = new ItemPair[len];
			for (int i = 0; i < len; i++) {
				pairs[i] = new ItemPair(ItemId.CHISEL, values[i].item.getId());
 			}
		}

	}

}
