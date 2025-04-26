package com.zenyte.plugins.object;

import com.zenyte.game.GameInterface;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

@SuppressWarnings("unused")
public final class BankObject implements ObjectAction {

	@Override
	public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
		if (option.equals("Bank") || option.equals("Use")) {
            GameInterface.BANK.open(player);
		} else if (option.equalsIgnoreCase("Collect")) {
            GameInterface.GRAND_EXCHANGE_COLLECTION_BOX.open(player);
		} else if (option.equalsIgnoreCase("Presets")) {
			GameInterface.PRESET_MANAGER.open(player);
		} else if (option.equalsIgnoreCase("Last-preset")) {
			player.getPresetManager().loadLastPreset();
		}
	}

	@Override
	public Object[] getObjects() {
		return new Object[] { ObjectId.CHEST_42834, "Bank", "Bank booth", "Bank chest", "Open chest", "Bank counter", 46075, 46223};
	}
}
