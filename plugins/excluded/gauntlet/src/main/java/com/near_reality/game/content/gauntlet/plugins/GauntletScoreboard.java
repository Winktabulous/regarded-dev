package com.near_reality.game.content.gauntlet.plugins;

import com.near_reality.game.content.gauntlet.GauntletModule;
import com.near_reality.game.content.gauntlet.GauntletStatistics;
import com.zenyte.game.GameInterface;
import com.zenyte.game.world.entity.player.BossTimer;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

public class GauntletScoreboard implements ObjectAction {

	@Override
	public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		GauntletStatistics statistics = GauntletModule.statistics;

		player.getInterfaceHandler().sendInterface(GameInterface.GAUNTLET_SCOREBOARD);
		player.getPacketDispatcher().sendComponentText(GameInterface.GAUNTLET_SCOREBOARD, 5, "The Gauntlet");

		player.getPacketDispatcher().sendComponentText(GameInterface.GAUNTLET_SCOREBOARD, 6, "Your Completions:");
		player.getPacketDispatcher().sendComponentText(GameInterface.GAUNTLET_SCOREBOARD, 7, player.getNumericAttribute("gauntlet_completions").intValue());

		player.getPacketDispatcher().sendComponentText(GameInterface.GAUNTLET_SCOREBOARD, 8, "Global Completions:");
		player.getPacketDispatcher().sendComponentText(GameInterface.GAUNTLET_SCOREBOARD, 9, statistics.getGlobalCompletionCount());

		player.getPacketDispatcher().sendComponentText(GameInterface.GAUNTLET_SCOREBOARD, 10, "Your Deaths:");
		player.getPacketDispatcher().sendComponentText(GameInterface.GAUNTLET_SCOREBOARD, 11, player.getNumericAttribute("gauntletDeathCount").intValue());

		player.getPacketDispatcher().sendComponentText(GameInterface.GAUNTLET_SCOREBOARD, 12, "Global Deaths:");
		player.getPacketDispatcher().sendComponentText(GameInterface.GAUNTLET_SCOREBOARD, 13, statistics.getGlobalDeathCount());

		player.getPacketDispatcher().sendComponentText(GameInterface.GAUNTLET_SCOREBOARD, 14, "Your Best Time:");
		player.getPacketDispatcher().sendComponentText(GameInterface.GAUNTLET_SCOREBOARD, 15, player.getBossTimer().personalBest("gauntlet"));

		player.getPacketDispatcher().sendComponentText(GameInterface.GAUNTLET_SCOREBOARD, 16, "Global Best Time:");
		player.getPacketDispatcher().sendComponentText(GameInterface.GAUNTLET_SCOREBOARD, 17, BossTimer.formatBestTime(statistics.getGlobalBestCompletionTimeSeconds()));

		player.getPacketDispatcher().sendComponentText(GameInterface.GAUNTLET_SCOREBOARD, 18, "The Corrupted Gauntlet");

		player.getPacketDispatcher().sendComponentText(GameInterface.GAUNTLET_SCOREBOARD, 19, "Your Completions:");
		player.getPacketDispatcher().sendComponentText(GameInterface.GAUNTLET_SCOREBOARD, 20, player.getNumericAttribute("corrupted_gauntlet_completions").intValue());

		player.getPacketDispatcher().sendComponentText(GameInterface.GAUNTLET_SCOREBOARD, 21, "Global Completions:");
		player.getPacketDispatcher().sendComponentText(GameInterface.GAUNTLET_SCOREBOARD, 22, statistics.getGlobalCorruptedCompletionCount());

		player.getPacketDispatcher().sendComponentText(GameInterface.GAUNTLET_SCOREBOARD, 23, "Your Deaths:");
		player.getPacketDispatcher().sendComponentText(GameInterface.GAUNTLET_SCOREBOARD, 24, player.getNumericAttribute("gauntletCorruptedDeathCount").intValue());

		player.getPacketDispatcher().sendComponentText(GameInterface.GAUNTLET_SCOREBOARD, 25, "Global Deaths:");
		player.getPacketDispatcher().sendComponentText(GameInterface.GAUNTLET_SCOREBOARD, 26, statistics.getGlobalCorruptedDeathCount());

		player.getPacketDispatcher().sendComponentText(GameInterface.GAUNTLET_SCOREBOARD, 27, "Your Best Time:");
		player.getPacketDispatcher().sendComponentText(GameInterface.GAUNTLET_SCOREBOARD, 28, player.getBossTimer().personalBest("corrupt_gauntlet"));

		player.getPacketDispatcher().sendComponentText(GameInterface.GAUNTLET_SCOREBOARD, 29, "Global Best Time:");
		player.getPacketDispatcher().sendComponentText(GameInterface.GAUNTLET_SCOREBOARD, 30, BossTimer.formatBestTime(statistics.getGlobalCorruptedBestCompletionTimeSeconds()));
	}

	@Override
	public Object[] getObjects() {
		return new Object[] {ObjectId.SCOREBOARD_36060};
	}

}
