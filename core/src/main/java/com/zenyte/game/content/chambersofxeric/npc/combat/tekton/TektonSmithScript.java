package com.zenyte.game.content.chambersofxeric.npc.combat.tekton;

import com.zenyte.game.content.chambersofxeric.npc.Tekton;
import com.zenyte.game.content.chambersofxeric.room.TektonRoom;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Kris | 10. mai 2018 : 00:54:08
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class TektonSmithScript implements TektonScript {
	private static final Projectile sparkProjectile = new Projectile(660, 100, 5, 0, 25, 60, 64, 5);
	private static final Graphics explosion = new Graphics(659);
	private static final SoundEffect flyingSound = new SoundEffect(155, 8, 0);
	private static final SoundEffect hittingSound = new SoundEffect(156, 5, 0);

	@Override
	public void execute(final Tekton tekton) {
		tekton.animate("lean to anvil");
		WorldTasksManager.schedule(new WorldTask() {
			private int ticks = -2;
			@Override
			public void run() {
				if (tekton.getRoom().getRaid().isDestroyed() || tekton.isDead() || tekton.isFinished()) {
					stop();
					return;
				}
				if (ticks == -2) {
					tekton.setTransformation(7545);
				}
				if (tekton.isRoomEmpty()) {
					tekton.resetToDefaultState();
					stop();
					return;
				}
				if (ticks % 4 == 0) {
					final ArrayList<Location> sparks = new ArrayList<>(tekton.getRoom().getSparks());
					final List<Player> availablePlayers = tekton.getRoom().getPlayers().stream().filter(p -> !p.getLocation().withinDistance(tekton.getRoom().getSafeTile(), 1)).collect(Collectors.toList());
					if (availablePlayers.size() > 0) {
						final int length = Math.min(4, availablePlayers.size());
						for (int i = 0; i < length; i++) {
							sendSparks(tekton, availablePlayers.remove(Utils.random(availablePlayers.size() - 1)), sparks, length == 4 ? 1 : length == 3 ? (i == 0 ? 2 : 1) : 2);
						}
					}
				}
				if (ticks == 20) {
					tekton.setEnrageTime(100);
					tekton.heal((int) (tekton.getMaxHitpoints() * 0.065F));
					tekton.execute("engage");
					stop();
					return;
				}
				ticks++;
			}
		}, 1, 0);
	}

	private void sendSparks(final Tekton tekton, final Entity target, final List<Location> sparks, final int count) {
		final TektonRoom room = tekton.getRoom();
		final Location to = new Location(target.getLocation());
		for (int i = 0; i < count; i++) {
			final Location from = sparks.remove(Utils.random(sparks.size() - 1));
			World.sendSoundEffect(from, flyingSound);
			WorldTasksManager.schedule(() -> {
				World.sendGraphics(explosion, to);
				World.sendSoundEffect(to, hittingSound);
				for (final Player p : room.getRaid().getPlayers()) {
					if (p.getLocation().withinDistance(to, 1)) {
						p.applyHit(new Hit(tekton, Utils.random(10, 20), HitType.REGULAR));
					}
				}
			}, World.sendProjectile(from, to, sparkProjectile));
		}
	}
}
