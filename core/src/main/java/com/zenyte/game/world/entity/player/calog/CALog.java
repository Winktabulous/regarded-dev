package com.zenyte.game.world.entity.player.calog;

import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.VarManager;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import mgi.types.config.StructDefinitions;
import mgi.types.config.enums.EnumDefinitions;
import mgi.types.config.enums.IntEnum;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Savions.
 */
public class CALog {

	public static final int CA_EASY_COMPLETION_TOTAL_VARBIT = 12885;
	public static final int CA_MEDIUM_COMPLETION_TOTAL_VARBIT = 12886;
	public static final int CA_HARD_COMPLETION_TOTAL_VARBIT = 12887;
	public static final int CA_ELITE_COMPLETION_TOTAL_VARBIT = 12888;
	public static final int CA_MASTER_COMPLETION_TOTAL_VARBIT = 12889;
	public static final int CA_GRANDMASTER_COMPLETION_TOTAL_VARBIT = 12890;

	private static final int[] CA_TASK_VARS = { 3116, 3117, 3118, 3119, 3120, 3121, 3122, 3123, 3124, 3125, 3126, 3127, 3128, 3387, 3718, 3773, 3774 };
	private static final int[] CA_TIER_REWARD_STATUS_VARBITS = { 12863, 12864, 12865, 12866, 12867, 12868 };
	private static final int[] CA_TASK_TIER_ENUMS = { 3981, 3982, 3983, 3984, 3985, 3986 };

	private static final int STRUCT_POINTER_TASK_INDX = 1306;
	private static final int STRUCT_POINTER_TASK_NAME = 1308;
	private static final int STRUCT_POINTER_TASK_TIER = 1310;
	private static final int STRUCT_POINTER_TASK_BOSS = 1312;

	private static final int[] TOTAL_TASK_COUNT = new int[CATierType.values().length];

	static {
		VarManager.appendPersistentVarbit(CA_EASY_COMPLETION_TOTAL_VARBIT);
		VarManager.appendPersistentVarbit(CA_MEDIUM_COMPLETION_TOTAL_VARBIT);
		VarManager.appendPersistentVarbit(CA_HARD_COMPLETION_TOTAL_VARBIT);
		VarManager.appendPersistentVarbit(CA_ELITE_COMPLETION_TOTAL_VARBIT);
		VarManager.appendPersistentVarbit(CA_MASTER_COMPLETION_TOTAL_VARBIT);
		VarManager.appendPersistentVarbit(CA_GRANDMASTER_COMPLETION_TOTAL_VARBIT);
		for (int varpBit : CA_TIER_REWARD_STATUS_VARBITS) {
			VarManager.appendPersistentVarbit(varpBit);
		}
		for (int varp : CA_TASK_VARS) {
			VarManager.appendPersistentVarp(varp);
		}

		for (int enumIndx = 0; enumIndx < CA_TASK_TIER_ENUMS.length; enumIndx++) {
			final IntEnum categoryEnum = EnumDefinitions.getIntEnum(CA_TASK_TIER_ENUMS[enumIndx]);
			final ObjectSet<Int2IntMap.Entry> entrySet = categoryEnum.getValues().int2IntEntrySet();
			TOTAL_TASK_COUNT[enumIndx] = entrySet.size();

			/*
			for (final Int2IntMap.Entry entry : entrySet) {
				final int subCategoryStructId = entry.getIntValue();
				final StructDefinitions subCategoryStruct = Objects.requireNonNull(StructDefinitions.get(subCategoryStructId));
				final int taskIndx = Integer.parseInt(subCategoryStruct.getValue(STRUCT_POINTER_TASK_INDX).orElseThrow(RuntimeException::new).toString());
				final String taskName = String.valueOf(subCategoryStruct.getValue(STRUCT_POINTER_TASK_NAME).orElseThrow(RuntimeException::new).toString());
				final int taskTier = Integer.parseInt(subCategoryStruct.getValue(STRUCT_POINTER_TASK_TIER).orElseThrow(RuntimeException::new).toString());
				final int boss = Integer.parseInt(subCategoryStruct.getValue(STRUCT_POINTER_TASK_BOSS).orElseThrow(RuntimeException::new).toString());
			}*/
		}
	}

	private final transient Player player;

	//current flag set used for multi-stage tasks, doesn't save on logout and reset on other task started/completed
	private final transient Int2IntMap currentTaskFlagMap = new Int2IntOpenHashMap();

	public CALog(final Player player) {
		this.player = player;
	}

