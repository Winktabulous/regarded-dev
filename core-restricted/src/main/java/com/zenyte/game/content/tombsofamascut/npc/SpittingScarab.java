package com.zenyte.game.content.tombsofamascut.npc;

import com.zenyte.game.content.tombsofamascut.encounter.KephriEncounter;
import com.zenyte.game.content.tombsofamascut.raid.EncounterStage;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.Toxins;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Savions.
 */
public class SpittingScarab extends TOANPC implements CombatScript {

	private static final int ID = 11725;
	private static final Location SPAWN_LOC = new Location(3556, 5413);
	private static final Animation SPAWN_ANIM = new Animation(9589);
	private static final Animation ATTACK_ANIM = new Animation(9588);
	private static final Projectile ATTACK_PROJECTILE = new Projectile(2152, 24, 31, 36, 16, 14, 64, 5);
	private final KephriEncounter encounter;

	public SpittingScarab(KephriEncounter encounter) {
		super(ID, encounter.getLocation(SPAWN_LOC), Direction.SOUTH_WEST, 0, encounter);
		this.encounter = encounter;
		setRandomTarget();
		setMaxDistance(48);
		setAggressionDistance(48);
		getCombat().setCombatDelay(getCombat().getCombatDelay() + 4);
	}

	@Override public void processNPC() {
		super.processNPC();
		if (combat.getTarget() == null || combat.getTarget().isDying() || !encounter.insideChallengeArea(combat.getTarget())) {
			setRandomTarget();
		}
	}

	@Override public NPC spawn() {
		setAnimation(SPAWN_ANIM);
		return super.spawn();
	}

	private void setRandomTarget() {
		final Player[] players = encounter.getChallengePlayers();
		if (players.length > 0) {
			getCombat().setTarget(players[Utils.random(players.length - 1)]);
		}
	}

	@Override public double getRangedPrayerMultiplier() { return 0.2; }

	@Override public boolean isIntelligent() { return true; }

	@Override public float getPointMultiplier() {
		return .5F;
	}

	@Override public int attack(Entity target) {
		if (encounter.getKephri() != null) {
			setAnimation(ATTACK_ANIM);
			final Player[] players = encounter.getChallengePlayers();
			for (Player p : players) {
				if (p != null) {
					World.sendProjectile(this, p, ATTACK_PROJECTILE);
					delayHit(this, 2, p, new Hit(this, getRandomMaxHit(this, encounter.getKephri().getMaxHit(10), AttackType.RANGED, p), HitType.RANGED));
					WorldTasksManager.schedule(encounter.addRunningTask(() -> {
						if (!p.isDying() && encounter.insideChallengeArea(p) && encounter.getKephri() != null && !encounter.getKephri().isDying()
								&& !encounter.getKephri().isFinished() && EncounterStage.STARTED.equals(encounter.getStage())) {
							p.getToxins().applyToxin(Toxins.ToxinType.POISON, 5, SpittingScarab.this);
						}
					}), 1);
				}
			}
		}
		return 3;
	}
}
