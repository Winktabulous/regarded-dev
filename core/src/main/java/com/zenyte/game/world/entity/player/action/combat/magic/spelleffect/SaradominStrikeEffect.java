package com.zenyte.game.world.entity.player.action.combat.magic.spelleffect;

import com.zenyte.game.content.achievementdiary.diaries.WildernessDiary;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Entity.EntityType;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 8. jaan 2018 : 1:04.45
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class SaradominStrikeEffect implements SpellEffect {
	@Override
	public void spellEffect(final Entity player, final Entity target, final int damage) {
		if (target.getEntityType() == EntityType.PLAYER) {
			if (player instanceof Player) {
				final Player p = (Player) player;
				p.getAchievementDiaries().update(WildernessDiary.CAST_GOD_SPELL);
			}
			((Player) target).getPrayerManager().drainPrayerPoints(1);
		}
	}
}
