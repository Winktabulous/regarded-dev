package com.zenyte.plugins.itemonobject;

import com.zenyte.game.content.skills.smithing.DragonSqShieldCreationAction;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 21 jul. 2018 | 23:16:18
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ShieldHalfOnAnvilAction implements ItemOnObjectAction {

	@Override
	public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
		player.getActionManager().setAction(new DragonSqShieldCreationAction());
	}

	@Override
	public Object[] getItems() {
		return new Object[] { 2366, 2368 };
	}

	@Override
	public Object[] getObjects() {
		return new Object[] { "Anvil" };
	}

}
