package com.zenyte.game.content.tombsofamascut.npc;

import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.game.content.tombsofamascut.encounter.AkkhaEncounter;
import com.zenyte.game.content.tombsofamascut.raid.EncounterStage;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.EntityHitBar;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPCCombat;

/**
 * @author Savions.
 */
public class AkkhaFinalShadow extends TOANPC {

	private static final Animation DEATH_ANIM = new Animation(9792);
	private final int hitThreshold;
	private final int maxHitPoints;
	private int currentReceivedHits;
	private final AkkhaEncounter encounter;
	public AkkhaFinalShadow(int id, Location tile, Direction facing, AkkhaEncounter encounter, int maxHitPoints) {
		super(id, tile, facing, 0, encounter);
		super.hitBar = new EntityHitBar(this) {
			@Override public int getType() {
				return 19;
			}
		};
		hitThreshold = 1 + encounter.getStartTeamSize() * 2;
		this.encounter = encounter;
		deathDelay = 2;
		setAnimation(Akkha.BECOME_VISIBLE_ANIM);
		this.maxHitPoints = maxHitPoints;
		combatDefinitions.setHitpoints(maxHitPoints);
		setHitpoints((int) (maxHitPoints * 0.2));
		this.combat = new NPCCombat(this) {
			@Override
			public void setTarget(final Entity target, TargetSwitchCause cause) { }
			@Override
			public void forceTarget(final Entity target) { }
		};
	}

	@Override public int getMaxHitpoints() { return maxHitPoints; }

	@Override public void listenScheduleHit(Hit hit) {
		if (!HitType.MELEE.equals(hit.getHitType())) {
			hit.setDamage(0);
		}
		super.listenScheduleHit(hit);
	}

	@Override public void processNPC() {
		super.processNPC();
		if (id != Akkha.FINAL_ID) {
			setTinting(AkkhaShadow.INVULNERABLE_TINTING);
		}
	}

	@Override public void handleIngoingHit(Hit hit) {
		super.handleIngoingHit(hit);
		if (id != Akkha.FINAL_ID) {
			hit.setDamage(0);
		}
		if (++currentReceivedHits == hitThreshold) {
			encounter.teleportFakeShadows();
		}
	}

	@Override public void sendDeath() {
		this.hitpoints = 0;
		super.sendDeath();
		if (id == Akkha.FINAL_ID) {
			setAnimation(DEATH_ANIM);
			encounter.sendFakeShadowDeaths();
		}
	}

	@Override public boolean setHitpoints(int amount) {
		final int currentHitPoints = getHitpoints();
		final boolean set = super.setHitpoints(amount);
		if (currentHitPoints > 0 && id == Akkha.FINAL_ID && encounter != null && EncounterStage.STARTED.equals(encounter.getStage())) {
			encounter.getPlayers().forEach(p -> p.getHpHud().updateValue(hitpoints));
		}
		return set;
	}

	@Override protected void onFinish(Entity source) {
		super.onFinish(source);
		if (id == Akkha.FINAL_ID) {
			encounter.completeRoom();
		}
	}

	@Override public void setUnprioritizedAnimation(Animation animation) {}

	@Override public boolean isCycleHealable() { return false; }

	@Override public float getPointMultiplier() { return 0; }

	@Override public void setTarget(Entity target, TargetSwitchCause cause) {}

	@Override public void setFaceEntity(Entity entity) {}

	@Override public void setRespawnTask() {}

	@Override public boolean isEntityClipped() { return false; }

	@Override public boolean addWalkStep(int nextX, int nextY, int lastX, int lastY, boolean check) { return false; }

	public void resetCurrentReceivedHits() { this.currentReceivedHits = 0; }
}
