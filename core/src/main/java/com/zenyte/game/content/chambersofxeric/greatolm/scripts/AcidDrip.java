package com.zenyte.game.content.chambersofxeric.greatolm.scripts;

import com.zenyte.game.content.chambersofxeric.greatolm.AcidPool;
import com.zenyte.game.content.chambersofxeric.greatolm.GreatOlm;
import com.zenyte.game.content.chambersofxeric.greatolm.OlmCombatScript;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 15. jaan 2018 : 21:11.51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class AcidDrip implements OlmCombatScript {
	private static final Projectile projectile = new Projectile(1364, 60, 20, 30, 5, 30, 0, 5);
	private static final SoundEffect sound = new SoundEffect(1784, 15, 0);

	@Override
	public void handle(final GreatOlm olm) {
		final Player t = olm.random(olm.getDirection());
		if (t == null) {
			return;
		}
		olm.getScripts().add(this.getClass());
		final Location face = olm.getFaceCoordinates();
		olm.performAttack();
		World.sendSoundEffect(olm.getMiddleLocation(), sound);
		World.sendProjectile(face, t, projectile);
		WorldTasksManager.schedule(new WorldTask() {
			private int ticks;
			@Override
			public void run() {
				if (olm.getRoom().getRaid().isDestroyed()) {
					stop();
					return;
				}
				if (ticks++ == 0) {
					t.getTemporaryAttributes().put("acidDrip", Utils.currentTimeMillis() + 12500);
					t.sendMessage("<col=ff0000>The Great Olm has smothered you in acid. It starts to drip off slowly.");
					if (!olm.getRoom().containsPool(t.getLocation())) {
						final AcidPool acid = new AcidPool(new Location(t.getLocation()));
						olm.getRoom().getAcidPools().add(acid);
						acid.process();//Spawns it immediately.
					}
				} else {
					olm.getScripts().remove(AcidDrip.this.getClass());
					stop();
				}
			}
		}, World.sendProjectile(face, t, projectile), 21);
	}
}
