package com.zenyte.game.world.entity.pathfinding.events.npc;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.pathfinding.events.RouteEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.ObjectStrategy;

/**
 * @author Kris | 14. juuli 2018 : 04:00:44
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class NPCObjectEvent extends RouteEvent<NPC, ObjectStrategy> {

	public NPCObjectEvent(final NPC entity, final ObjectStrategy strategy) {
		super(entity, strategy, null, 0);
	}
	
	public NPCObjectEvent(final NPC entity, final ObjectStrategy strategy, final Runnable event) {
		super(entity, strategy, event, 0);
	}

	@Override
	protected void resetFlag() {
		
	}

	@Override
	protected void inform(final String message) {
		
	}

}
