package com.zenyte.game.content.minigame.warriorsguild.npcs;

import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;

/**
 * @author Kris | 18. dets 2017 : 2:06.15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class Shanomi extends NPC implements Spawnable {

	private static final ForceTalk[] MESSAGES = new ForceTalk[] {
			new ForceTalk("Do nothing which is no use."),
			new ForceTalk("Way of the Warrior this is."),
			new ForceTalk("Think not dishonestly."),
			new ForceTalk("The Way in training is."),
			new ForceTalk("Acquainted with every art become."),
			new ForceTalk("Ways of all professions know you."),
			new ForceTalk("Gain and loss between you must distinguish."),
			new ForceTalk("Judgement and understanding for everything develop you must."),
			new ForceTalk("Those things which cannot be seen, perceive them."),
			new ForceTalk("Trifles pay attention even to."),
	};

	public Shanomi(final int id, final Location tile, final Direction facing, final int radius) {
		super(id, tile, facing, radius);
	}
	
	private long lastAnnouncement;
	private int cycle;
	
	@Override
	public void processNPC() {
		super.processNPC();
		if (lastAnnouncement < Utils.currentTimeMillis()) {
			lastAnnouncement = Utils.currentTimeMillis() + 10000;
			setForceTalk(MESSAGES[cycle++ % MESSAGES.length]);
		}
	}

	@Override
	public boolean validate(final int id, final String name) {
		return id == 2462;
	}

}
