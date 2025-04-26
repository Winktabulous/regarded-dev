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
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;

/**
 * @author Kris | 19. jaan 2018 : 22:05.26
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class FallingCrystal implements OlmCombatScript {
	private static final Graphics graphics = new Graphics(246);
	private static final Graphics shatterGraphics = new Graphics(1338);
	private static final Projectile projectile = new Projectile(1352, 255, 0, 0, 0, 60, 127, 5);
	private static final SoundEffect crySound = new SoundEffect(324, 15, 0);
	private static final SoundEffect shatterSound = new SoundEffect(3821, 5, 0);

	public FallingCrystal(final Player player) {
		target = player;
	}

	private final Player target;

	@Override
	public void handle(final GreatOlm olm) {
		if (target == null) {
			return;
		}
		olm.getScripts().add(this.getClass());
		World.sendSoundEffect(olm.getMiddleLocation(), crySound);
		target.sendMessage("<col=ff0000>The Great Olm has chosen you as its target - watch out!");
		for (final Player player : olm.everyone(GreatOlm.ENTIRE_CHAMBER)) {
			if (player == target) {
				continue;
			}
			player.sendMessage("The Great Olm sounds a cry...");
		}
		olm.performAttack();
		WorldTasksManager.schedule(() -> target.setGraphics(graphics));
		WorldTasksManager.schedule(new WorldTask() {
			private int ticks = 12;
			@Override
			public void run() {
				if (olm.getRoom().getRaid().isDestroyed() || target.isDead() || target.isFinished() || !olm.getRoom().inChamber(target.getLocation()) || olm.getRaid().isCompleted()) {
					olm.getScripts().remove(FallingCrystal.this.getClass());
					stop();
					return;
				}
				if (ticks-- == 0) {
					olm.getScripts().remove(FallingCrystal.this.getClass());
					stop();
					return;
				}
				target.setGraphics(graphics);
				final Location tile = new Location(target.getLocation());
				World.sendProjectile(target.getLocation().transform(-1, 0, 0), tile, projectile);
				WorldTasksManager.schedule(() -> {
					World.sendGraphics(shatterGraphics, tile);
					World.sendSoundEffect(tile, shatterSound);
					for (final Player player : olm.everyone(GreatOlm.ENTIRE_CHAMBER)) {
						if (player.getLocation().getPositionHash() == tile.getPositionHash()) {
							CombatUtilities.delayHit(olm, -1, player, new Hit(olm, Utils.random(15, 25), HitType.REGULAR));
						}
					}
				}, 1);
			}
		}, 2, 1);
	}
}
