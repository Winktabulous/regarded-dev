package com.zenyte.plugins.dialogue;

import com.zenyte.game.world.entity.player.NotificationSettings;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Setting;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 24 feb. 2018 : 16:34:32
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class RingOfWealthD extends Dialogue {

	public RingOfWealthD(Player player) {
		super(player);
	}

	@Override
	public void buildDialogue() {
		options(TITLE, "View boss log.", !player.getBooleanSetting(Setting.ROW_CURRENCY_COLLECTOR) ? "Toggle currency collection off." : "Toggle currency collection on.")
		.onOptionOne(() -> { 
			player.getNotificationSettings().sendKillLog(NotificationSettings.BOSS_NPC_NAMES, true); 
			finish(); 
		})
		.onOptionTwo(() -> {
			player.getSettings().setSetting(Setting.ROW_CURRENCY_COLLECTOR, player.getBooleanSetting(Setting.ROW_CURRENCY_COLLECTOR) ? 0 : 1);
			setKey(2);
		});
		plain(2, "Currency collector has been turned " + (!player.getBooleanSetting(Setting.ROW_CURRENCY_COLLECTOR) ? "off." : "on."));
	}

}
