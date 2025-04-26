package com.zenyte.plugins.object;

import com.zenyte.game.model.polls.Poll;
import com.zenyte.game.model.polls.PollManager;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 2. apr 2018 : 23:10.46
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class PollBoothOA implements ObjectAction {

	@Override
	public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
		final Poll poll = PollManager.map.get(1);
		if (poll == null) {
			player.sendMessage("There are no polls you could view at this time.");
			return;
		}
		player.getPollManager().open(poll);
	}

	@Override
	public Object[] getObjects() {
		return new Object[] { "Poll booth" };
	}

}
