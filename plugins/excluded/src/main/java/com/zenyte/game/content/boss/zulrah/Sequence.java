package com.zenyte.game.content.boss.zulrah;

import com.zenyte.game.world.entity.player.Player;

/**
 * An interface for Zulrah's attacks.
 * @author Kris | 15. march 2018 : 17:55.44
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public interface Sequence {

	void attack(final ZulrahNPC zulrah, final ZulrahInstance instance, final Player target);
	
}
