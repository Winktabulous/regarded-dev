package com.zenyte.game.model.ui;

import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 10. juuli 2018 : 18:00:05
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public interface DialogueInterface {

	void handleComponentClick(final Player player, final int interfaceId, final int componentId, final int slotId, int itemId, int optionId, String option);
	
	int[] getInterfaceIds();
	
}
