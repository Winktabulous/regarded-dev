package com.zenyte.plugins.handlers;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.Plugin;

/**
 * @author Kris | 19. juuli 2018 : 22:25:35
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public interface InterfaceSwitchPlugin extends Plugin {

	void switchItem(final Player player, final int fromInterface, final int toInterface, final int fromComponent,
                    final int toComponent, final int fromSlot, final int toSlot);

	int[] getInterfaceIds();
	
}
