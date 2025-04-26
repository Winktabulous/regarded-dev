package com.zenyte.game.world.entity.npc.impl.misc;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;

/**
 * @author Kris | 28. juuli 2018 : 15:27:18
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class Insect extends NPC implements Spawnable {

	public Insect(final int id, final Location tile, final Direction facing, final int radius) {
		super(id, tile, facing, radius);
	}

	@Override
	public boolean validate(final int id, final String name) {
		return name.equals("Rat") || id == 3019 || id == 1553 || id == 3202;
	}
	
	@Override
	public boolean isEntityClipped() {
		return false;
	}

}
