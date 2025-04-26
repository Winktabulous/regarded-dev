package com.zenyte.game.content.hiscores;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import com.zenyte.utils.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;

import static com.zenyte.game.content.hiscores.HiscoresCategoryEntry.*;

public enum HiscoresCategory {
	SKILLING(10323, (player, value) -> !player.getPrivilege().eligibleTo(PlayerPrivilege.ADMINISTRATOR) && value[0] >= 30L, OVERALL, ATTACK, STRENGTH, DEFENCE, HITPOINTS, RANGED, MAGIC, PRAYER, RUNECRAFTING, AGILITY, HERBLORE,
			THIEVING, CRAFTING, FLETCHING, SLAYER, MINING, SMITHING, FISHING, COOKING, FIREMAKING, WOODCUTTING, FARMING, HUNTER),
	BOSSES(10324, (player, value) -> !player.getPrivilege().eligibleTo(PlayerPrivilege.ADMINISTRATOR) && value[0] >= 25L,
			ABYSSAL_SIRE, ALCHEMICAL_HYDRA, ANGEL_OF_DEATH, BARROWS, BRYOPHYTA, CALLISTO, CERBERUS, CHAOS_ELEMENTAL, CHAOS_FANATIC, COMMANDER_ZILYANA,
			CORPOREAL_BEAST, CRAZY_ARCHAEOLOGIST, DAGANNOTH_PRIME, DAGANNOTH_REX, DAGANNOTH_SUPREME, GENERAL_GRAARDOR, GANODERMIC_BEAST, GIANT_MOLE,
			GROTESQUE_GUARDIANS, HESPORI, KRIL_TSUTSAROTH, KING_BLACK_DRAGON, KRAKEN, KREEARRA, MIMIC, NEX, NIGHTMARE, PHOSANIS_NIGHTMARE, OBOR, RISE_OF_THE_SIX, SARACHNIS, SCORPIA, SKOTIZO,
			THE_GAUNTLET, THE_CORRUPTED_GAUNTLET, THERMONUCLEAR_SMOKE_DEVIL, TZKAL_ZUK, TZTOK_JAD, VANSTROM_KLAUSE, VENENATIS, VETION, VORKATH,
			ZALCANO, WINTERTODT, ZULRAH),
	RAIDS(10325, (player, value) -> !player.getPrivilege().eligibleTo(PlayerPrivilege.ADMINISTRATOR) && value[0] >= 10L,CHAMBERS_OF_XERIC, CHAMBERS_OF_XERIC_CM),
	PVP(10326, (player, value) -> !player.getPrivilege().eligibleTo(PlayerPrivilege.ADMINISTRATOR) && value[0] > 0L, WILDERNESS),
	;

	public static final HiscoresCategory[] values = values();
	public static final Map<Integer, HiscoresCategory> idToCategory = new HashMap<>();
	private final int structId;
	private final HiscoresCategoryEntry[] entries;
	private final BiPredicate<Player, long[]> applies;

	HiscoresCategory(int structId, BiPredicate<Player, long[]> applies, HiscoresCategoryEntry... entries) {
		this.structId = structId;
		this.applies = applies;
		this.entries = entries;
	}

	public int getStructId() {
		return structId;
	}

	public HiscoresCategoryEntry[] getEntries() {
		return entries;
	}

	public Map<HiscoresCategoryEntry, HiscoresScore> collectData(Player player) {
		Map<HiscoresCategoryEntry, HiscoresScore> data = new HashMap<>();
		for (HiscoresCategoryEntry entry : entries) {
			long[] values = entry.values(player);
			if (applies.test(player, values)) {
				data.put(entry, new HiscoresScore(TextUtils.formatName(player.getUsername()), values));
			}
		}

		return data;
	}

	public BiPredicate<Player, long[]> getApplies() {
		return applies;
	}

	static {
		for (HiscoresCategory category : values) {
			idToCategory.put(category.structId, category);
		}
	}

}
