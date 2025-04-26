package com.zenyte.game.content.chambersofxeric;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 11. mai 2018 : 00:22:46
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public interface RoomController {

	/**
	 * A default method that's executed when the raid is officially started.
	 */
	default void loadRoom() {}

	/**
	 * A method to check whether the player can pass to the next room or not.
	 * 
	 * @param player
	 *            the player who clicked the entrance.
	 * @param object
	 *            the object that was being clicked.
	 * @return whether the player can pass or not.
	 */
	default boolean canPass(final Player player, final WorldObject object) {
		return true;
	}

    /**
     * Builds the points cap calculator.
     * @return the combat points cap calculator.
     */
    default CombatPointCapCalculator buildPointsCap() {
        return new CombatPointCapCalculator();
    }

}
