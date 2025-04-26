package com.zenyte.game.content.skills.agility;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * An agility object that can be failed.
 */
public interface Failable {
	
	/**
	 * Defines failure behaviour of an agility object.
	 * @param player the player interacting with the agility object
	 * @param object the object the player is interacting with
	 */
    void startFail(final Player player, final WorldObject object);
	default void endFail(final Player player, final WorldObject object) {}
	double getFailXp(final WorldObject object);

}
