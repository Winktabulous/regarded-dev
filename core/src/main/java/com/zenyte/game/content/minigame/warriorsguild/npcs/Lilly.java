package com.zenyte.game.content.minigame.warriorsguild.npcs;

import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;

/**
 * @author Kris | 18. dets 2017 : 1:59.43
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class Lilly extends NPC implements Spawnable {
	
	private static final ForceTalk[] MESSAGES = new ForceTalk[] {
			new ForceTalk("Don't look back, they might be gaining on you."),
			new ForceTalk("Demons are a Ghoul's best Friend."),
			new ForceTalk("It's not an optical illusion, it just looks like one."),
			new ForceTalk("If you don't care where you are, then you ain't lost.")
	};

	public Lilly(final int id, final Location tile, final Direction facing, final int radius) {
		super(id, tile, facing, radius);
	}
	
	private long lastAnnouncement;
	
	@Override
	public void processNPC() {
		super.processNPC();
		if (lastAnnouncement < Utils.currentTimeMillis()) {
			lastAnnouncement = Utils.currentTimeMillis() + 10000;
			setForceTalk(MESSAGES[Utils.random(MESSAGES.length - 1)]);
		}
	}

	@Override
	public boolean validate(final int id, final String name) {
		return id == 2470;
	}

}
