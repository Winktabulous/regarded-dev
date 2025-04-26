package com.zenyte.plugins.interfaces;

import com.google.common.collect.ImmutableList;
import com.zenyte.game.model.ui.UserInterface;
import com.zenyte.game.world.entity.player.NotificationSettings;
import com.zenyte.game.world.entity.player.Player;
import mgi.utilities.StringFormatUtil;

/**
 * @author Tommeh "," 21 feb. 2018 : 21:09:53
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public final class KillLogInterface implements UserInterface {
	
	@Override
	public void handleComponentClick(final Player player, final int interfaceId, final int componentId, final int slotId, final int itemId, final int optionId, final String option) {
		if (player.getTemporaryAttributes().get("KillLogType") == null) {
			return;
		}
		final String type = (String) player.getTemporaryAttributes().get("KillLogType");
		final ImmutableList<String> names = type.equals("Boss") ? NotificationSettings.BOSS_NPC_NAMES : NotificationSettings.SLAYER_NPC_NAMES;
		if (componentId == 16) {
			final String name = names.get(slotId);
			player.getTemporaryAttributes().put("SelectedSlayerLogStreak", new Object[] { name, slotId });
		} else if (componentId == 25) {
			final Object[] data = (Object[]) player.getTemporaryAttributes().get("SelectedSlayerLogStreak");
			final String name = (String) data[0];
			final int slot = (int) data[1];
			final int hash = (player.getSettings().getKillsLog().getOrDefault(name, 0) & 0xFFFF) & 0xFFFF | 0;
			player.getSettings().getKillsLog().put(name, hash);
			player.getPacketDispatcher().sendClientScript(1588, slot);
			player.sendMessage("Your killstreak of " + StringFormatUtil.formatString(name + (type.equals("Slayer") ? "s" : "")) + " has been reset.");
		}
	}

	@Override
	public int[] getInterfaceIds() {
		return new int[] { 549 };
	}

}
