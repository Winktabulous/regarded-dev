package com.zenyte.plugins.item;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.model.ui.PaneType;
import com.zenyte.game.packet.out.FreeCam;
import com.zenyte.game.packet.out.IfOpenTop;
import com.zenyte.game.world.entity.Location;

/**
 * @author Kris | 25. aug 2018 : 22:35:07
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class OrbOfOculus extends ItemPlugin {

	@Override
	public void handle() {
		bind("Scry", (player, item, slotId) -> {
			player.lock(1);
			player.send(new FreeCam(true));
			player.send(new IfOpenTop(PaneType.ORB_OF_OCULUS));
            player.getInterfaceHandler().getVisible().forcePut(PaneType.ORB_OF_OCULUS.getId() << 16, PaneType.ORB_OF_OCULUS.getId());
			player.getTemporaryAttributes().put("oculusStart", new Location(player.getLocation()));
		});
	}

	@Override
	public int[] getItems() {
		return new int[] { 22364 };
	}

}
