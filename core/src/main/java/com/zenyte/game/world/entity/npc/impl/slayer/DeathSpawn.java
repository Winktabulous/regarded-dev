package com.zenyte.game.world.entity.npc.impl.slayer;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;

/**
 * @author Tommeh | 13 dec. 2017 : 18:41:35
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class DeathSpawn extends NPC implements Spawnable {
	
	private int ticks;

	public DeathSpawn(final int id, final Location tile, final Direction direction, final int radius) {
		super(id, tile, direction, radius);
		spawned = true;
	}

	@Override
	public void processNPC() {
		if (++ticks == 100) {
			finish();
			return;
		}
		super.processNPC();
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 1;
	}

	@Override
	public boolean triggersAutoRetaliate() {
		return false;
	}

	@Override
	public boolean validate(final int id, final String name) {
		return id == 10;
	}

}
