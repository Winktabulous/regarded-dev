package com.zenyte.game.content.skills.magic.spells;

import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 15. juuli 2018 : 22:03:09
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public interface TeleportSpell extends DefaultSpell {

	@Override
    void execute(final Player player, final int optionId, final String option);
	
	@Override
	@Deprecated
    default boolean spellEffect(final Player player, final int optionId, final String option) {
		return false;
	}
	
}
