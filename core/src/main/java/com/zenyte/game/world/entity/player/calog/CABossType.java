package com.zenyte.game.world.entity.player.calog;

/**
 * @author Savions.
 */
public enum CABossType {

	ABYSSAL_SIRE(12917, 0, false),
	ALCHEMICAL_HYDRA(12926, 1, false),
	BARROWS(12893, 2, false),
	BRYOPHYTA(12925, 3, false),
	CALLISTO(12901, 4, false),
	CERBERUS(12916, 5, false),
	CHAOS_ELEMENTAL(12904, 6, false),
	CHAOS_FANATIC(12910, 7, false),
	CRAZY_ARCHEOLOGIST(12912, 10, false),
	COX(12891, 0, true),
	COX_CM(12892, 0, true),
	CORPOREAL_BEAST(12908, 9, false),
	COMMANDER_ZILYANA(12896, 8, false),
	DAGANNOTH_PRIME(12898, 11, false),
	DAGANNOTH_REX(12899, 11, false),
	DAGANNOTH_SUPREME(12900, 11, false),
	DERANGED_ARCHEOLOGIST(12922, 0, false),
	CRYSTALLINE_HUNLLEF(12931, 14, false),
	CORRUPTED_HUNLLEF(12932, 14, false),
	GENERAL_GRAARDOOR(12895, 15, false),
	GIANT_MOLE(12906, 16, false),
	GROTESQUE_GUARDIANS(12923, 17, false),
	HESPORI(12927, 18, false),
	KALPHITE_QUEEN(12907, 20, false),
	KING_BLACK_DRAGON(12905, 21, false),
	KRAKEN(12914, 22, false),
	KREE_ARRA(12894, 23, false),
	KRIL_TSUTSAROTH(12897, 24, false),
	THE_MIMIC(12928, 0, false),
	NEX(13171, 25, false),
	THE_NIGHTMARE(12934, 26, false),
	PHANTOM_MUSPAH(14712, 28, false),
	PHOSANI_NIGHTMARE(13002, 26, false),
	OBOR(12920, 27, false),
	SARACHNIS(12929, 29, false),
	SCORPIA(12911, 30, false),
	SKOTIZO(12918, 31, false),
	TEMPOROSS(12936, 32, false),
	TOB_ENTRY_MODE(12939, 1, true),
	TOB(12935, 1, true),
	TOB_HARD_MODE(12938, 1, true),
	THERMONUCLEAR_SMOKE_DEVIL(12915, 33, false),
	TOA_ENTRY_MODE(14297, 2, true),
	TOA(14298, 2, true),
	TOA_HARD_MODE(14299, 2, true),
	TZHAAR_KET_RAK_CHALLENGES(12937, 19, false),
	TZKAL_ZUK(12921, 19, false),
	TZTOK_JAD(12913, 13, false),
	VENENATIS(12902, 34, false),
	VETION(12903, 35, false),
	VORKATH(12924, 36, false),
	WINTERTODT(12919, 37, false),
	ZALCANO(12930, 38, false),
	ZULRAH(12909, 39, false),
	;

	public static final CABossType[] values = values();
	private final int taskVarBit;
	private final int collLogVal;
	private final boolean raid;

	CABossType(final int taskVarBit, int collLogVal, boolean raid) {
		this.taskVarBit = taskVarBit;
		this.collLogVal = collLogVal;
		this.raid = raid;
	}

	public int getTaskVarBit() {
		return taskVarBit;
	}

	public int getCollLogVal() {
		return collLogVal;
	}

	public boolean isRaid() { return raid; }
}
