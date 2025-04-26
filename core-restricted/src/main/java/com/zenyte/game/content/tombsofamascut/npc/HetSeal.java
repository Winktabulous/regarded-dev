package com.zenyte.game.content.tombsofamascut.npc;

import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.game.content.tombsofamascut.encounter.HetEncounter;
import com.zenyte.game.content.tombsofamascut.raid.EncounterStage;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.EntityHitBar;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.RemoveHitBar;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.npc.NPCCombat;

/**
 * @author Savions.
 */
public class HetSeal extends TOANPC {

	public static final int ID = 11706;
	private static final int HEALTH_INCREMENT = 96;
	private static final Graphics PROTECTED_GFX = new Graphics(2122);
	private final HetEncounter encounter;
	private final HitBar newHitBar = new HitBar(this);

	public HetSeal(Location tile, HetEncounter encounter) {
		super(ID, tile, Direction.SOUTH, 0, encounter, 0, false);
		this.encounter = encounter;
		super.hitBar = newHitBar;
		this.combat = new NPCCombat(this) {
			@Override
			public void setTarget(final Entity target, TargetSwitchCause cause) { }
			@Override
			public void forceTarget(final Entity target) { }
		};
	}

	@Override public void processNPC() {
		super.processNPC();
		if (EncounterStage.STARTED.equals(encounter.getStage())) {
			if (id == ID) {
				setGraphics(PROTECTED_GFX);
			}
			getHitBars().clear();
			getHitBars().add(newHitBar);
			getUpdateFlags().flag(UpdateFlag.HIT);
		}
	}

	@Override public boolean setHitpoints(int amount) {
		final boolean set = super.setHitpoints(amount);
		if (encounter != null && EncounterStage.STARTED.equals(encounter.getStage())) {
			encounter.getPlayers().forEach(p -> p.getHpHud().updateValue(hitpoints));
		}
		return set;
	}

	@Override public void setMaxHealth() {
		final int maxHitPoints = combatDefinitions.getHitpoints() + (HEALTH_INCREMENT * (toaRaidArea.getStartTeamSize() - 1));
		combatDefinitions.setHitpoints(maxHitPoints);
		setHitpoints(maxHitPoints);
	}

	@Override public void sendDeath() {
		getHitBars().clear();
		getHitBars().add(new RemoveHitBar(hitBar.getType()));
		getUpdateFlags().flag(UpdateFlag.HIT);
		encounter.completeRoom();
		setTransformation(ID + 1);
	}

	@Override public boolean addWalkStep(int nextX, int nextY, int lastX, int lastY, boolean check) { return false; }

	@Override public void setRespawnTask() {}

	@Override public void setTarget(Entity target, TargetSwitchCause cause) {}

	@Override public void setFaceEntity(Entity entity) {}

	@Override public float getPointMultiplier() {
		return 2.5f;
	}

	private static class HitBar extends EntityHitBar {

		public HitBar(Entity entity) {
			super(entity);
		}

		@Override public int getType() {
			return 10;
		}
	}
}
