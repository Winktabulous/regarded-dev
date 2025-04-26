package com.zenyte.game.world.entity;

import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 17. juuli 2018 : 23:30:03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class InvalidLocationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidLocationException(final Player player, final Location location) {
		super("Player " + player.getName() + "(" + player.getUsername() + ") attempted to teleport to an invalid location of " + location);
	}

}
