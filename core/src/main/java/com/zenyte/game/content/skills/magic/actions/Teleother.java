package com.zenyte.game.content.skills.magic.actions;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 16. juuli 2018 : 00:56:50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class Teleother {

	private static final int INTERFACE_ID = 326;
	
	public static final void request(final Player player, final Player target, final Teleport teleport) {
		target.getTemporaryAttributes().put("teleother_teleport", teleport);
		target.getPacketDispatcher().sendComponentText(INTERFACE_ID, 89, player.getName());
		target.getPacketDispatcher().sendComponentText(INTERFACE_ID, 91, teleport.toString());
		target.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, INTERFACE_ID);
	}
	
}
