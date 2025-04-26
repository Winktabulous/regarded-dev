package com.zenyte.game.world.region.dynamicregion;

/**
 * @author Kris | 29. juuli 2018 : 23:59:39
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class OutOfBoundaryException extends Exception {

	private static final long serialVersionUID = 6159322408098293331L;

	public OutOfBoundaryException(final String message) {
		super(message);
	}
	
}
