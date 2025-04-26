package com.zenyte.plugins.item;

import com.near_reality.game.model.item.degrading.Degradeable;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.degradableitems.DegradableItem;
import com.zenyte.game.model.item.degradableitems.DegradeType;
import com.zenyte.game.model.item.pluginextensions.ChargeExtension;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.ContainerWrapper;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;

/**
 * @author Kris | 25. aug 2018 : 23:12:34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class BraceletOfClay extends ItemPlugin implements ChargeExtension {

	@Override
	public void handle() {
		bind("Check", (player, item, slotId) -> player.getChargesManager().checkCharges(item));
	}

	@Override
	public int[] getItems() {
		return new int[] {11074};
	}

	@Override
	public void removeCharges(final Player player, final Item item, final ContainerWrapper wrapper, int slotId, final int amount) {
		int clayCharges = player.getNumericAttribute("bracelet of clay charges").intValue();
		//Set to default if the value is 0.
		if (clayCharges <= 0) {
			clayCharges = 28;
		}
		clayCharges--;
		player.addAttribute("bracelet of clay charges", Math.max(0, clayCharges));
		if (clayCharges <= 0) {
			player.sendMessage("Your bracelet of clay has degraded to dust.");
			player.getEquipment().set(EquipmentSlot.HANDS, null);
		}
	}

	@Override
	public void checkCharges(final Player player, final Item item) {
		final String name = item.getName();
		final int charges = player.getNumericAttribute("bracelet of clay charges").intValue();
		if (charges <= 0) {
			player.sendMessage("Your " + item.getName() + " has 28 charges.");
			return;
		}
		final Degradeable deg = DegradableItem.ITEMS.get(item.getId());
		if (deg == null) {
			return;
		}
		if (deg.getType() == DegradeType.RECOIL || deg.getType() == DegradeType.USE) {
			player.sendMessage("Your " + name + " has " + charges + " charge" + (charges == 1 ? "" : "s") + " remaining.");
			return;
		}
		final String percentage = FORMATTER.format(charges / (float) DegradableItem.getFullCharges(item.getId()) * 100);
		player.sendMessage("Your " + name + " " + (name.contains("legs") ? "have " : "has ") + percentage.replace(".0", "") + "% charges remaining.");
	}
}
