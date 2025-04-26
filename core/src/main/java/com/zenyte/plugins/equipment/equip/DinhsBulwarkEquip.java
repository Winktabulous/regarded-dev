package com.zenyte.plugins.equipment.equip;

import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;

/**
 * @author Kris | 27. march 2018 : 4:27.26
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class DinhsBulwarkEquip implements EquipPlugin {

	private static final Animation ANIM = new Animation(7512);
	private static final Graphics GFX = new Graphics(1336);

	@Override
	public boolean handle(Player player, Item item, int slotId, int equipmentSlot) {
		return true;
	}

	@Override
	public  void onEquip(final Player player, final Container container, final Item equippedItem) {
		player.getTemporaryAttributes().put("dinhsbulwarkdelay", Utils.currentTimeMillis() + 4800);
		player.setGraphics(GFX);
		player.setAnimation(ANIM);
		if (player.getCombatDefinitions().getStyle() == 1) {
			player.getCombatDefinitions().setStyle(0);
		}
	}

	@Override
	public int[] getItems() {
		return new int[] { 21015 };
	}

}
