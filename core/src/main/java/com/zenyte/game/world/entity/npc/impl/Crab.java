package com.zenyte.game.world.entity.npc.impl;

import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.game.content.achievementdiary.diaries.FremennikDiary;
import com.zenyte.game.content.achievementdiary.diaries.KourendDiary;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combatdefs.NPCCDLoader;
import com.zenyte.game.world.entity.npc.combatdefs.NPCCombatDefinitions;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

/**
 * @author Kris | 14. veebr 2018 : 0:29.28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public final class Crab extends NPC implements Spawnable {
	private static final Animation FALL_ANIM = new Animation(1314);
	private static final Animation RISE_ANIM = new Animation(1316);

	public Crab(final int id, final Location tile, final Direction facing, final int radius) {
		super(id, tile, facing, radius);
		setForceAggressive(true);
		aggressionDistance = 1;
	}

	@Override
	protected void updateCombatDefinitions() {
		NPCCombatDefinitions def = combatDefinitionsMap.get(isRocks() ? rocks2AliveMap.get(getId()) : getId());
		if (def == null) {
			final NPCCombatDefinitions cachedDefs = NPCCDLoader.get(isRocks() ? rocks2AliveMap.get(getId()) : getId());
			def = NPCCombatDefinitions.clone(getId(), cachedDefs);
		}
		if (combatDefinitionsMap.isEmpty()) {
			setHitpoints(def.getHitpoints());
		}
		setCombatDefinitions(def);
	}

	private long transformationDelay;

	private boolean isRocks() {
		return rocks2AliveMap.containsKey(getId());
	}

	@Override
	public void processNPC() {
		final boolean isRocks = isRocks();
		if (!isRocks && combat.process() || isLocked()) {
			return;
		}
		if (forceWalk != null) {
			if (getLocation().withinDistance(forceWalk, 5)) {
				forceWalk = null;
				resetWalkSteps();
				return;
			}
			if (getLocation().getPositionHash() == forceWalk.getPositionHash()) {
				forceWalk = null;
			} else if (getWalkSteps().size() == 0) {
				this.addWalkSteps(forceWalk.getX(), forceWalk.getY(), getSize(), true);
			}
			if (!hasWalkSteps()) {
				forceWalk = null;
			}
			return;
		}
		if (checkAggressivity()) {
			return;
		}
		if (isRocks || isDead()) {
			return;
		}
		final long currentTime = System.currentTimeMillis();
		final long currentTick = WorldThread.getCurrentCycle();
		if (currentTime > getFindTargetDelay() && currentTime > getAttackingDelay() && currentTick > getAttackedByDelay()) {
			transform(null);
		}
	}

	@Override
	public boolean checkAggressivity() {
		if (!forceAggressive) {
			if (!combatDefinitions.isAggressive() && !WildernessArea.isWithinWilderness(getX(), getY())) {
				return false;
			}
		}
		getPossibleTargets(targetType);
		if (!possibleTargets.isEmpty()) {
			final Entity target = possibleTargets.get(Utils.random(possibleTargets.size() - 1));
			setTarget(target);
			return true;
		}
		return false;
	}

	@Override
	public void setTarget(final Entity target, TargetSwitchCause cause) {
		transform(target);
	}

	private void transform() {
		transformationDelay = Utils.currentTimeMillis() + 3000;
		setTransformation(isRocks() ? rocks2AliveMap.get(getId()) : alive2RocksMap.get(getId()));
	}

	private void transform(final Entity target) {
		if (transformationDelay > Utils.currentTimeMillis()) return;
		if (target == null) {
			if (isRocks()) {
				return;
			}
			lock();
			resetWalkSteps();
			combat.reset();
			WorldTasksManager.schedule(() -> {
				unlock();
				setAnimation(FALL_ANIM);
				transform();
			}, 0);
			return;
		}
		if (!isRocks()) {
			combat.setTarget(target);
			return;
		}
		lock();
		transform();
		setAnimation(RISE_ANIM);
		final long currentTick = WorldThread.getCurrentCycle();
		setAttackedTick(currentTick);
		setAttackedByDelay(currentTick);
		target.setAttackedBy(target);
		target.setFindTargetDelay(Utils.currentTimeMillis() + 5000);
		WorldTasksManager.schedule(() -> {
			unlock();
			combat.setTarget(target);
		}, 1);
	}

	@Override
	public void onDeath(final Entity source) {
		super.onDeath(source);
		if (source instanceof Player) {
			final Player player = (Player) source;
			if (getName(player).contains("Sand")) {
				player.getAchievementDiaries().update(KourendDiary.KILL_A_SANDCRAB);
			} else {
				player.getAchievementDiaries().update(FremennikDiary.KILL_ROCK_CRABS);
			}
		}
	}

	@Override
	protected void onFinish(final Entity source) {
		super.onFinish(source);
		if (isRocks()) {
			return;
		}
		combat.reset();
		transform();
	}

	private static final Int2IntOpenHashMap alive2RocksMap = new Int2IntOpenHashMap();
	public static final Int2IntOpenHashMap rocks2AliveMap = new Int2IntOpenHashMap();

	static {
		alive2RocksMap.put(100, 101);
		alive2RocksMap.put(102, 103);
		alive2RocksMap.put(2261, 2262);
		alive2RocksMap.put(5935, 5936);
		alive2RocksMap.put(5940, 5941);
		alive2RocksMap.put(7206, 7207);
		alive2RocksMap.put(7266, 7267);
		alive2RocksMap.put(7799, 7800);
		alive2RocksMap.put(8297, 8299);
		for (final Int2IntMap.Entry entry : alive2RocksMap.int2IntEntrySet()) {
			rocks2AliveMap.put(entry.getIntValue(), entry.getIntKey());
		}
	}

	@Override
	public boolean validate(final int id, final String name) {
		return rocks2AliveMap.containsKey(id);
	}
}
