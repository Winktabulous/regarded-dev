package com.zenyte.plugins.object;

import com.zenyte.game.content.skills.thieving.Chest;
import com.zenyte.game.content.skills.thieving.actions.ChestThieving;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Corey
 * @since 23/11/19
 */
public final class ThievingChestObject implements ObjectAction {
	
	@Override
	public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
		ChestThieving.handleChest(player, object, option.equalsIgnoreCase("Search for traps"));
	}
	
	@Override
	public Object[] getObjects() {
		return Chest.data.keySet().toArray();
	}
	
}
