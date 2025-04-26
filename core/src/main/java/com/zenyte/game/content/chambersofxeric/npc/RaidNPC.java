package com.zenyte.game.content.chambersofxeric.npc;

import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.chambersofxeric.map.RaidArea;
import com.zenyte.game.content.consumables.Consumable;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.CollisionUtil;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combatdefs.*;
import com.zenyte.game.world.entity.player.Player;

import java.util.EnumSet;

/**
 * @author Kris | 13. mai 2018 : 18:49:43
 * @param <T> type of the room in which the monster resides.
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public abstract class RaidNPC<T extends RaidArea> extends NPC {
	public RaidNPC(final Raid raid, final T room, final int id, final Location tile) {
		super(id, tile, Direction.SOUTH, 5);
		this.maxDistance = 32;
		this.raid = raid;
		this.room = room;
		final int size = raid.getOriginalPlayers().size();
		final int combatHitpointsOffset = Math.max(59, Math.min(raid.getCombatLevel(), 126));
		final float combatHitpointsMultiplier = combatHitpointsOffset / 126.0F;
		final int combatOffset = Math.max(75, Math.min(raid.getCombatLevel(), 126));
		final float combatMultiplier = combatOffset / 126.0F;
		aggressiveLevelMultiplier = (size == 1 ? 1 : (1.0 + (0.07 * (1.0 + (Math.floor(size / 5.0))) + (0.01 * (size - 1.0))))) * combatMultiplier;
		defenceMultiplier = (1 + (0.01 * (size - 1))) * combatMultiplier;
		hitpointsMultiplier = (1 + (Math.floor(size / 2.0))) * combatHitpointsMultiplier;
	}

	@Override
	protected void updateCombatDefinitions() {
		super.updateCombatDefinitions();
		if (raid != null) {
			setStats();
			if (isToxinImmune()) {
				this.combatDefinitions.setImmunityTypes(EnumSet.allOf(ImmunityType.class));
			}
		}
	}

	protected boolean isToxinImmune() {
		return true;
	}

	public boolean grantPoints() {
		return true;
	}

	@Override
	public void setTransformation(final int id) {
		nextTransformation = id;
		setId(id);
		size = definitions.getSize();
		updateFlags.flag(UpdateFlag.TRANSFORMATION);
	}

	double aggressiveLevelMultiplier;
	double defenceMultiplier;
	protected double hitpointsMultiplier;
	StatType[] aggressiveStats = new StatType[] {StatType.ATTACK, StatType.STRENGTH, StatType.MAGIC, StatType.RANGED};

	public float getPointsMultiplier(final Hit hit) {
		return getXpModifier(hit);
	}

	protected void setStats() {
		final NPCCombatDefinitions cachedDefs = NPCCombatDefinitions.clone(getId(), NPCCDLoader.get(getId()));
		final double challengeModeMultiplier = raid.isChallengeMode() ? 1.1 : 0.8;
		final StatDefinitions statDefinitions = cachedDefs.getStatDefinitions();
		for (final StatType aggressiveStat : aggressiveStats) {
			statDefinitions.set(aggressiveStat, Math.max(1, (int) Math.floor(statDefinitions.get(aggressiveStat) * aggressiveLevelMultiplier * challengeModeMultiplier)));
		}
		statDefinitions.set(StatType.DEFENCE, Math.max(1, (int) Math.floor(statDefinitions.get(StatType.DEFENCE) * defenceMultiplier * challengeModeMultiplier)));
		setCombatDefinitions(cachedDefs);
		combatDefinitions.setHitpoints(Math.max(1, (int) Math.floor(cachedDefs.getHitpoints() * hitpointsMultiplier * challengeModeMultiplier)));
		setHitpoints(combatDefinitions.getHitpoints());
	}

	protected final Raid raid;
	protected final T room;

	protected int getMaxHit(final int base) {
		final double challengeModeMultiplier = raid.isChallengeMode() ? 1.5 : 1.0;
		return (int) Math.floor(base * aggressiveLevelMultiplier * challengeModeMultiplier);
	}

	void announce(final Player killer) {
		raid.sendGlobalMessage("As the " + getDefinitions().getName() + " is slain, supplies are dropped for " + killer.getName() + ".");
	}

	@Override
	public boolean isPotentialTarget(final Entity entity) {
		final int entityX = entity.getX();
		final int entityY = entity.getY();
		final int entitySize = entity.getSize();
		final int x = getX();
		final int y = getY();
		final int size = getSize();
		final long currentTime = Utils.currentTimeMillis();
		final long currentTick = WorldThread.getCurrentCycle();
		return !entity.isMaximumTolerance() && (entity.isMultiArea() || entity.getAttackedBy() == this || (entity.getAttackedByDelay() <= currentTick && entity.getFindTargetDelay() <= currentTime)) && (!isProjectileClipped(entity, combatDefinitions.isMelee()) || CollisionUtil.collides(x, y, size, entityX, entityY, entitySize)) && (forceAggressive || combatDefinitions.isAlwaysAggressive() || entity instanceof NPC && ((NPC) entity).getDefinitions().containsOption("Attack")) && isAcceptableTarget(entity) && (!(entity instanceof Player) || !isTolerable() || !((Player) entity).isTolerant(getLocation()));
	}

	@Override
	protected void spawnDrop(final Item item, final Location tile, final Player killer) {
		final int id = item.getId();
		final boolean qualifies = id == ItemId.NOXIFER_SEED || id == ItemId.BUCHU_SEED || id == ItemId.GOLPAR_SEED || Consumable.gourdDrinks.containsKey(id);
		if (killer.isIronman() && qualifies) {
			World.spawnFloorItem(item, tile, -1, null, null, invisibleDropTicks(), visibleDropTicks(), true, false);
			return;
		}
		World.spawnFloorItem(item, tile, killer, invisibleDropTicks(), visibleDropTicks());
	}

	@Override
	public boolean isEntityClipped() {
		return false;
	}

	@Override
	public boolean isTolerable() {
		return false;
	}

	@Override
	public void setRespawnTask() {
	}

	public Raid getRaid() {
		return raid;
	}

	public T getRoom() {
		return room;
	}
}
