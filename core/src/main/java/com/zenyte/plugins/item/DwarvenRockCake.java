package com.zenyte.plugins.item;

import com.zenyte.game.content.consumables.ConsumableAnimation;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.variables.PlayerVariables;

/**
 * @author Kris | 25. aug 2018 : 22:19:27
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class DwarvenRockCake extends ItemPlugin {
	private static final ForceTalk message = new ForceTalk("Ow, I think I broke a tooth!");

	@Override
	public void handle() {
		bind("Eat", (player, item, slotId) -> {
			final PlayerVariables variables = player.getVariables();
			if (variables.getFoodDelay() > 0) {
				return;
			}
			if (player.getHitpoints() > 2) {
				variables.setFoodDelay(1);
				player.setAnimation(ConsumableAnimation.getEatAnimation(player));
				player.applyHit(new Hit(1, HitType.REGULAR));
				player.setForceTalk(message);
			} else {
				player.sendMessage("Your hitpoints are too low to do this!");
			}
		});
		bind("Guzzle", (player, item, slotId) -> {
			final PlayerVariables variables = player.getVariables();
			if (variables.getFoodDelay() > 0) {
				return;
			}
			final int damage = (int) Math.ceil(player.getHitpoints() * 0.1F);
			if (damage >= player.getHitpoints()) {
				player.sendMessage("Your hitpoints are too low to do this!");
				return;
			}
			variables.setFoodDelay(1);
			player.setAnimation(ConsumableAnimation.getEatAnimation(player));
			player.applyHit(new Hit(damage, HitType.REGULAR));
		});
	}

	@Override
	public int[] getItems() {
		return new int[] {7509, 7510};
	}
}
