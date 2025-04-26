package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ChargeExtension;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.ContainerWrapper;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;

/**
 * @author Kris | 27. aug 2018 : 15:16:30
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class RingOfForging extends ItemPlugin implements ChargeExtension {

	@Override
	public void handle() {
		bind("Check", (player, item, slotId) -> player.getChargesManager().checkCharges(item));
	}

	@Override
	public int[] getItems() {
		return new int[] {2568};
	}

	@Override
	public void removeCharges(final Player player, final Item item, final ContainerWrapper wrapper, int slotId, final int amount) {
		final int charges = player.getNumericAttribute("RING_OF_FORGING").intValue();
		player.getAttributes().put("RING_OF_FORGING", (charges - amount) <= 0 ? 140 : (charges - amount));
		if ((charges - amount) <= 0) {
			player.getEquipment().set(EquipmentSlot.RING.getSlot(), null);
			player.sendMessage("Your ring of forging has degraded to dust.");
		}
	}

	@Override
	public void checkCharges(final Player player, final Item item) {
		final int charges = player.getAttributes().containsKey("RING_OF_FORGING") ? player.getNumericAttribute("RING_OF_FORGING").intValue() : 40;
		player.sendMessage("Your " + item.getName() + " has " + charges + " charge" + (charges == 1 ? "" : "s") + " remaining.");
	}
}
