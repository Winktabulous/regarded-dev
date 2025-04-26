package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 30. dets 2017 : 19:51.51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class BlowpipeItemOnItemAction implements ItemOnItemAction {

	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		if (!from.getName().contains("blowpipe") && !to.getName().contains("blowpipe")) {
			return;
		}
		final Item blowpipe = from.getName().contains("blowpipe") ? from : to;
		final Item other = blowpipe == from ? to : from;
		int darts = blowpipe.getNumericAttribute("blowpipeDarts").intValue();
		int scales = blowpipe.getNumericAttribute("blowpipeScales").intValue();
		int type = blowpipe.getNumericAttribute("blowpipeDartType").intValue();
		if (other.getId() == 12934) {
			if (scales == 16383) {
				player.sendMessage("Your blowpipe is already fully charged with scales.");
				return;
			}
			int amount = other.getAmount();
			if (amount + scales > 16383 || amount + scales < 0) {
				amount = 16383 - scales;
			}
			scales += amount;
			if (blowpipe.getId() == 12924) {
				blowpipe.setId(12926);
				player.getInventory().refresh(fromSlot, toSlot);
			}
			player.getInventory().deleteItem(new Item(other.getId(), amount));
			blowpipe.setAttribute("blowpipeScales", scales);
            player.getChargesManager().checkCharges(blowpipe);
			return;
		}
		if (other.getId() != type && darts > 0) {
			player.sendMessage("Your cannot load the blowpipe with different type ammunition simultaneously.");
			return;
		}
		if (darts == 16383) {
			player.sendMessage("Your blowpipe is already fully loaded with darts.");
			return;
		}
		int amount = other.getAmount();
		if (amount + darts > 16383 || amount + darts < 0) {
			amount = 16383 - darts;
		}
		darts += amount;
		type = other.getId();
		player.getInventory().deleteItem(new Item(other.getId(), amount));
		blowpipe.setAttribute("blowpipeDarts", darts);
		blowpipe.setAttribute("blowpipeDartType", type);
		player.getChargesManager().checkCharges(blowpipe);
        if (blowpipe.getId() == 12924) {
            blowpipe.setId(12926);
            player.getInventory().refresh(fromSlot, toSlot);
        }
	}

	@Override
	public int[] getItems() {
		return new int[] { 12934, 12924, 12926, 806, 807, 808, 809, 810, 811, 3093, 11230, ItemId.AMETHYST_DART };
	}

}
