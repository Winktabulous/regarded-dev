package com.zenyte.game.world.region.dynamicregion;

/**
 * @author Kris | 29. juuli 2018 : 16:55:19
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class OutOfSpaceException extends Exception {

	private static final long serialVersionUID = -8022536853048240634L;

	public OutOfSpaceException(final String message) {
		super(message);
	}

}
