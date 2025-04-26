package com.zenyte.game.world.region.area.plugins;

import com.zenyte.game.content.skills.magic.spells.MagicSpell;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 25/07/2019 | 23:55
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public interface SpellPlugin {

    boolean canCast(final Player player, final MagicSpell spell);
}
