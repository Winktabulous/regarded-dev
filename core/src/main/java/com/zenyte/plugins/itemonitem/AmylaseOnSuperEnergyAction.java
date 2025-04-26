package com.zenyte.plugins.itemonitem;

import com.zenyte.game.content.skills.herblore.actions.CombineStaminaPotion;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.plugins.dialogue.SkillDialogue;

/**
 * @author Tommeh | 11 jun. 2018 | 21:51:01
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class AmylaseOnSuperEnergyAction implements ItemOnItemAction {
	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		if (player.getSkills().getLevel(SkillConstants.HERBLORE) < 77) {
			player.sendMessage("You need a Herblore level of at least 77 to brew stamina potions.");
			return;
		}
		final Item amylase = from.getId() == ItemId.AMYLASE_CRYSTAL ? from : to;
		final Item potion = from.getId() != ItemId.AMYLASE_CRYSTAL ? from : to;
		final Integer dose = Integer.valueOf(potion.getName().substring(13, 14));
		if (amylase.getAmount() >= dose) {
			player.getDialogueManager().start(new StaminaPotCreationDialogue(player, new Item(potion.getId())));
		} else {
			player.sendMessage("You need at least " + dose + " amylase crystal" + (dose == 1 ? "" : "s") + " to do this.");
		}
	}

	@Override
	public ItemPair[] getMatchingPairs() {
		return new ItemPair[] {ItemPair.of(ItemId.SUPER_ENERGY4, ItemId.AMYLASE_CRYSTAL), ItemPair.of(ItemId.SUPER_ENERGY3, ItemId.AMYLASE_CRYSTAL), ItemPair.of(ItemId.SUPER_ENERGY2, ItemId.AMYLASE_CRYSTAL), ItemPair.of(ItemId.SUPER_ENERGY1, ItemId.AMYLASE_CRYSTAL)};
	}

	@Override
	public int[] getItems() {
		return null;
	}


	private static class StaminaPotCreationDialogue extends SkillDialogue {
		private final Item potionToUpgradeItem;

		public StaminaPotCreationDialogue(Player player, Item... items) {
			super(player, "How many would you like to make?", new Item(CombineStaminaPotion.POTS.get(items[0].getId())));
			potionToUpgradeItem = items[0];
		}

		@Override
		public void run(int slotId, int amount) {
			player.getActionManager().setAction(new CombineStaminaPotion(amount, potionToUpgradeItem.getId()));
		}
	}
}
