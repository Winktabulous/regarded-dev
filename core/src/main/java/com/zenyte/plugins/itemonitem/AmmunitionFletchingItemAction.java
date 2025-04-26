package com.zenyte.plugins.itemonitem;

import com.zenyte.game.content.skills.fletching.FletchingDefinitions;
import com.zenyte.game.content.skills.fletching.FletchingDefinitions.AmmunitionFletchingData;
import com.zenyte.game.content.skills.fletching.FletchingDefinitions.AmmunitionType;
import com.zenyte.game.content.skills.fletching.actions.AmmunitionFletching;
import com.zenyte.game.content.skills.fletching.actions.OgreAmmunitionFletchingD;
import com.zenyte.game.content.skills.fletching.composite.CompOgreBowItemOnItemAction;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.skills.AmmunitionFletchingD;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Kris | 11. nov 2017 : 0:34.49
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class AmmunitionFletchingItemAction implements ItemOnItemAction {
	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		final FletchingDefinitions.AmmunitionFletchingData ammunition = AmmunitionFletchingData.getDataByMaterial(from, to);
		if (ammunition != null) {
			if ((ammunition.equals(AmmunitionFletchingData.BROAD_ARROW) || ammunition.equals(AmmunitionFletchingData.BROAD_BOLT) || ammunition.equals(AmmunitionFletchingData.AMETHYST_BROAD_BOLT)) && !player.getSlayer().isUnlocked("Broader fletching")) {
				player.sendMessage("You have not unlocked the ability to make broad ammunition.");
				return;
			}
			if (ammunition == AmmunitionFletchingData.OGRE_ARROW_SHAFT || ammunition == AmmunitionFletchingData.WOLFBONE_TIPS) {
				if ((to.getId() == ItemId.KNIFE || to.getId() == ItemId.CHISEL) || (from.getId() == ItemId.KNIFE || from.getId() == ItemId.CHISEL)) {
					final Item material = from.getId() == ItemId.KNIFE || from.getId() == ItemId.CHISEL ? to : from;
					if (material.getId() == ItemId.KNIFE || material.getId() == ItemId.CHISEL) {
						return;
					}
					if (ammunition == AmmunitionFletchingData.OGRE_ARROW_SHAFT && player.getInventory().containsItem(ItemId.WOLF_BONES)) {
						player.getDialogueManager().start(new OgreAmmunitionFletchingD(player, ammunition, ammunition.getProduct(), CompOgreBowItemOnItemAction.unstrungBow));
					} else {
						player.getDialogueManager().start(new OgreAmmunitionFletchingD(player, ammunition, ammunition.getProduct()));
					}
				}
				return;
			}
			if (AmmunitionFletchingData.hasRequirements(player, ammunition)) {
				final int sets = ammunition.getType().getSets();
				if (from.getAmount() <= sets || to.getAmount() <= sets || ammunition.getType().isInstant()) {
					final int amount = Math.min(from.getAmount(), to.getAmount());
					player.getActionManager().setAction(new AmmunitionFletching(ammunition, amount, false));
				} else {
					player.getDialogueManager().start(new AmmunitionFletchingD(player, ammunition));
				}
				return;
			}
		} else {
			player.sendMessage("Nothing interesting happens");
		}
	}

	@Override
	public int[] getItems() {
		final IntArrayList list = new IntArrayList();
		for (final FletchingDefinitions.AmmunitionFletchingData data : AmmunitionFletchingData.VALUES) {
			for (final Item item : data.getMaterials()) {
				list.add(item.getId());
			}
		}
		list.add(ItemId.KNIFE);
		list.add(ItemId.CHISEL);
		return list.toArray(new int[list.size()]);
	}
}
