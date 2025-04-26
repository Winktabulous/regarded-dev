package com.zenyte.game.world.entity.pathfinding.events.npc;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.pathfinding.events.RouteEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;

/**
 * @author Kris | 14. juuli 2018 : 15:13:25
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class NPCTileEvent extends RouteEvent<NPC, TileStrategy> {

	public NPCTileEvent(final NPC entity, final TileStrategy strategy, final Runnable event) {
		super(entity, strategy, event, 0);
	}
	
	public NPCTileEvent(final NPC entity, final TileStrategy strategy, final Runnable event, final int delay) {
		super(entity, strategy, event, delay);
	}

	@Override
	protected void resetFlag() {
		
	}

	@Override
	protected void inform(final String message) {
		
	}

}
