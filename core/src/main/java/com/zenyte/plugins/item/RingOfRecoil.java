package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ChargeExtension;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.ContainerWrapper;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;

/**
 * @author Kris | 25. aug 2018 : 22:41:01
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class RingOfRecoil extends ItemPlugin implements ChargeExtension {
	@Override
	public void handle() {
		bind("Break", (player, item, slotId) -> {
			player.getAttributes().put("RING_OF_RECOIL", 40);
			player.getInventory().deleteItem(slotId, item);
			player.sendMessage("You break the ring of recoil.");
		});
	}

	@Override
	public int[] getItems() {
		return new int[] {2550};
	}

	@Override
	public void removeCharges(final Player player, final Item item, final ContainerWrapper wrapper, int slotId, final int amount) {
		final int charges = player.getNumericAttribute("RING_OF_RECOIL").intValue();
		player.getAttributes().put("RING_OF_RECOIL", (charges - amount) <= 0 ? 40 : (charges - amount));
		if ((charges - amount) <= 0) {
			player.getEquipment().set(EquipmentSlot.RING.getSlot(), null);
			player.sendMessage(Colour.RS_PURPLE.wrap("Your Ring of Recoil has shattered."));
		}
	}

	@Override
	public void checkCharges(final Player player, final Item item) {
		final int charges = player.getAttributes().containsKey("RING_OF_RECOIL") ? player.getNumericAttribute("RING_OF_RECOIL").intValue() : 40;
		player.sendMessage("Your " + item.getName() + " has " + charges + " charge" + (charges == 1 ? "" : "s") + " remaining.");
	}
}
