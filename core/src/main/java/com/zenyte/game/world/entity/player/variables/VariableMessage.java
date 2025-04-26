package com.zenyte.game.world.entity.player.variables;

/**
 * @author Kris | 5. juuni 2018 : 02:02:45
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class VariableMessage {

	public VariableMessage(final int onTick, final String message) {
		this.onTick = onTick;
		this.message = message;
	}
	
	final int onTick;
	final String message;
	
}
