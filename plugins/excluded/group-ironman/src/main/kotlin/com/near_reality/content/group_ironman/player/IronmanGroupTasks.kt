package com.near_reality.content.group_ironman.player

import com.zenyte.game.item.ItemId.*
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants.*
import com.zenyte.game.content.achievementdiary.DiaryComplexity;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot.*
import mgi.types.config.StructDefinitions
import mgi.types.config.enums.Enums

enum class IronmanGroupTasks(val enumIndex: Int, val taskCompleted: (Player) -> Boolean) {

	EQUIP_RUNE_PLATE(0, { player -> player.equipment.containsAnyOf(PLATE.slot, RUNE_PLATEBODY) }),
	EQUIP_STRONGHOLD_BOOTS(1, { player -> player.equipment.containsAnyOf(BOOTS.slot, FANCY_BOOTS, FIGHTING_BOOTS) }),
	GATHERING_99(2, { player ->
		player.skills.getLevelForXp(MINING) == 99 || player.skills.getLevelForXp(FISHING) == 99 || player.skills.getLevelForXp(WOODCUTTING) == 99 ||
				player.skills.getLevelForXp(HUNTER) == 99 || player.skills.getLevelForXp(FARMING) == 99
	}),
	ARTISAN_99(3, { player ->
		player.skills.getLevelForXp(COOKING) == 99 || player.skills.getLevelForXp(SMITHING) == 99 || player.skills.getLevelForXp(FLETCHING) == 99 ||
				player.skills.getLevelForXp(FIREMAKING) == 99 || player.skills.getLevelForXp(HERBLORE) == 99 || player.skills.getLevelForXp(CRAFTING) == 99 ||
				player.skills.getLevelForXp(RUNECRAFTING) == 99 || player.skills.getLevelForXp(CONSTRUCTION) == 99
	}),
	SURVIVAL_99(4, { player ->
		player.skills.getLevelForXp(AGILITY) == 99 ||player.skills.getLevelForXp(THIEVING) == 99 || player.skills.getLevelForXp(SLAYER) == 99
	}),
	COMBAT_99(5, { player ->
		player.skills.getLevelForXp(ATTACK) == 99 ||player.skills.getLevelForXp(
			STRENGTH) == 99 || player.skills.getLevelForXp(DEFENCE) == 99
				|| player.skills.getLevelForXp(MAGIC) == 99 || player.skills.getLevelForXp(RANGED) == 99
				|| player.skills.getLevelForXp(PRAYER) == 99  || player.skills.getLevelForXp(HITPOINTS) == 99
	}),
	ATTACK_99(6,  { player -> player.skills.getLevelForXp(ATTACK) == 99 }),
	STRENGTH_99(7,  { player -> player.skills.getLevelForXp(STRENGTH) == 99 }),
	DEFENCE_99(8,  { player -> player.skills.getLevelForXp(DEFENCE) == 99 }),
	RANGED_99(9,  { player -> player.skills.getLevelForXp(RANGED) == 99 }),
	PRAYER_99(10,  { player -> player.skills.getLevelForXp(PRAYER) == 99 }),
	MAGIC_99(11,  { player -> player.skills.getLevelForXp(MAGIC) == 99 }),
	RUNECRAFT_99(12,  { player -> player.skills.getLevelForXp(RUNECRAFTING) == 99 }),
	HUNTER_99(13,  { player -> player.skills.getLevelForXp(HUNTER) == 99 }),
	HITPOINTS_99(14,  { player -> player.skills.getLevelForXp(HITPOINTS) == 99 }),
	AGILITY_99(15,  { player -> player.skills.getLevelForXp(AGILITY) == 99 }),
	HERBLORE_99(16,  { player -> player.skills.getLevelForXp(HERBLORE) == 99 }),
	THIEVING_99(17,  { player -> player.skills.getLevelForXp(THIEVING) == 99 }),
	CRAFTING_99(18,  { player -> player.skills.getLevelForXp(CRAFTING) == 99 }),
	FLETCHING_99(19,  { player -> player.skills.getLevelForXp(FLETCHING) == 99 }),
	SLAYER_99(20,  { player -> player.skills.getLevelForXp(SLAYER) == 99 }),
	CONSTRUCTION_99(21,  { player -> player.skills.getLevelForXp(CONSTRUCTION) == 99 }),
	MINING_99(22,  { player -> player.skills.getLevelForXp(MINING) == 99 }),
	SMITHING_99(23,  { player -> player.skills.getLevelForXp(SMITHING) == 99 }),
	FISHING_99(24,  { player -> player.skills.getLevelForXp(FISHING) == 99 }),
	COOKING_99(25,  { player -> player.skills.getLevelForXp(COOKING) == 99 }),
	FIREMAKING_99(26,  { player -> player.skills.getLevelForXp(FIREMAKING) == 99 }),
	WOODCUTTING_99(27,  { player -> player.skills.getLevelForXp(WOODCUTTING) == 99 }),
	FARMING_99(28,  { player -> player.skills.getLevelForXp(FARMING) == 99 }),
	QP_50(29,  { true }),
	QP_100(30,  { true }),
	QP_150(31,  { true }),
	QP_200(32,  { true }),
	CMB_LVL_50(33, { player -> player.skills.combatLevel >= 50 }),
	CMB_LVL_100(34, { player -> player.skills.combatLevel >= 100 }),
	CMB_LVL_126(35, { player -> player.skills.combatLevel >= 126 }),
	EASY_TASKS(36, { player -> player.achievementDiaries.isAllCompleted(DiaryComplexity.EASY) }),
	MEDIUM_TASKS(37, { player -> player.achievementDiaries.isAllCompleted(DiaryComplexity.MEDIUM) }),
	HARD_TASKS(38, { player -> player.achievementDiaries.isAllCompleted(DiaryComplexity.HARD) }),
	ELITE_TASKS(39, { player -> player.achievementDiaries.isAllCompleted(DiaryComplexity.ELITE) }),
	EASY_COMBAT_TASKS(40, { true }),
	MEDIUM_COMBAT_TASKS(41, { true }),
	HARD_COMBAT_TASKS(42, { true }),
	ELITE_COMBAT_TASKS(43, { true }),
	TOTAL_LEVEL_500(44, { player -> player.skills.totalLevel >= 500 }),
	TOTAL_LEVEL_750(45, { player -> player.skills.totalLevel >= 750 }),
	TOTAL_LEVEL_1000(46, { player -> player.skills.totalLevel >= 1000 }),
	TOTAL_LEVEL_1500(47, { player -> player.skills.totalLevel >= 1500 }),
	TOTAL_LEVEL_2000(48, { player -> player.skills.totalLevel >= 2000 }),
	;

	fun extraSpaces(): Int {
		return StructDefinitions.get(Enums.GIM_STORAGE_REQS.getValue(enumIndex).orElseThrow(Enums.exception())).getParamAsInt(1547)
	}

	companion object {
		val VALUES = values()
	}

}