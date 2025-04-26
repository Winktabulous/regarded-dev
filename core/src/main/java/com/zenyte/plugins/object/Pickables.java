package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.PickPlant;
import com.zenyte.game.world.object.WorldObject;

public class Pickables implements ObjectAction {

	@Override
	public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
		if (option.equalsIgnoreCase("Pick"))
			player.getActionManager().setAction(new PickPlant(object));
	}

	@Override
	public Object[] getObjects() {
		return PickPlant.Pickables.map.keySet().toArray(new Object[0]);
	}

}