	public void complete(@NotNull CAType type) {
		if (type.isDisabled()) return;
		final int varId = CA_TASK_VARS[type.getVarIndex() >> 5];
		final int varValue = player.getVarManager().getValue(varId);
		final int bit = 1 << (type.getVarIndex() & 31);
		if ((varValue & bit) != 0) {
			return;
		}
		final CATierType tier = type.getTier();
		final String tierName = tier.name().toLowerCase();
		player.sendMessage("Congratulations! You've completed " + Utils.getAOrAn(tierName) + " " + tierName + " combat task: <col=40ff00>" + type.getName());
		player.notification("Combat Task Completed!", "Task Completed: " + Colour.WHITE.wrap(type.getName()), 16750623);
		player.getVarManager().sendVar(varId, varValue | bit);
		final int tasksOfTierCompleted = player.getVarManager().getBitValue(tier.getVarBit()) + 1;
		player.getVarManager().sendBit(tier.getVarBit(), tasksOfTierCompleted);
		final Optional<CABossType> optionalBossType = Stream.of(CABossType.values).filter(b -> b.ordinal() == type.getBossNumber() - 1).findFirst();
		if (optionalBossType.isPresent()) {
			final CABossType bossType = optionalBossType.get();
			player.getVarManager().sendBit(bossType.getTaskVarBit(), player.getVarManager().getBitValue(bossType.getTaskVarBit()) + 1);
		}
		if (tasksOfTierCompleted >= TOTAL_TASK_COUNT[tier.ordinal()]) {
			// This should be set to 2, to signify a completed task
			// Setting to 1, is a partial complete, or "started" / "progressed"
			player.getVarManager().sendBit(CA_TIER_REWARD_STATUS_VARBITS[tier.ordinal()], 2); // OLD: 1
			//TODO dialogue
			player.sendMessage("Congratulations! You have finished all the combat achievement of tier: " + tier.name().toLowerCase());
		}
	}

	public void completeSilent(@NotNull CAType type) {
		final int varId = CA_TASK_VARS[type.getVarIndex() >> 5];
		final int varValue = player.getVarManager().getValue(varId);
		final int bit = 1 << (type.getVarIndex() & 31);
		if ((varValue & bit) != 0) {
			return;
		}
		final CATierType tier = type.getTier();
		player.getVarManager().sendVar(varId, varValue | bit);
		final int tasksOfTierCompleted = player.getVarManager().getBitValue(tier.getVarBit()) + 1;
		player.getVarManager().sendBit(tier.getVarBit(), tasksOfTierCompleted);
		final Optional<CABossType> optionalBossType = Stream.of(CABossType.values).filter(b -> b.ordinal() == type.getBossNumber() - 1).findFirst();
		if (optionalBossType.isPresent()) {
			final CABossType bossType = optionalBossType.get();
			player.getVarManager().sendBit(bossType.getTaskVarBit(), player.getVarManager().getBitValue(bossType.getTaskVarBit()) + 1);
		}
		if (tasksOfTierCompleted >= TOTAL_TASK_COUNT[tier.ordinal()])
			setTierCompleted(tier);
	}

	public boolean taskCompleted(@NotNull CAType type) {
		if (type.isDisabled()) {
			return true;
		}

		final int varId = CA_TASK_VARS[type.getVarIndex() >> 5];
		final int varValue = player.getVarManager().getValue(varId);
		final int bit = 1 << (type.getVarIndex() & 31);
		return (varValue & bit) != 0;
	}

	public void resetTask(int varIndex) {
		//temporary
		player.getVarManager().sendVar(CA_TASK_VARS[varIndex >> 5], varIndex & ~(1 << (varIndex & 31)));
		player.sendMessage("Reset task: " + Arrays.stream(CAType.values).filter(t -> t.getVarIndex() == varIndex).findFirst().get().toString().toLowerCase());
	}

	public void checkKcTask(String name, int kcRequired, CAType task) {
		if (player.getNotificationSettings().getKillcount(name) >= kcRequired) {
			complete(task);
		}
	}

	public boolean hasTierCompleted(CATierType tier) {
		return player.getVarManager().getBitValue(CA_TIER_REWARD_STATUS_VARBITS[tier.ordinal()]) == 2;
	}

	public boolean hasAllTiersCompleted() {
		for (CATierType caTierType : CATierType.values) {
			if (!hasTierCompleted(caTierType)) {
				return false;
			}
		}

		return true;
	}

	public boolean completedPreviousTiers(CATierType tier) {
		for (int indx = 0; indx < tier.ordinal(); indx++) {
			if (!hasTierCompleted(CATierType.values[indx])) {
				return false;
			}
		}

		return true;
	}

	public boolean isEligibleForRewards(CATierType tier) {
		return player.getVarManager().getBitValue(CA_TIER_REWARD_STATUS_VARBITS[tier.ordinal()]) == 1;
	}

	public void setTierCompleted(CATierType tier) {
		player.getVarManager().sendBit(CA_TIER_REWARD_STATUS_VARBITS[tier.ordinal()], 2);
	}

	public void setCurrentTaskValue(CAType task, int value) {
		currentTaskFlagMap.put(task.ordinal(), value);
	}

	public void addCurrentTaskFlag(CAType task, int flag) {
		final int key = task.ordinal();
		final int currentFlag = currentTaskFlagMap.getOrDefault(key, 0);
		currentTaskFlagMap.put(key, currentFlag | flag);
	}

	public void removeCurrentTaskFlag(CAType task, int flag) {
		final int key = task.ordinal();
		final int currentFlag = currentTaskFlagMap.getOrDefault(key, 0);
		currentTaskFlagMap.put(key, currentFlag & ~flag);
	}

	public void removeCurrentTask(CAType task) {
		currentTaskFlagMap.remove(task.ordinal());
	}

	public int getCurrentTaskValue(CAType task) {
		return currentTaskFlagMap.getOrDefault(task.ordinal(), 0);
	}

	public boolean hasCurrentTaskFlags(CAType task, int... flags) {
		final int currentFlag = currentTaskFlagMap.getOrDefault(task.ordinal(), 0);
		for (int flag : flags) {
			if ((currentFlag  & flag) == 0) {
				return false;
			}
		}
		return true;
	}

	public void resetCurrentTaskFlagSet(CAType task) {
		currentTaskFlagMap.put(task.ordinal(), 0);
	}

}