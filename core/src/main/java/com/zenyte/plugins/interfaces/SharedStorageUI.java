package com.zenyte.plugins.interfaces;

import com.zenyte.game.model.ui.UserInterface;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 4. mai 2018 : 19:49:51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class SharedStorageUI implements UserInterface {

	@Override
	public void handleComponentClick(final Player player, final int interfaceId, final int componentId, final int slotId, final int itemId, final int optionId, final String option) {
		/*final Controller controller = player.getControllerManager().getController();
		if (!(controller instanceof RaidController)) {
			return;
		}
		final RaidController raidController = (RaidController) controller;
		final Raid raid = raidController.getRaid();
		if (raid == null) {
			return;
		}
		final SharedStorage storage = raid.getStorage();
		if (storage == null) {
			return;
		}
		if (componentId == 5) {
			final int size = storage.getContainer().getContainerSize();
			player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
			player.getPrivateStorage().open(size == 250 ? 30 : size == 500 ? 60 : 90);
			return;
		}
		final int slot = storage.getContainer().getSlotOf(itemId);
		final Item item = storage.getContainer().get(slot);
		if (item == null) {
			return;
		}
		switch (optionId) {
		case 1:
			storage.withdraw(player, slot, 1, true);
			break;
		case 2:
			storage.withdraw(player, slot, 5, true);
			break;
		case 3:
			storage.withdraw(player, slot, 10, true);
			break;
		case 4:
			storage.withdraw(player, slot, Integer.MAX_VALUE, true);
			break;
		case 5:
			player.sendInputInt("Enter amount:", amount -> storage.withdraw(player, slot, amount, true));
			break;
		case 10:
			Examine.sendItemExamine(player, item);
			return;
		}*/
		//TODO REWRITE THIS TRASH
	}

	@Override
	public int[] getInterfaceIds() {
		return new int[] { 550 };
	}

}
