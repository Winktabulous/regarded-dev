package com.zenyte.game.world.entity.pathfinding.events.npc;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.pathfinding.events.RouteEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.EntityStrategy;

/**
 * @author Kris | 14. juuli 2018 : 04:00:07
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class NPCEntityEvent extends RouteEvent<NPC, EntityStrategy> {

	public NPCEntityEvent(final NPC entity, final EntityStrategy strategy) {
		super(entity, strategy, null, 0);
	}
	
	public NPCEntityEvent(final NPC entity, final EntityStrategy strategy, final Runnable event) {
		super(entity, strategy, event, 0);
	}

	@Override
	protected void resetFlag() {
		
	}

	@Override
	protected void inform(final String message) {
		
	}

}
