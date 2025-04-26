package com.zenyte.game.world.entity.player.calog;

import com.zenyte.game.item.ItemId;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Savions.
 */
public enum CATierType {

	EASY(CALog.CA_EASY_COMPLETION_TOTAL_VARBIT, ItemId.ANTIQUE_LAMP_25920, 20, 5_000, "Easy"),
	MEDIUM(CALog.CA_MEDIUM_COMPLETION_TOTAL_VARBIT, ItemId.ANTIQUE_LAMP_25921, 30, 10_000, "Medium"),
	HARD(CALog.CA_HARD_COMPLETION_TOTAL_VARBIT, ItemId.ANTIQUE_LAMP_25922, 40, 15_000, "Hard"),
	ELITE(CALog.CA_ELITE_COMPLETION_TOTAL_VARBIT, ItemId.ANTIQUE_LAMP_25923, 50, 25_000, "Elite"),
	MASTER(CALog.CA_MASTER_COMPLETION_TOTAL_VARBIT, ItemId.ANTIQUE_LAMP_25924, 60, 35_000, "Master"),
	GRANDMASTER(CALog.CA_GRANDMASTER_COMPLETION_TOTAL_VARBIT, ItemId.ANTIQUE_LAMP_25925, 70, 50_000, "Grandmaster");

	public static final CATierType[] values = values();

	private final int varBit;
	private final int lampId;
	private final int minimumLevel;
	private final int experience;
	private final String name;

	private static final Map<Integer, CATierType> TIER_TYPE_MAP = new HashMap<>();

	static {
		for (CATierType type : values) {
			TIER_TYPE_MAP.put(type.getLampId(), type);
		}
	}

	public static CATierType getTierByLamp(int lampId) {
		return TIER_TYPE_MAP.get(lampId);
	}

	CATierType(int varBit, int lampId, int minimumLevel, int experience, String name) {
		this.varBit = varBit;
		this.lampId = lampId;
		this.minimumLevel = minimumLevel;
		this.experience = experience;
		this.name = name;
	}

	public final int getVarBit() { return varBit; }

	public int getLampId() { return lampId; }

	public int getMinimumLevel() { return minimumLevel; }

	public int getExperience() { return experience; }

	public String getName() {
		return name;
	}

}