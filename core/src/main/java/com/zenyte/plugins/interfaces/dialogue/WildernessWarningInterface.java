package com.zenyte.plugins.interfaces.dialogue;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.model.ui.UserInterface;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.object.WildernessDitchObject;

/**
 * @author Kris | 10. juuli 2018 : 21:35:15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class WildernessWarningInterface implements UserInterface {

	private static final Animation ANIM = new Animation(6132);
	
	@Override
	public void handleComponentClick(final Player player, final int interfaceId, final int componentId, final int slotId, final int itemId, final int optionId, final String option) {
		if (componentId == 1) {
			if (slotId == 0) {
				player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
			} else if (slotId == 1) {
				player.getTemporaryAttributes().put("EnteredWildernessDitch", true);
				WildernessDitchObject.jump(player, (WorldObject) player.getTemporaryAttributes().get("WildernessDitch"));
			}
		}
	}

	@Override
	public int[] getInterfaceIds() {
		return new int[] { 475 };
	}
 
}
