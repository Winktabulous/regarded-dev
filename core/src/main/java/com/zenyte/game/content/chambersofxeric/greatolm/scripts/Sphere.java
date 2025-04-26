package com.zenyte.game.content.chambersofxeric.greatolm.scripts;

import com.zenyte.game.content.chambersofxeric.greatolm.GreatOlm;
import com.zenyte.game.content.chambersofxeric.greatolm.OlmCombatScript;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.content.skills.prayer.PrayerManager;
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
import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.util.Arrays;
import java.util.List;

/**
 * @author Kris | 16. jaan 2018 : 4:04.09
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class Sphere implements OlmCombatScript {
	private static final Projectile meleeProjectile = new Projectile(1345, 60, 20, 30, 15, 120, 0, 0);
	private static final Projectile rangedProjectile = new Projectile(1343, 60, 20, 30, 15, 120, 0, 0);
	private static final Projectile magicProjectile = new Projectile(1341, 60, 20, 30, 15, 120, 0, 0);
	private static final Graphics meleeGraphics = new Graphics(1346, 0, 96);
	private static final Graphics rangedGraphics = new Graphics(1344, 0, 96);
	private static final Graphics magicGraphics = new Graphics(1342, 0, 96);
	private static final SoundEffect sound = new SoundEffect(1784, 15, 0);

	@Override
	public void handle(final GreatOlm olm) {
		final List<Player> everyone = olm.everyone(olm.getDirection());
		if (everyone.isEmpty()) {
			return;
		}
		olm.getScripts().add(this.getClass());
		final Location face = olm.getFaceCoordinates();
		final IntArrayList typelist = new IntArrayList(Arrays.asList(0, 1, 2));
		int random = Math.min(3, everyone.size());
		olm.performAttack();
		World.sendSoundEffect(olm.getMiddleLocation(), sound);
		while (random-- > 0) {
			final int type = typelist.removeInt(Utils.random(typelist.size() - 1));
			final Player player = everyone.remove(Utils.random(everyone.size() - 1));
			if (player == null) {
				continue;
			}
			final PrayerManager prayer = player.getPrayerManager();
			final boolean overhead = prayer.isActive(Prayer.PROTECT_FROM_MAGIC) || prayer.isActive(Prayer.PROTECT_FROM_MELEE) || prayer.isActive(Prayer.PROTECT_FROM_MISSILES);
			if (overhead) {
				Lightning.deactivateOverheadProtectionPrayers(player, prayer, false);
				player.getPrayerManager().drainPrayerPoints(player.getPrayerManager().getPrayerPoints() / 2);
			}
			switch (type) {
			case 0: 
				player.sendMessage("<col=ff0000>The Great Olm fires a sphere of aggression your way.</col>" + (overhead ? " Your prayers have been sapped." : ""));
				break;
			case 1: 
				player.sendMessage("<col=00ff00>The Great Olm fires a sphere of accuracy and dexterity your way.</col>" + (overhead ? " Your prayers have been sapped." : ""));
				break;
			default: 
				player.sendMessage("<col=0000ff>The Great Olm fires a sphere of magical power your way.</col>" + (overhead ? " Your prayers have been sapped." : ""));
				break;
			}
			World.sendProjectile(face, player, type == 0 ? meleeProjectile : type == 1 ? rangedProjectile : magicProjectile);
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					if (olm.getRoom().getRaid().isDestroyed()) {
						stop();
						return;
					}
					if (player.isDead()) {
						stop();
						olm.getScripts().remove(Sphere.this.getClass());
						return;
					}
					player.setGraphics(type == 0 ? meleeGraphics : type == 1 ? rangedGraphics : magicGraphics);
					final Hit hit = new Hit(olm, Math.min(99, olm.getBypassMode() ? player.getHitpoints() / 3 : player.getHitpoints() / 2), HitType.REGULAR);
					//Setting a predicate on the hit that is executed right before the hitpoints are removed based on the hit; If the predicate returns true, the hit is discarded.
					hit.setPredicate(h -> !(type == 0 && !prayer.isActive(Prayer.PROTECT_FROM_MELEE) || type == 1 && !prayer.isActive(Prayer.PROTECT_FROM_MISSILES) || type == 2 && !prayer.isActive(Prayer.PROTECT_FROM_MAGIC)));
					player.applyHit(hit);
					olm.getScripts().remove(Sphere.this.getClass());
				}
			}, 4);
		}
	}
}
