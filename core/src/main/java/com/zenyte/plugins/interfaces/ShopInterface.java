package com.zenyte.plugins.interfaces;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.UserInterface;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.GameShop;
import mgi.types.config.items.ItemDefinitions;

import static com.zenyte.game.world.entity.player.container.impl.GameShop.INTERFACE;
import static com.zenyte.game.world.entity.player.container.impl.GameShop.INVENTORY_INTERFACE;

/**
 * @author Kris | 10. nov 2017 : 18:00.44
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class ShopInterface implements UserInterface {
	@Override
	public void handleComponentClick(final Player player, final int interfaceId, final int componentId, int slotId, final int itemId, final int optionId, final String option) {
		final Object obj = player.getTemporaryAttributes().get("Shop");
		if (!(obj instanceof GameShop)) {
			return;
		}
		final GameShop shop = (GameShop) obj;
		final Item item = interfaceId == INVENTORY_INTERFACE ? player.getInventory().getContainer().get(slotId) : shop.getContainer().get(slotId - 1);
		if (item == null) {
			return;
		}
		final ItemDefinitions defs = ItemDefinitions.get(item.getId());
		switch (interfaceId) {
		case INTERFACE: 
			if (componentId == 16) {
				slotId--;
				switch (optionId) {
				case 1: 
					if (defs == null) {
						return;
					}
					final String currency = shop.getCurrency() == 995 ? "coins" : shop.getCurrency() == 11849 ? "marks of grace" : "";
					player.sendMessage(defs.getName() + ": currently costs " + defs.getPrice() + " " + currency + ".");
					break;
				case 2: 
					shop.buyItem(player, slotId, 1);
					break;
				case 3: 
					shop.buyItem(player, slotId, 5);
					break;
				case 4: 
					shop.buyItem(player, slotId, 10);
					break;
				case 5: 
					shop.buyItem(player, slotId, 50);
					break;
				case 10: 
					ItemUtil.sendItemExamine(player, item);
					break;
				}
			}
			return;
		case INVENTORY_INTERFACE: 
			if (componentId == 0) {
				switch (optionId) {
				case 1: 
					if (defs == null) {
						return;
					}
					if (!item.isTradable() || item.getId() == 995 || !shop.isGeneralStore() && !shop.getContainer().containsOne(item.getId())) {
						player.sendMessage("You can't sell this item to this shop.");
					} else {
						player.sendMessage(defs.getName() + ": shop will buy for " + defs.getPrice() / 3 + " coins.");
					}
					break;
				case 2: 
					shop.sellItem(player, slotId, 1);
					break;
				case 3: 
					shop.sellItem(player, slotId, 5);
					break;
				case 4: 
					shop.sellItem(player, slotId, 10);
					break;
				case 5: 
					shop.sellItem(player, slotId, 50);
					break;
				case 10: 
					ItemUtil.sendItemExamine(player, item);
					break;
				}
			}
			return;
		}
	}

	@Override
	public int[] getInterfaceIds() {
		return new int[] {INTERFACE, INVENTORY_INTERFACE};
	}
}
