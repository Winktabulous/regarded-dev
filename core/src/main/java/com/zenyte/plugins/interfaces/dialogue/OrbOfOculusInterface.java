package com.zenyte.plugins.interfaces.dialogue;

import com.zenyte.game.model.ui.UserInterface;
import com.zenyte.game.packet.out.FreeCam;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;

/**
 * @author Kris | 10. juuli 2018 : 21:29:41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class OrbOfOculusInterface implements UserInterface {
	@Override
	public void handleComponentClick(final Player player, final int interfaceId, final int componentId, final int slotId, final int itemId, final int optionId, final String option) {
		final Object loc = player.getTemporaryAttributes().get("oculusStart");
		if (!(loc instanceof Location)) {
			return;
		}
		player.send(new FreeCam(false));
		if (player.getPrivilege().eligibleTo(PlayerPrivilege.ADMINISTRATOR)) {
			player.setLocation((Location) loc);
		}
		player.lock(1);
		WorldTasksManager.schedule(() -> player.send(new FreeCam(true)));
	}

	@Override
	public int[] getInterfaceIds() {
		return new int[] {16};
	}
}
