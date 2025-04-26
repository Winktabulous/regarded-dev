package com.zenyte.game.world.entity.npc.impl.misc;

import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;

/**
 * @author Kris | 6. apr 2018 : 15:28.36
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>}
 */
public final class Cow extends NPC implements Spawnable {

	private static final ForceTalk[] MESSAGES = new ForceTalk[] { new ForceTalk("Moo!"), new ForceTalk("Moooo!"), new ForceTalk("Moooooooooo!") };
	
	public Cow(final int id, final Location tile, final Direction facing, final int radius) {
		super(id, tile, facing, radius);
	}

	private long chatDelay;
	
	@Override
	public void processNPC() {
		super.processNPC();
		if (!isDead() && chatDelay < Utils.currentTimeMillis() && Utils.random(100) == 0) {
			chatDelay = Utils.currentTimeMillis() + 5000;
			setForceTalk(MESSAGES[Utils.random(MESSAGES.length - 1)]);
		}
	}

	@Override
	public boolean validate(final int id, final String name) {
		return name.equals("cow") || name.equals("cow calf");
	}
	
}
