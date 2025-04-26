package com.zenyte.game.content.boss.nightmare.object;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.boss.nightmare.NightmareGlobal;
import com.zenyte.game.world.entity.player.BossTimer;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

public class NightmareScoreboard implements ObjectAction {

	@Override
	public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		GameInterface.NIGHTMARE_STATISTICS.open(player);
		player.getPacketDispatcher().sendComponentText(GameInterface.NIGHTMARE_STATISTICS, 8, player.getNotificationSettings().getKillcount("The Nightmare"));
		player.getPacketDispatcher().sendComponentText(GameInterface.NIGHTMARE_STATISTICS, 10, player.getNumericAttribute("nightmare_death"));
		player.getPacketDispatcher().sendComponentText(GameInterface.NIGHTMARE_STATISTICS, 12, player.getBossTimer().personalBest("The Nightmare"));

		player.getPacketDispatcher().sendComponentText(GameInterface.NIGHTMARE_STATISTICS, 14, NightmareGlobal.statistics.getGlobalKillCount());
		player.getPacketDispatcher().sendComponentText(GameInterface.NIGHTMARE_STATISTICS, 16, NightmareGlobal.statistics.getGlobalDeathCount());
		player.getPacketDispatcher().sendComponentText(GameInterface.NIGHTMARE_STATISTICS, 18, BossTimer.formatBestTime(NightmareGlobal.statistics.getGlobalBestKillTimeSeconds()));

	}

	@Override
	public Object[] getObjects() {
		return new Object[] {ObjectId.SCOREBOARD_37949};
	}

}
