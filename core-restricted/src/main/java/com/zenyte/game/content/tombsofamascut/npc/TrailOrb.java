package com.zenyte.game.content.tombsofamascut.npc;

import com.zenyte.game.content.tombsofamascut.encounter.AkkhaEncounter;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Savions.
 */
public class TrailOrb extends NPC {

	private final AkkhaEncounter encounter;
	private boolean finish;
	private int aliveTicks = 8;

	public TrailOrb(int id, Location tile, AkkhaEncounter encounter) {
		super(id, tile, true);
		this.encounter = encounter;
		setRadius(0);
		setForceAggressive(false);
	}

	@Override public void processNPC() {
		if (--aliveTicks <= 0 || finish) {
			finish();
			return;
		}
		final Player[] players = encounter.getChallengePlayers();
		for (final Player p : players) {
			if (p != null && getLocation().equals(p.getLocation())) {
				p.getTemporaryAttributes().put("mark_akkha_trail_orb_explosion_" + id, true);
				finish = true;
			}
		}
	}

	@Override protected void onFinish(Entity source) {
		super.onFinish(source);
		encounter.removeTrailOrb(this);
	}

	@Override public boolean addWalkStep(int nextX, int nextY, int lastX, int lastY, boolean check) { return false; }

	@Override public void setRespawnTask() {}

	@Override public boolean canAttack(Player source) {
		return false;
	}

	@Override public boolean isEntityClipped() {
		return false;
	}
}
