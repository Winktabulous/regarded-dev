package com.zenyte.game.world.entity.pathfinding.events.player;

import com.zenyte.game.world.entity.pathfinding.events.RouteEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 14. juuli 2018 : 04:14:34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class TileEvent extends RouteEvent<Player, TileStrategy> {
	
	public TileEvent(final Player entity, final TileStrategy strategy, final Runnable event) {
		super(entity, strategy, event, 0);
	}

	public TileEvent(final Player entity, final TileStrategy strategy, final Runnable event, final int delay) {
		super(entity, strategy, event, delay);
	}

	@Override
	protected void resetFlag() {
		entity.getPacketDispatcher().resetMapFlag();
	}

	@Override
	protected void inform(final String message) {
		entity.sendMessage(message);
	}

}
