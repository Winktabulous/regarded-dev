package com.zenyte.game.content.chambersofxeric.greatolm.scripts;

import com.zenyte.game.content.chambersofxeric.greatolm.GreatOlm;
import com.zenyte.game.content.chambersofxeric.greatolm.OlmCombatScript;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;

import java.util.List;

/**
 * @author Kris | 18. jaan 2018 : 22:56.20
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class LifeSiphon implements OlmCombatScript {
	private static final Projectile projectile = new Projectile(1355, 65, 0, 30, 15, 90, 0, 0);
	private static final Graphics graphics = new Graphics(1363);
	private static final SoundEffect hitSound = new SoundEffect(124, 5, 0);
	private static final SoundEffect startSound = new SoundEffect(1784, 15, 0);
	private static final SoundEffect splashSound = new SoundEffect(3308, 10, 0);
	private Location firstSpot;
	private Location secondSpot;

	@Override
	public void handle(final GreatOlm olm) {
		final Location face = olm.getFaceCoordinates();
		firstSpot = olm.randomLocation(olm.getDirection());
		if (firstSpot == null) {
			return;
		}
		olm.getScripts().add(this.getClass());
		olm.performAttack();
		World.sendSoundEffect(olm.getMiddleLocation(), startSound);
		while (true) {
			secondSpot = olm.randomLocation(olm.getDirection());
			if (secondSpot == null) {
				continue;
			}
			if (secondSpot.getPositionHash() == firstSpot.getPositionHash()) {
				continue;
			}
			break;
		}
		World.sendProjectile(face, firstSpot, projectile);
		World.sendProjectile(face, secondSpot, projectile);
		WorldTasksManager.schedule(new WorldTask() {
			private int ticks;
			@Override
			public void run() {
				if (olm.getRoom().getRaid().isDestroyed()) {
					stop();
					return;
				}
				if (ticks++ == 0) {
					World.sendGraphics(graphics, firstSpot);
					World.sendGraphics(graphics, secondSpot);
					World.sendSoundEffect(firstSpot, splashSound);
					World.sendSoundEffect(secondSpot, splashSound);
				} else {
					stop();
					olm.getScripts().remove(LifeSiphon.this.getClass());
					final List<Player> everyone = olm.everyone(GreatOlm.ENTIRE_CHAMBER);
					int damage = 0;
					World.sendSoundEffect(firstSpot, hitSound);
					World.sendSoundEffect(secondSpot, hitSound);
					if (!everyone.isEmpty()) {
						final int firstHash = firstSpot.getPositionHash();
						final int secondHash = secondSpot.getPositionHash();
						for (final Player p : everyone) {
							final int hash = p.getLocation().getPositionHash();
							if (hash != firstHash && hash != secondHash) {
								final int dmg = Utils.random(15, 25);
								damage += dmg;
								p.applyHit(new Hit(dmg, HitType.REGULAR));
								p.setGraphics(CombatSpell.CRUMBLE_UNDEAD.getHitGfx());
							}
						}
						final Hit hit = new Hit(damage * 5, HitType.HEALED);
						hit.setForcedHitsplat(true);
						olm.applyHit(hit);
					}
				}
			}
		}, 3, 5);
	}
}
