package com.zenyte.plugins.equipment.equip;

import com.zenyte.game.content.minigame.warriorsguild.catapultroom.CatapultRoom;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;

/**
 * Handles the warriors guild catapult shield.
 * @author Kris | 27. march 2018 : 4:22.18
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class DefensiveShieldEquip implements EquipPlugin {

	@Override
	public boolean handle(final Player player, final Item item, final int slotId, final int equipmentSlot) {
		if (player.getLocation().getPositionHash() != CatapultRoom.LOCATION.getPositionHash()) {
			player.sendMessage("You may not equip this shield outside the target area in the Warriors' Guild.");
			return false;
		}
		if (player.getInventory().getFreeSlots() < 3) {
			player.sendMessage("You need some more free inventory space to equip the shield.");
			return false;
		}
		CatapultRoom.wieldShield(player);
		return false;
	}

	@Override
	public int[] getItems() {
		return new int[] { 8856 };
	}

}
