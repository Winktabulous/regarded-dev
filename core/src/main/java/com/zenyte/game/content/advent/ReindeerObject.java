package com.zenyte.game.content.advent;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

public class ReindeerObject extends WorldObject {

	private final int index;

	public ReindeerObject(int index, Location location) {
		super(ObjectId.REINDEER, 10, 0, location);
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

}
