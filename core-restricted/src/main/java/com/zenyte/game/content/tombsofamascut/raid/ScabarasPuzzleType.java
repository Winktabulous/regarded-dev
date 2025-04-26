package com.zenyte.game.content.tombsofamascut.raid;

import com.zenyte.game.content.tombsofamascut.encounter.ScabarasEncounter;

import java.util.List;

/**
 * @author Savions.
 */
public enum ScabarasPuzzleType {
	LIGHT, SUM, PILLAR, MEMORY;

	public static final ScabarasPuzzleType[] values = values();
	public static final List<ScabarasPuzzleType> list = List.of(values);
}
