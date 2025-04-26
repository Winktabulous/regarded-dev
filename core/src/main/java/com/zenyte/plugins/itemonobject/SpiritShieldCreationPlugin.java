package com.zenyte.plugins.itemonobject;

import com.zenyte.game.content.skills.smithing.SpiritShieldCreationAction;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.BossDropItem;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

public class SpiritShieldCreationPlugin implements ItemOnObjectAction {
	@Override
	public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
		final BossDropItem shield = BossDropItem.getItemByMaterials(item, SpiritShieldCreationAction.BLESSED_SPIRIT_SHIELD);
		player.getActionManager().setAction(new SpiritShieldCreationAction(shield));
	}

	@Override
	public Object[] getItems() {
		return new Object[] {12819, 12823, 12827};
	}

	@Override
	public Object[] getObjects() {
		return new Object[] {"Anvil"};
	}
}
