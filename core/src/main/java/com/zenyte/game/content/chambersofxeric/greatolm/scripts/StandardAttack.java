package com.zenyte.game.content.chambersofxeric.greatolm.scripts;

import com.zenyte.game.content.chambersofxeric.ScalingMechanics;
import com.zenyte.game.content.chambersofxeric.greatolm.GreatOlm;
import com.zenyte.game.content.chambersofxeric.greatolm.OlmCombatScript;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;

import java.util.List;

/**
 * @author Kris | 18. jaan 2018 : 23:45.29
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class StandardAttack implements OlmCombatScript {
	private static final Projectile rangedProjectile = new Projectile(1340, 65, 15, 30, 15, 18, 0, 5);
	private static final Projectile magicProjectile = new Projectile(1339, 65, 15, 30, 15, 18, 0, 5);
	private static final SoundEffect rangedSound = new SoundEffect(1784, 15, 0);
	private static final SoundEffect magicSound = new SoundEffect(3749, 15, 0);

	@Override
	public void handle(final GreatOlm olm) {
		if (Utils.random(2) == 0) {
			olm.setRanging(!olm.isRanging());
		}
		final boolean ranged = olm.isRanging();
		final Location face = olm.getFaceCoordinates();
		final List<Player> everyone = olm.everyone(olm.getDirection());
		if (everyone.isEmpty()) {
			return;
		}
		olm.performAttack();
		World.sendSoundEffect(olm.getMiddleLocation(), ranged ? rangedSound : magicSound);
		for (final Player player : everyone) {
			World.sendProjectile(face, player, ranged ? rangedProjectile : magicProjectile);
			CombatUtilities.delayHit(olm, rangedProjectile.getTime(face, player.getLocation()), player, new Hit(olm, CombatUtilities.getRandomMaxHit(olm, ScalingMechanics.getOlmMaxHitStandard(olm.getRaid()), ranged ? CombatScript.RANGED : CombatScript.MAGIC, player), ranged ? HitType.RANGED : HitType.MAGIC));
		}
	}
}
