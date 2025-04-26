package com.zenyte.game.content.tombsofamascut.npc;

import com.zenyte.game.content.tombsofamascut.encounter.ScabarasEncounter;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;

/**
 * @author Savions.
 */
public class Scarab extends TOANPC implements CombatScript {

	private static final int ID = 11697;
	private static final Animation SPAWN_ANIM = new Animation(9589);
	private static final Animation ATTACK_ANIM = new Animation(9587);
	private static final Projectile ATTACK_PROJECTILE = new Projectile(1766, 11, 22, 51, 16, 15, 64, 5);
	private final boolean isSouthernSpawned;
	private final ScabarasEncounter encounter;

	public Scarab(Location tile, ScabarasEncounter raidArea, boolean isSouthernSpawned) {
		super(ID, tile, Direction.EAST, 64, raidArea, 0, false);
		setAnimation(SPAWN_ANIM);
		this.isSouthernSpawned = isSouthernSpawned;
		this.encounter = raidArea;
		setForceAggressive(true);
		setMaxDistance(48);
		setDeathDelay(3);
		forceCheckAggression = true;
	}

	@Override public float getPointMultiplier() {
		return 0.5F;
	}

	@Override public int attack(Entity target) {
		setAnimation(ATTACK_ANIM);
		World.sendProjectile(this, target, ATTACK_PROJECTILE);
		delayHit(2, target, new Hit(this, getRandomMaxHit(this, 6, AttackType.RANGED, target), HitType.RANGED));
		return combatDefinitions.getAttackSpeed();
	}

	@Override public void onFinish(Entity source) {
		super.onFinish(source);
		encounter.removeScarab(this);
	}

	public boolean isSouthernSpawned() { return isSouthernSpawned; }
}