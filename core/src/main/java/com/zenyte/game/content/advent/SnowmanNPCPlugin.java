package com.zenyte.game.content.advent;

import com.zenyte.game.world.entity.npc.actions.NPCPlugin;

public class SnowmanNPCPlugin extends NPCPlugin {

	@Override
	public void handle() {
		bind("Talk-to", (player, npc) -> {
			if (!AdventCalendarManager.adventEnabled()) {
				player.sendDeveloperMessage("Advent calendar not enabled.");
				return;
			}

			SnowmanNPC snowmanNPC = (SnowmanNPC) npc;
			int current = AdventCalendarManager.getChallengeProgress(player, 1);
			int mask = 1 << snowmanNPC.getEventIndex();
			if ((current & mask) != 0) {
				player.sendMessage("You've already found this Snowman.");
				return;
			}

			current |= mask;
			AdventCalendarManager.setChallenge(player, 2022, 1, current);

			AdventDay adventDay = AdventCalendarManager.getDay(1);
			int currentValue = adventDay.getValue(current);
			int left = adventDay.getCountToComplete() - currentValue;

			if (left > 0) {
				player.sendMessage("Congratulations! You've found a Snowman. Only " + left + " to go!");
			}
		});
	}

	@Override
	public int[] getNPCs() {
		return new int[] { 16061, 16062, 16063 };
	}

}
