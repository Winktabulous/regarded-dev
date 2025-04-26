package com.zenyte.game.content.advent;

import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

public class ReindeerObjectPlugin implements ObjectAction {

	@Override
	public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		if (!AdventCalendarManager.adventEnabled()) {
			player.sendDeveloperMessage("Advent calendar not enabled.");
			return;
		}

		if (!(object instanceof ReindeerObject)) {
			return;
		}

		ReindeerObject snowmanNPC = (ReindeerObject) object;
		int current = AdventCalendarManager.getChallengeProgress(player, 13);
		int mask = 1 << snowmanNPC.getIndex();
		if ((current & mask) != 0) {
			player.sendMessage("You've already pet this Reindeer.");
			return;
		}

		current |= mask;
		AdventCalendarManager.setChallenge(player, 2022, 13, current);

		AdventDay adventDay = AdventCalendarManager.getDay(13);
		int currentValue = adventDay.getValue(current);
		int left = adventDay.getCountToComplete() - currentValue;
		player.setAnimation(Animation.LADDER_DOWN);

		if (left > 0) {
			player.sendMessage("Congratulations! You've pet a Reindeer. Only " + left + " to go!");
		}
	}

	@Override
	public Object[] getObjects() {
		return new Object[]{ObjectId.REINDEER};
	}

}
