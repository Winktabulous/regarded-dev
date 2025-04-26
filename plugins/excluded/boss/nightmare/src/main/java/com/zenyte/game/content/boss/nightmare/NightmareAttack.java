package com.zenyte.game.content.boss.nightmare;

import java.util.Arrays;
import java.util.List;

public enum NightmareAttack {
	FLOWERS,
	CURSE,
	CHARGE,
	SPORES,
	HUSKS,
	PARASITE,
	MULTI_CLAWS_5,
	MULTI_CLAWS_6;

	public static final NightmareAttack[] SPAWNS = {HUSKS, PARASITE};
	public static final List<NightmareAttack> SPECIALS = Arrays.asList(FLOWERS, CURSE, SPORES, CHARGE);

}
