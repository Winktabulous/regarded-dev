package com.zenyte.game.content.tombsofamascut.npc;

import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.content.tombsofamascut.encounter.CrondisPuzzleEncounter;
import com.zenyte.game.content.tombsofamascut.raid.EncounterStage;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AttackDefinitions;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;

import java.util.List;

/**
 * @author Savions.
 */
public class CrondisCrocodile extends TOANPC implements CombatScript {

	private static final int ID = 11705;

	private final PalmOfResourcefulness palmOfResourcefulness;
	private final CrondisPuzzleEncounter crondisPuzzleEncounter;

	public CrondisCrocodile(Location tile, PalmOfResourcefulness palmOfResourcefulness, CrondisPuzzleEncounter crondisPuzzleEncounter) {
		super(ID, tile, Direction.SOUTH, 12, crondisPuzzleEncounter, 0, false);
		this.palmOfResourcefulness = palmOfResourcefulness;
		this.crondisPuzzleEncounter = crondisPuzzleEncounter;
		setFaceLocation(palmOfResourcefulness.getLocation());
		forceCheckAggression = true;
	}

	@Override public void autoRetaliate(Entity source) {
		if (!(combat.getTarget() instanceof PalmOfResourcefulness)) {
			super.autoRetaliate(source);
		}
	}

	@Override public boolean checkAggressivity() {
		if (EncounterStage.STARTED.equals(crondisPuzzleEncounter.getStage())) {
			if (palmOfResourcefulness.equals(combat.getTarget()) && palmOfResourcefulness.getHitpoints() >= palmOfResourcefulness.getMaxHitpoints()) {
				getCombat().setTarget(null);
			}
			for (Player player : crondisPuzzleEncounter.getChallengePlayers()) {
				if (location.withinDistance(player.getX(), player.getY(), 3)) {
					final Item container = player.getInventory().getAny(CrondisPuzzleEncounter.CONTAINER_ITEM_ID);
					if (container != null && container.getCharges() > 0) {
						resetWalkSteps();
						getCombat().setTarget(player);
						return true;
					}
				}
			}
			if (combat.getTarget() == null && palmOfResourcefulness.getHitpoints() < palmOfResourcefulness.getMaxHitpoints()) {
				resetWalkSteps();
				getCombat().setTarget(palmOfResourcefulness);
				return true;
			}
		}
		return false;
	};

	@Override public void finish() {
		super.finish();
		crondisPuzzleEncounter.finishCrocodile(this);
	}

	public void remove() {
		super.finish();
	}

	@Override public int attack(Entity target) {
		final AttackDefinitions attDefs = combatDefinitions.getAttackDefinitions();
		setAnimation(attDefs.getAnimation());
		if (target instanceof Player player) {
			player.sendSound(attDefs.getStartSound());
			boolean hit = getRandomMaxHit(this, 1000, AttackType.CRUSH, target) > 0;
			if (!hit) {
				delayHit(this, 0, target, new Hit(this, 0, HitType.MELEE));
			} else {
				int damage = 18;
				long lastHit = (long) player.getTemporaryAttributes().getOrDefault("toa_acid_trail_hit", 0L);
				if (lastHit <= WorldThread.getCurrentCycle() + 98) {
					damage += 3;
				}
				lastHit = (long) player.getTemporaryAttributes().getOrDefault("toa_acid_spike_hit", 0L);
				if (lastHit <= WorldThread.getCurrentCycle() + 98) {
					damage += 3;
				}
				if (player.getPrayerManager().isActive(Prayer.PROTECT_FROM_MELEE)) {
					damage /= 3;
					player.getPrayerManager().drainPrayerPoints(12);
				}
				delayHit(this, 0, target, new Hit(this, damage, HitType.MELEE));
			}
		} else if (palmOfResourcefulness.equals(combat.getTarget())) {
			delayHit(this, 0, target, new Hit(this, Utils.random(2, 5), HitType.PALM_LOWER));
		}
		return 7;
	}

	@Override public float getPointMultiplier() {
		return 1;
	}
}
