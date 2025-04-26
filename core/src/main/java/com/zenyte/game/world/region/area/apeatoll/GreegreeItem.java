package com.zenyte.game.world.region.area.apeatoll;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.plugins.equipment.equip.EquipPlugin;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Kris | 28. aug 2018 : 14:09:40
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class GreegreeItem extends ItemPlugin implements EquipPlugin {
	@Override
	public void handle() {
		bind("Hold", (player, item, slotId) -> player.getEquipment().wear(slotId));
	}

	@Override
	public boolean handle(final Player player, final Item item, final int slotId, final int equipmentSlot) {
		final Greegree greegree = Greegree.MAPPED_VALUES.get(item.getId());
		if (greegree == null) {
			return false;
		}
		if (!GlobalAreaManager.get("Ape Atoll").inside(player.getLocation()) && !GlobalAreaManager.get("Ape Atoll Dungeon").inside(player.getLocation())) {
			player.sendMessage("You attempt to use the Monkey Greegree but nothing happens.");
			return false;
		}
		if (player.isUnderCombat()) {
			player.sendMessage("You cannot transform while in combat.");
			return false;
		}
		return true;
	}

	@Override
	public int[] getItems() {
		final IntArrayList list = new IntArrayList(Greegree.VALUES.length);
		for (final Greegree greegree : Greegree.VALUES) {
			list.add(greegree.getItemId());
		}
		return list.toIntArray();
	}
}
