package com.zenyte.game.content.tombsofamascut.item;

import com.zenyte.game.content.tombsofamascut.TOAConstants;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.degradableitems.DegradableItem;
import com.zenyte.game.model.item.pluginextensions.ChargeExtension;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.ContainerWrapper;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

@SuppressWarnings("unused")
public class TumekensShadowPlugin extends ItemPlugin implements ChargeExtension {

	private static final Item TUMEKENS_SHADOW_ITEM = new Item(ItemId.TUMEKENS_SHADOW);
	private static final Item SOUL_RUNE = new Item(ItemId.SOUL_RUNE);
	private static final Item CHAOS_RUNE = new Item(ItemId.CHAOS_RUNE);

	@Override
	public void handle() {
		bind("Uncharge", (player, item, container, slotId) -> {
			int spaceNeeded = 2;
			if (player.getInventory().containsItem(SOUL_RUNE)) {
				spaceNeeded--;
			}
			if (player.getInventory().containsItem(CHAOS_RUNE)) {
				spaceNeeded--;
			}
			if (player.getInventory().getFreeSlots() < spaceNeeded) {
				player.sendMessage("Your inventory is too full to hold the runes.");
				return;
			}
			player.getDialogueManager().start(new Dialogue(player) {
				@Override
				public void buildDialogue() {
					options("Uncharge all the chargse from your staff?", new DialogueOption("Proceed.", () -> {
						final int charges = item.getCharges();
						if (charges > 0) {
							final int souls = charges * TOAConstants.SOUL_RUNES_MOD;
							final int chaos = charges * TOAConstants.CHAOS_RUNES_MOD;
							player.getInventory().addOrDrop(new Item(ItemId.SOUL_RUNE, souls));
							player.getInventory().addOrDrop(new Item(ItemId.CHAOS_RUNE, chaos));
							item.setCharges(0);
							player.getDialogueManager().item(TUMEKENS_SHADOW_ITEM, "You uncharge your Tumeken's shadow, regaining " + Utils.pluralizedFormatted("soul rune", souls) + " and " + Utils.pluralizedFormatted("chaos rune", chaos) + " in the process.");
						}

						final String name = item.getName();
						if (item.getId() == ItemId.TUMEKENS_SHADOW) {
							item.setId(ItemId.TUMEKENS_SHADOW_UNCHARGED);
						}

						container.refresh(slotId);
					}), new DialogueOption("Cancel."));
				}
			});
		});
		bind("Charge", (player, item, container, slotId) -> charge(player, item, slotId));
	}

	public static void charge(final Player player, final Item item, final int slotId) {
		final int charges = item.getCharges();
		final int maxItemCharges = DegradableItem.TUMEKENS_SHADOW.getMaximumCharges();
		if (charges >= maxItemCharges) {
			player.sendMessage("Your " + TUMEKENS_SHADOW_ITEM.getName() + " is already fully charged.");
			return;
		}

		final int chargesFromSoul = player.getInventory().getAmountOf(ItemId.SOUL_RUNE) / TOAConstants.SOUL_RUNES_MOD;
		if (chargesFromSoul < 1) {
			player.sendMessage("You don't appear to have enough soul runes to charge the Tumeken's shadow.");
			return;
		}

		final int chargesFromChaos = player.getInventory().getAmountOf(ItemId.CHAOS_RUNE) / TOAConstants.CHAOS_RUNES_MOD;
		if (chargesFromChaos < 1) {
			player.sendMessage("You don't appear to have enough chaos runes to charge the Tumeken's shadow.");
			return;
		}

		final int chargesFromItems = Math.min(chargesFromSoul, chargesFromChaos);
		final int maxCharges = Math.min(maxItemCharges - charges, chargesFromItems);
		player.sendInputInt("How many charges do you want to apply? (Up to " + Utils.format(maxCharges) + ")", val -> {
			final int chargesToAdd = Math.min(maxCharges, val);
			if (chargesToAdd < 1) {
				return;
			}

			Item soulRunes = new Item(ItemId.SOUL_RUNE, chargesToAdd * TOAConstants.SOUL_RUNES_MOD);
			Item chaosRunes = new Item(ItemId.CHAOS_RUNE, chargesToAdd * TOAConstants.CHAOS_RUNES_MOD);
			if (!player.getInventory().containsItem(soulRunes) || !player.getInventory().containsItem(chaosRunes)) {
				return;
			}

			final int totalCharges = charges + chargesToAdd;
			player.getInventory().deleteItems(soulRunes, chaosRunes);
			player.getInventory().deleteItems(chaosRunes);
			player.getDialogueManager().start(new Dialogue(player) {
				@Override
				public void buildDialogue() {
					final String chargesString = Utils.pluralizedFormatted("charge", chargesToAdd);
					if (charges == 0) {
						item(TUMEKENS_SHADOW_ITEM, "You apply " + chargesString + " to your " + TUMEKENS_SHADOW_ITEM.getName() + ".");
					} else {
						final String totalChargesString = Utils.pluralizedFormatted("charge", totalCharges);
						item(TUMEKENS_SHADOW_ITEM, "You apply an additional " + chargesString + " to your " + TUMEKENS_SHADOW_ITEM.getName() + ". It now has " + totalChargesString + " in total.");
					}
				}
			});
			if (item.getId() == ItemId.TUMEKENS_SHADOW_UNCHARGED) {
				item.setId(ItemId.TUMEKENS_SHADOW);
			}
			item.setCharges(totalCharges);
			player.getInventory().refresh(slotId);
		});
	}

	@Override
	public int[] getItems() {
		return new int[]{ItemId.TUMEKENS_SHADOW, ItemId.TUMEKENS_SHADOW_UNCHARGED};
	}

	@Override
	public void removeCharges(Player player, Item item, ContainerWrapper wrapper, int slotId, int amount) {
		final int currentCharges = item.getCharges();
		item.setCharges(currentCharges - 1);
		if (item.getCharges() <= 0) {
			final String weaponName = item.getName();
			item.setId(ItemId.TUMEKENS_SHADOW_UNCHARGED);
			player.getEquipment().refresh(EquipmentSlot.WEAPON.getSlot());
			player.sendMessage("<col=ef1020>Your " + weaponName + " has ran out of charges.</col>");
		}
	}

	@Override
	public void checkCharges(Player player, Item item) {
		player.sendMessage(item.getName() + " has " + Utils.pluralizedFormatted("charge", item.getCharges()) + " remaining.");
	}

}
