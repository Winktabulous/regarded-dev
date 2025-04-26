package com.zenyte.game.content.tombsofamascut.npc;

import com.zenyte.game.content.tombsofamascut.encounter.CrondisPuzzleEncounter;
import com.zenyte.game.content.tombsofamascut.raid.EncounterStage;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.*;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combatdefs.NPCCDLoader;
import com.zenyte.game.world.entity.npc.combatdefs.NPCCombatDefinitions;
import mgi.types.config.HitbarDefinitions;

/**
 * @author Savions.
 */
public class PalmOfResourcefulness extends NPC {

	private static final SoundEffect PALM_TREE_UPGRADE_SOUND = new SoundEffect(6516, 1, 15);
	private static final SoundEffect PALM_TREE_DOWNGRADE_SOUND = new SoundEffect(6529, 1, 15);
	private final CrondisPuzzleEncounter crondisPuzzleEncounter;
	private final HitBar PALM_HITBAR = new HitBar() {

		@Override public int getType() {
			return 11;
		}

		@Override public int getPercentage() {
			final int multiplier = getMultiplier();
			final float mod = (float) getMaxHitpoints() / (multiplier);
			return multiplier - Math.min((int) ((hitpoints + mod) / mod), multiplier);
		}

		public int getMultiplier() {
			final int type = getType();
			return HitbarDefinitions.get(type).getSize();
		}
	};

	public PalmOfResourcefulness(int id, Location tile, CrondisPuzzleEncounter crondisPuzzleEncounter) {
		super(id, tile, Direction.SOUTH, 0);
		this.crondisPuzzleEncounter = crondisPuzzleEncounter;
		super.hitBar = PALM_HITBAR;
	}

	@Override public void processNPC() {
		super.processNPC();
		if (EncounterStage.STARTED.equals(crondisPuzzleEncounter.getStage())) {
			getHitBars().clear();
			getHitBars().add(PALM_HITBAR);
			getUpdateFlags().flag(UpdateFlag.HIT);
		}
	}

	@Override public boolean setHitpoints(int amount) {
		final boolean set = super.setHitpoints(amount);
		if (crondisPuzzleEncounter != null && id != CrondisPuzzleEncounter.PALM_NPC_ID + 4) {
			crondisPuzzleEncounter.getPlayers().forEach(p -> p.getHpHud().updateValue(getMaxHitpoints() - hitpoints));
			final int npcId = getNpcId();
			if (id != npcId) {
				crondisPuzzleEncounter.getPlayers().forEach(p -> p.sendSound(id > npcId ? PALM_TREE_DOWNGRADE_SOUND : PALM_TREE_UPGRADE_SOUND));
				setTransformation(npcId);
				if (npcId == CrondisPuzzleEncounter.PALM_NPC_ID + 4) {
					blockIncomingHits(999999);
					hitpoints = getMaxHitpoints();
					getHitBars().clear();
					getHitBars().add(new RemoveHitBar(hitBar.getType()));
					getUpdateFlags().flag(UpdateFlag.HIT);
					crondisPuzzleEncounter.completeRoom();
				}
			}
		}
		return set;
	}

	private int getNpcId() {
		for (int i = 0; i < 5; i++) {
			final float percentage = .25F + i * .25F;
			if (getMaxHitpoints() - hitpoints < getMaxHitpoints() * percentage) {
				return CrondisPuzzleEncounter.PALM_NPC_ID + i;
			}
		}
		return CrondisPuzzleEncounter.PALM_NPC_ID;
	}

	@Override public void sendDeath() {

	}

	@Override public void setRespawnTask() {

	}

	@Override public void setFaceEntity(Entity entity) {}

	@Override public void setFacedInteractableEntity(InteractableEntity facedInteractableEntity) {}

	@Override public void setFaceLocation(Location tile) {}

	@Override public boolean isCycleHealable() { return false; }

	@Override protected boolean preserveStatsOnTransformation() {
		return true;
	}

	@Override protected void updateTransformationalDefinitions() {
		final NPCCombatDefinitions def = NPCCombatDefinitions.clone(getId(), NPCCDLoader.get(getId()));
		def.setHitpoints(combatDefinitions.getHitpoints());
		def.setAttackSpeed(combatDefinitions.getAttackSpeed());
		def.getStatDefinitions().setCombatStats(combatDefinitions.getStatDefinitions().getCombatStats());
		setCombatDefinitions(def);
	}
}
