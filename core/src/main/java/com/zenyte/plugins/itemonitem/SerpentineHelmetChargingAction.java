package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 24 mei 2018 | 19:59:30
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class SerpentineHelmetChargingAction implements ItemOnItemAction {
	public static final int MAX_CHARGES = 90000;
	public static final double CHARGES_TO_SCALES_RATIO = MAX_CHARGES / 11000.0;
	public static final double SCALES_TO_CHARGES_RATIO = 11000.0 / MAX_CHARGES;

	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		if ((from.getId() >= 12929 && to.getId() >= 12929 || from.getId() >= 13196 && to.getId() >= 13196) && from.getId() != 12934 && to.getId() != 12934) {
			return;
		}
		final Item helmet = (from.getId() == 12929 || from.getId() == 12931 || from.getId() >= 13196 && from.getId() <= 13199) ? from : to;
		final int charges = helmet.getCharges();
		if (charges == MAX_CHARGES) {
			player.sendMessage("Your serpentine helmet is already fully charged.");
			return;
		}
		final int scales = player.getInventory().getAmountOf(12934);
		final int amount = (int) (scales * CHARGES_TO_SCALES_RATIO);
		int toCharge = amount;
		if (charges + amount > MAX_CHARGES) {
			toCharge = MAX_CHARGES - helmet.getCharges();
			final int freeScales = (int) Math.ceil(toCharge / CHARGES_TO_SCALES_RATIO);
			player.getInventory().deleteItem(12934, freeScales);
			player.sendMessage("You charge the serpentine helmet with " + freeScales + " charges.");
		} else {
			player.getInventory().deleteItem(12934, amount);
			player.sendMessage("You charge the serpentine helmet with " + amount + " charges.");
		}
		helmet.setCharges(helmet.getCharges() + toCharge);
		if (helmet.getId() == 12929 || helmet.getId() == 13196 || helmet.getId() == 13198) {
			helmet.setId(helmet.getId() >= 13196 ? helmet.getId() + 1 : 12931);
			player.getInventory().refresh(helmet == from ? fromSlot : toSlot);
		}
	}

	@Override
	public int[] getItems() {
		return new int[] {12934, 12929, 12931, 13196, 13197, 13198, 13199};
	}
}
