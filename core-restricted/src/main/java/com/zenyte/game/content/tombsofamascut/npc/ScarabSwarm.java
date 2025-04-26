package com.zenyte.game.content.tombsofamascut.npc;

import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.game.content.tombsofamascut.encounter.KephriEncounter;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPCCombat;
import com.zenyte.game.world.entity.pathfinding.events.npc.NPCEntityEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.EntityStrategy;

/**
 * @author Savions.
 */
public class ScarabSwarm extends TOANPC {

	private static final int ID = 11723;
	private static final Animation SPAWN_ANIM = new Animation(9605);
	private static final Animation FADE_ANIM = new Animation(9607);
	private final KephriEncounter encounter;
	private final boolean medic;
	private boolean preventHeal;
	private int delay = 4;

	public ScarabSwarm(Location tile, boolean medic, final KephriEncounter encounter) {
		super(ID, tile, Direction.NORTH, 0, encounter, 0, false);
		this.encounter = encounter;
		this.medic = medic;
		setAnimation(SPAWN_ANIM);
		setCantInteract(true);
		this.combat = new NPCCombat(this) {
			@Override
			public void setTarget(final Entity target, TargetSwitchCause cause) { }
			@Override
			public void forceTarget(final Entity target) { }
		};
	}

	@Override public void processNPC() {
		if (delay == 2) {
			setCantInteract(false);
		}
		if (((delay <= 0 && !hasWalkSteps()) || (delay > 0 && --delay <= 0)) && !isDying() && !isFinished() &&
				encounter.getKephri() != null && !encounter.getKephri().isDying() && !encounter.getKephri().isFinished() && !preventHeal) {
			setRouteEvent(new NPCEntityEvent(this, new EntityStrategy(encounter.getKephri()), () -> {
				if (encounter.getKephri().inShieldingPhase()) {
					encounter.getKephri().healBoss(this, 1F, medic ? .18F : .08F, medic ? .2F : .1F);
				}
				fadeAway();
			}));
		}
	}

	public void fadeAway() {
		if (isDying() || isFinished()) {
			return;
		}
		preventHeal = true;
		resetWalkSteps();
		setCantInteract(true);
		setAnimation(FADE_ANIM);
		WorldTasksManager.schedule(encounter.addRunningTask(() -> {
			if (!isDying() && !isFinished()) {
				finish();
			}
		}), 2);
	}

	@Override public void sendDeath() {
		super.sendDeath();
		preventHeal = true;
	}

	@Override public boolean addWalkStep(int nextX, int nextY, int lastX, int lastY, boolean check) {
		return super.addWalkStep(nextX, nextY, lastX, lastY, false);
	}

	@Override public boolean isAlwaysTakeMaxHit(HitType type) { return true; }

	@Override public void setMaxHealth() {}

	@Override public void autoRetaliate(Entity source) {}

	@Override public boolean isEntityClipped() { return false; }

	@Override public float getPointMultiplier() { return .5F; }

	@Override public void setRespawnTask() {}

	@Override public void setTarget(Entity target, TargetSwitchCause cause) {}

	@Override public void setFaceEntity(Entity entity) {}
}
