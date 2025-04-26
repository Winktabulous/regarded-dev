package com.zenyte.plugins.object;

import com.zenyte.game.content.skills.woodcutting.CanoeLocation;
import com.zenyte.game.content.skills.woodcutting.actions.CanoeHandler;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 13 aug. 2018 | 16:23:28
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class CanoeStationObject implements ObjectAction {

	@Override
	public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
		final int bit = player.getVarManager().getBitValue(object.getDefinitions().getVarbit());
		if (bit == 0) {
			if (player.getSkills().getLevel(SkillConstants.WOODCUTTING) < 12) {
				player.sendMessage("You need a Woodcutting level of 12 to chop this tree.");
				return;
			}
			CanoeHandler.chopCanoeStation(player, object);
		} else if (bit >= 1 && bit <= 4) {
			CanoeHandler.floatCanoe(player, object);
		} else if (bit == 10) {
			CanoeHandler.sendCanoeShapingInterface(player, object);
		} else {
			CanoeLocation.sendInterfaceConfiguration(player, bit);
		}
	}

	@Override
	public Object[] getObjects() {
		return new Object[] { "Canoe Station" };
	}

}
