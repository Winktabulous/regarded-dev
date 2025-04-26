package com.zenyte.game.content.skills.magic.resources;

import com.zenyte.game.content.skills.magic.RuneContainer;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 7. juuli 2018 : 00:57:06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public interface RuneResource {

	/** The type of the container. */
    RuneContainer getContainer();
	
	/** Gets the amount of requested runes that can be obtained from this given rune resource. */
    int getAmountOf(final Player player, final int runeId, final int amountRequired);
	
	/** Consumes the previously obtained {@link #getAmountOf(Player, int, int)} amount of requested runes from this resource. */
    void consume(final Player player, final int runeId, final int amount);
	
	/** An array containing the ids of the runes affected by this resource. If null, affects all game runes. */
    int[] getAffectedRunes();
	
}
