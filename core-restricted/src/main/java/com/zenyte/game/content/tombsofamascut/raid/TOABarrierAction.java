package com.zenyte.game.content.tombsofamascut.raid;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.RegionArea;

/**
 * @author Savions.
 */
public class TOABarrierAction implements ObjectAction {

	@Override public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		final RegionArea area = GlobalAreaManager.getArea(player);
		if (area instanceof final TOARaidArea toaRaidArea) {
			toaRaidArea.handleBarrier(player, object, !"Pass".equals(option));
		}
	}

	@Override public Object[] getObjects() {
		return new Object[] {45135};
	}
}
