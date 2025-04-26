package com.zenyte.plugins.object;

import com.zenyte.game.GameInterface;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

public class ChallengesScoreboard implements ObjectAction {

	@Override
	public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		if(option.equalsIgnoreCase("View")) {
			if(player.getGameMode().isGroupIronman())
				GameInterface.CHALLENGES.open(player);
			else GameInterface.CHALLENGES_SOLO.open(player);
		}
		if(option.equalsIgnoreCase("Group Challenges")) {
			GameInterface.CHALLENGES.open(player);
		}
		if(option.equalsIgnoreCase("Solo Challenges")) {
			GameInterface.CHALLENGES_SOLO.open(player);
		}
	}

	@Override
	public Object[] getObjects() {
		return new Object[]{ObjectId.SCOREBOARD_40448};
	}

}
