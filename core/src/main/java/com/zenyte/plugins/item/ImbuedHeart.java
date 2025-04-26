package com.zenyte.plugins.item;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.variables.TickVariable;

/**
 * @author Kris | 25. aug 2018 : 22:29:05
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class ImbuedHeart extends ItemPlugin {
	private static final Graphics GRAPHICS = new Graphics(1316);

	@Override
	public void handle() {
		bind("Invigorate", (player, item, slotId) -> {
			if (player.getVariables().getTime(TickVariable.IMBUED_HEART_COOLDOWN) == 0 && player.getVariables().getTime(TickVariable.SATURATED_HEART_COOLDOWN) == 0) {
				final int boost = (int) (player.getSkills().getLevel(SkillConstants.MAGIC) * 0.1 + 1);
				player.getSkills().boostSkill(SkillConstants.MAGIC, boost);
				player.setGraphics(GRAPHICS);
				player.getVariables().schedule(700, TickVariable.IMBUED_HEART_COOLDOWN);
			} else {
				final int totalSeconds = (int) ((player.getVariables().getTime(TickVariable.IMBUED_HEART_COOLDOWN) > 0 ?
						player.getVariables().getTime(TickVariable.IMBUED_HEART_COOLDOWN) : player.getVariables().getTime(TickVariable.SATURATED_HEART_COOLDOWN)) * 0.6F);
				final int seconds = totalSeconds % 60;
				final int minutes = totalSeconds / 60;
				player.sendMessage("The heart is still drained of its power. Judging by how it feels, it will be ready in around " + (minutes == 0 ? (seconds + " seconds.") : (minutes + " minutes.")));
			}
		});
	}

	@Override
	public int[] getItems() {
		return new int[] {20724};
	}
}
