package com.zenyte.plugins.item;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;

/**
 * @author Kris | 25. aug 2018 : 22:25:52
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class GroundAshes extends ItemPlugin {

	private static final Animation ANIM = new Animation(4185);
	private static final Graphics GFX = new Graphics(689);
	
	@Override
	public void handle() {
		bind("Dust-hands", (player, item, slotId) -> {
			if (!(player.getX() >= 2258 && player.getX() <= 2877 
					&& player.getY() >= 3544 && player.getY() <= 3557
					&& player.getPlane() == 1)) {
				player.sendMessage("You have no need to dust your hands outside of the warriors' guild shotput area.");
				return;
			}
			if (player.getWeapon() != null || player.getShield() != null || player.getGloves() != null) {
				player.sendMessage("You can't be wielding anything to dust your hands.");
				return;
			}
			player.lock(7);
			player.getInventory().deleteItem(slotId, item);
			player.sendMessage("You dust your hands with the ground ashes for better grip.");
			player.setAnimation(ANIM);
			player.setGraphics(GFX);
			player.getTemporaryAttributes().put("shotputGrip", 2);
		});
	}

	@Override
	public int[] getItems() {
		return new int[] { 8865 };
	}

}
