package com.zenyte.game.world.entity.player.action.combat.magic.spelleffect;

import com.zenyte.game.world.entity.Entity;

/**
 * @author Kris | 29. dets 2017 : 20:52.18
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public interface SpellEffect {

	void spellEffect(final Entity player, final Entity target, final int damage);
	
}
