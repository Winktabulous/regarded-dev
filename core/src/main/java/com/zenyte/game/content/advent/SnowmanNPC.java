package com.zenyte.game.content.advent;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;

public class SnowmanNPC extends NPC {

	private final int index;

	public SnowmanNPC(int id, Location tile, Direction facing, int index) {
		super(id, tile, facing, 0);
		this.index = index;
	}

	public int getEventIndex() {
		return index;
	}

}
