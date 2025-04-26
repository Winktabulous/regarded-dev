package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

import java.util.ArrayList;

/**
 * @author Kris | 2. mai 2018 : 21:28:21
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class RaidFarmingPatchOA implements ObjectAction {
	@Override
	public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
		player.getRaid().ifPresent(raid -> raid.getFarming().handle(player, object, option));
	}

	@Override
	public Object[] getObjects() {
		final ArrayList<Object> list = new ArrayList<>();
		for (int i = 29997; i <= 30011; i++) {
			list.add(i);
		}
		list.add(29765);
		return list.toArray(new Object[0]);
	}
}
