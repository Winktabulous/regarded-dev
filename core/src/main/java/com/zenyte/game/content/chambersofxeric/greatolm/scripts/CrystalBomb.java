package com.zenyte.game.content.chambersofxeric.greatolm.scripts;

import com.zenyte.game.content.chambersofxeric.ScalingMechanics;
import com.zenyte.game.content.chambersofxeric.greatolm.GreatOlm;
import com.zenyte.game.content.chambersofxeric.greatolm.OlmCombatScript;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kris | 17. jaan 2018 : 17:59.53
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class CrystalBomb implements OlmCombatScript {
	private static final Projectile projectile = new Projectile(1357, 60, 5, 30, 5, 90, 0, 0);
	private static final Graphics explosion = new Graphics(40);
	private final List<Bomb> crystalBombs = new ArrayList<>();
	private static final SoundEffect sound = new SoundEffect(3120, 10, 0);
	private static final SoundEffect explosionSound = new SoundEffect(1021, 15, 0);

	@Override
	public void handle(final GreatOlm olm) {
		olm.getScripts().add(this.getClass());
		int bombCount = ScalingMechanics.getOlmBombCount(olm.getRaid());
		olm.performAttack();
		final Location head = olm.getFaceCoordinates();
		bombs:
		while (bombCount-- > 0) {
			int trycount = 50;
			loop:
			while (trycount-- > 0) {
				final Location random = olm.randomLocation(GreatOlm.ENTIRE_CHAMBER);
				if (random == null) {
					continue;
				}
				for (final Bomb bomb : crystalBombs) {
					if (bomb.getTileDistance(random) <= 2) {
						continue loop;
					}
				}
				final Bomb bomb = new Bomb(random);
				crystalBombs.add(bomb);
				World.sendProjectile(head, random, projectile);
				WorldTasksManager.schedule(() -> World.spawnObject(bomb), 3);
				continue bombs;
			}
		}
		WorldTasksManager.schedule(() -> {
			for (final CrystalBomb.Bomb bomb : crystalBombs) {
				for (int i = 0; i < 4; i++) {
					World.sendSoundEffect(bomb, new SoundEffect(sound.getId(), sound.getRadius(), i * 50));
				}
			}
		}, 3);
		WorldTasksManager.schedule(new WorldTask() {
			private boolean first;
			@Override
			public void run() {
				if (olm.getRoom().getRaid().isDestroyed()) {
					stop();
					return;
				}
				if (!first) {
					for (final Bomb bomb : crystalBombs) {
						World.sendGraphics(explosion, bomb);
						World.sendSoundEffect(bomb, explosionSound);
						World.removeObject(bomb);
					}
					first = true;
					return;
				}
				olm.getScripts().remove(CrystalBomb.this.getClass());
				final List<Player> everyone = olm.everyone(GreatOlm.ENTIRE_CHAMBER);
				for (final Bomb bomb : crystalBombs) {
					for (final Player p : everyone) {
						final int distance = p.getLocation().getTileDistance(bomb);
						if (distance <= 4) {
							final int damage = distance <= 0 ? 60 : distance == 1 ? 45 : distance == 2 ? 35 : distance == 3 ? 25 : 15;
							p.applyHit(new Hit(olm, damage, HitType.REGULAR));
						}
					}
				}
				stop();
			}
		}, 9, 0);
	}


	private static final class Bomb extends WorldObject {
		private static final int CRYSTAL_BOMB = 29766;

		Bomb(final Location tile) {
			super(CRYSTAL_BOMB, 10, 0, tile);
		}
	}
}
