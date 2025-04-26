package com.zenyte.plugins.item;

import com.zenyte.game.content.consumables.ConsumableEffects;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.variables.TickVariable;

/**
 * @author Savions.
 */
public class SaturatedHeart extends ItemPlugin {
	private static final Graphics GRAPHICS = new Graphics(2287);

	@Override
	public void handle() {
		bind("Invigorate", (player, item, slotId) -> {
			if (player.getVariables().getTime(TickVariable.IMBUED_HEART_COOLDOWN) == 0 && player.getVariables().getTime(TickVariable.SATURATED_HEART_COOLDOWN) == 0) {
				ConsumableEffects.applySaturatedHeart(player);
				player.setGraphics(GRAPHICS);
				player.getVariables().schedule(500, TickVariable.SATURATED_HEART_COOLDOWN);
			} else {
				final int totalSeconds = (int) ((player.getVariables().getTime(TickVariable.SATURATED_HEART_COOLDOWN) > 0 ?
						player.getVariables().getTime(TickVariable.SATURATED_HEART_COOLDOWN) : player.getVariables().getTime(TickVariable.IMBUED_HEART_COOLDOWN)) * 0.6F);
				final int seconds = totalSeconds % 60;
				final int minutes = totalSeconds / 60;
				System.out.println(minutes + ", " + seconds);
				player.sendMessage("The heart is still drained of its power. Judging by how it feels, it will be ready in around " + (minutes == 0 ? (seconds + " seconds.") : (minutes + " minutes.")));
			}
		});
	}

	@Override
	public int[] getItems() {
		return new int[] {ItemId.SATURATED_HEART};
	}
}
