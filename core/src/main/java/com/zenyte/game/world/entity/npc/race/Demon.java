package com.zenyte.game.world.entity.npc.race;

import com.zenyte.game.world.entity.npc.NPC;

import java.util.Arrays;

/**
 * @author Kris | 2. jaan 2018 : 23:15.09
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class Demon {
	private static final String[] ALL_DEMONS = new String[] {"Imp", "Demon", "Hellhound", "Bloodveld", "Nechryael", "Death spawn", "Fiend", "Abyssal sire", "K'ril tsutsaroth", "Balfruh kreeyath", "Tstanon karlak", "Zakl'n gritch", "Skotizo", "Tormented demon"};
	private static final String[] SELECTED_DEMONS = new String[] {"Imp", "Demon", "Nechryael", "Death spawn", "K'ril tsutsaroth", "Balfruh kreeyath", "Tstanon karlak", "Zakl'n gritch", "Skotizo", "Tormented demon"};
	private static final String[] GREATER_DEMONS = new String[] { "Greater Demon", "Nechryael",  "K'ril tsutsaroth", "Balfruh kreeyath", "Tstanon karlak", "Zakl'n gritch", "Skotizo", "Tormented demon"};

	static {
        Arrays.setAll(ALL_DEMONS, i -> ALL_DEMONS[i].toLowerCase());
        Arrays.setAll(SELECTED_DEMONS, i -> SELECTED_DEMONS[i].toLowerCase());
        Arrays.setAll(GREATER_DEMONS, i -> GREATER_DEMONS[i].toLowerCase());
	}

	/**
	 * Checks whether the NPC is of the demon race or not.
	 * @param npc NPC who to check.
	 * @param checkAll whether it checks all the demons listed or 
	 * only checks the demons that are weak to demon-specific items,
	 * such as darklight.
	 * @return whether the NPC is a demon.
	 */
	public static boolean isDemon(final NPC npc, final boolean checkAll) {
		return isDemon(npc, checkAll ? ALL_DEMONS : SELECTED_DEMONS);
	}

	public static boolean isGreaterDemon(final NPC npc) {
		return isDemon(npc, GREATER_DEMONS);
	}

	private static boolean isDemon(final NPC npc, String[] demonList) {
		if (npc == null) return false;
		final String name = npc.getDefinitions().getName();
		if (name == null) return false;
		final String lowerCaseName = name.toLowerCase();
        return Arrays.stream(demonList).anyMatch(lowerCaseName::contains);
	}
}
