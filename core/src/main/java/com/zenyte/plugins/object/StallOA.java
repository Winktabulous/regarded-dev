package com.zenyte.plugins.object;

import com.zenyte.game.content.skills.thieving.Stall;
import com.zenyte.game.content.skills.thieving.actions.StallThieving;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

import java.util.ArrayList;

/**
 * @author Kris | 11. apr 2018 : 16:00.49
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class StallOA implements ObjectAction {
	@Override
	public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
		StallThieving.handleStall(player, object);
	}

	@Override
	public Object[] getObjects() {
		final ArrayList<Object> list = new ArrayList<Object>();
		for (final Stall stall : Stall.VALUES) list.add(stall.getObjectId());
		return list.toArray(new Object[0]);
	}
}
