package com.zenyte.game.model.ui;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.Plugin;

/**
 * @author Kris | 10. nov 2017 : 17:20.22
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public interface UserInterface extends Plugin {
	
	void handleComponentClick(final Player player, final int interfaceId, final int componentId, final int slotId, final int itemId, final int optionId, final String option);
	
	int[] getInterfaceIds();
	
}
