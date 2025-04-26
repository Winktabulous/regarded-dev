package com.zenyte.game.content.skills.magic;

/**
 * @author Kris | 7. juuli 2018 : 00:38:59
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum RuneContainer {
	
	/** Runes held in elemental staves are never actually consumed. */
	STAFF,
	
	/** Runes held in the player's rune pouch. */
	RUNE_POUCH,

	/** Runes held in the player's secondary rune pouch. */
	SECONDARY_RUNE_POUCH,
	
	/** Runes held in the player's inventory. */
	INVENTORY,
	
	/** Runes held in unique equipment pieces, such as the Bryophyta's staff and Tome of Fire. */
	EQUIPMENT,

	/**
	 * Blighted sacks, for specific spells
	 */
	BLIGHTED_SACKS

}
