package com.zenyte.game.world.entity.npc.impl.wilderness;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 12 mrt. 2018 : 21:19:36
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class ScorpiaGuardian extends NPC implements Spawnable {

	private static final Projectile HEALING_PROJ = new Projectile(118, 43, 25, 57, 15, 18, 64, 5);

	private int ticks;

	public ScorpiaGuardian(final int id, final Location tile, final Direction direction, final int radius) {
		super(id, tile, direction, radius);
	}

	@Override
	public void processNPC() {
		final Object obj = getTemporaryAttributes().get("ScorpiaNPC");
		if (!(obj instanceof Scorpia)) {
			return;
		}
		final Scorpia scorpia = (Scorpia) obj;
		if (!scorpia.isDead()) {
			final Location from = new Location(getLocation().getCoordFaceX(getSize()), getLocation().getCoordFaceY(getSize()), getPlane());
			faceEntity(scorpia);
			if (ticks % 5 == 0 && from.withinDistance(scorpia.getMiddleLocation(), 4)) {
				World.sendProjectile(from, scorpia, HEALING_PROJ);
				scorpia.heal(3);
			}
			ticks++;
		}
		cancelCombat();
	}

	@Override protected void onDeath(Entity source) {
		super.onDeath(source);
		if (source instanceof Player) {
			final Object obj = getTemporaryAttributes().get("ScorpiaNPC");
			if (!(obj instanceof Scorpia)) {
				return;
			}
			final Scorpia scorpia = (Scorpia) obj;
			scorpia.setKilledGuardian();
		}
	}

	@Override
	public void setRespawnTask() {
	}

	@Override
	public boolean validate(final int id, final String name) {
		return id == 6617;
	}

}
