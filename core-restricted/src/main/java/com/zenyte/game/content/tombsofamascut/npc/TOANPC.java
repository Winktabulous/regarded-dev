package com.zenyte.game.content.tombsofamascut.npc;

import com.zenyte.game.content.tombsofamascut.raid.TOARaidArea;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.combatdefs.NPCCDLoader;
import com.zenyte.game.world.entity.npc.combatdefs.NPCCombatDefinitions;
import com.zenyte.game.world.entity.npc.combatdefs.StatType;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Savions.
 */
public abstract class TOANPC extends AbstractTOANPC {

	protected TOARaidArea toaRaidArea;
	private float raidFactor = 1.0F;
	private float damageFactor = 1.0F;
	private float accuracyFactor = 1.0F;
	private float levelFactor;
	protected int bossLevel;

	public TOANPC(int id, Location tile, Direction facing, int radius, TOARaidArea raidArea) {
		this(id, tile, facing, radius, raidArea, 0);
	}

	public TOANPC(int id, Location tile, Direction facing, int radius, TOARaidArea raidArea, int bossLevel) {
		this(id, tile, facing, radius, raidArea, bossLevel, true);
	}

	public TOANPC(int id, Location tile, Direction facing, int radius, TOARaidArea raidArea, int bossLevel, boolean scaleStats) {
		super(id, tile, facing, radius);
		this.toaRaidArea = raidArea;
		this.bossLevel = bossLevel;
		if (scaleStats) {
			levelFactor = bossLevel > 0 ? .08F + ((bossLevel - 1) * .05F) : 0;
			raidFactor = 1F + ((raidArea.getParty().getPartySettings().getRaidLevel() / 5F) * .02F);
			damageFactor = Math.min(2.5F, (raidFactor + levelFactor));
			accuracyFactor = raidFactor;
			combatDefinitions.getStatDefinitions().set(StatType.DEFENCE, (int) Math.floor(combatDefinitions.getStatDefinitions().get(StatType.DEFENCE) * raidFactor));
		}
		setMaxHealth();
	}

	public void setMaxHealth() {
		final float teamSizeFactor = 1F + (toaRaidArea.getStartTeamSize() - 1) * .9F;
		final float hpFactor = raidFactor * teamSizeFactor * (1F + levelFactor);
		int maxHitPoints = (int) Math.floor(combatDefinitions.getHitpoints() * hpFactor);
		combatDefinitions.setHitpoints(maxHitPoints);
		setHitpoints(maxHitPoints);
		setCombatLevelChange((int) (getCombatLevel() * (1F + (Math.max(0, hpFactor - 1F) / 4))));
	}

	@Override public void processNPC() {
		if (getCombat().getTarget() != null && getCombat().getTarget() instanceof final Player player && player.getAppearance().isTransformedIntoNpc()) {
			getCombat().setTarget(null);
		}
		super.processNPC();
	}


	public abstract float getPointMultiplier();

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

	@Override public float getAccuracyMultiplier() { return accuracyFactor; }

	public final int getMaxHit(int base) {
		return (int) Math.floor(base * damageFactor);
	}

	@Override public boolean isCycleHealable() { return false; }

	@Override public void setRespawnTask() {}
}