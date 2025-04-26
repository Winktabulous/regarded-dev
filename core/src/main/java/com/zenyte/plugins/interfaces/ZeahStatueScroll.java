package com.zenyte.plugins.interfaces;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.model.ui.UserInterface;
import com.zenyte.game.world.entity.player.Player;

public class ZeahStatueScroll implements UserInterface {

	private static final int INTERFACE = 116;
	private static final String[] LINES = {
		"Kourend the magnificent,", "Kourend the resplendent,", "Kourend, the most powerful", "of the nations in the world.",
		"Kourend is our citadel", "Kourend is our homeland.", "We will live and die for Kourend,", "for the city of our birth.",
		"From the rugged mountains,", "to the foaming seas,", "here we make our home in Kourend,", "all together, all in peace.",
		"Kourend is a monument,", "Kourend is a triumph;", "it shall stand here forever", "in the shadow of our King."
	};
	
	public static void open(final Player player) {
		int index = 6;
		player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, INTERFACE);
		player.getPacketDispatcher().sendComponentText(INTERFACE, 4, "Kourend the Magnificent");
		for(final String line : LINES) {
			if(index % 5 == 0) {
				index += 1;
			}

			player.getPacketDispatcher().sendComponentText(INTERFACE, index, line);
			index++;
		}
	}
	
	@Override
	public void handleComponentClick(final Player player, final int interfaceId, final int componentId, final int slotId, final int itemId, final int optionId, final String option) {	}

	@Override
	public int[] getInterfaceIds() {
		return new int[] { INTERFACE };
	}

}
