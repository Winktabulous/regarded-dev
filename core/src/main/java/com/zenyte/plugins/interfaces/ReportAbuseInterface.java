package com.zenyte.plugins.interfaces;

import com.zenyte.game.model.ui.UserInterface;
import com.zenyte.game.world.entity.player.Player;

public class ReportAbuseInterface implements UserInterface {

	private static final int INTERFACE = 553;
	
	public static void open(final Player player) {
		
	}
	
	@Override
	public void handleComponentClick(final Player player, final int interfaceId, final int componentId, final int slotId, final int itemId, final int optionId, final String option) {
		
	}

	@Override
	public int[] getInterfaceIds() {
		return new int[] { INTERFACE };
	}

}
