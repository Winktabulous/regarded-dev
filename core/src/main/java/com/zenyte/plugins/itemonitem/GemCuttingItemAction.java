package com.zenyte.plugins.itemonitem;

import com.zenyte.game.content.skills.crafting.CraftingDefinitions;
import com.zenyte.game.content.skills.crafting.CraftingDefinitions.GemCuttingData;
import com.zenyte.game.content.skills.crafting.actions.GemCuttingCrafting;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.skills.GemCuttingD;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Kris | 11. nov 2017 : 0:28.07
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class GemCuttingItemAction implements ItemOnItemAction {
	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		final CraftingDefinitions.GemCuttingData gem = GemCuttingData.getDataByMaterial(from, to);
		if (gem != null && GemCuttingData.hasRequirements(player, gem)) {
			if (player.getInventory().getAmountOf(gem.getMaterial().getId()) == 1 && !gem.equals(GemCuttingData.AMETHYST)) {
				player.getActionManager().setAction(new GemCuttingCrafting(gem, 0, 1));
			} else {
				player.getDialogueManager().start(new GemCuttingD(player, gem));
			}
		} else {
			player.sendMessage("Nothing interesting happens");
		}
	}

	@Override
	public int[] getItems() {
		final IntArrayList list = new IntArrayList();
		for (final CraftingDefinitions.GemCuttingData data : GemCuttingData.VALUES_ARR) {
			list.add(data.getMaterial().getId());
		}
		list.add(CraftingDefinitions.CHISEL.getId());
		return list.toArray(new int[list.size()]);
	}
}
