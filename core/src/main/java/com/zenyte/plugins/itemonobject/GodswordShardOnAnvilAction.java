package com.zenyte.plugins.itemonobject;

import com.zenyte.game.content.skills.smithing.GodswordShardCombination;
import com.zenyte.game.content.skills.smithing.GodswordShardCombinationAction;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 21 jul. 2018 | 23:16:18
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class GodswordShardOnAnvilAction implements ItemOnObjectAction {
	@Override
	public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
		final GodswordShardCombination combination = GodswordShardCombination.getCombination(player.getInventory().getContainer().getItems().values());
		if (combination != null) {
			player.getActionManager().setAction(new GodswordShardCombinationAction(combination));
		}
	}

	@Override
	public Object[] getItems() {
		return new Object[] {11794, 11796, 11800, 11818, 11820, 11822};
	}

	@Override
	public Object[] getObjects() {
		return new Object[] {"Anvil"};
	}
}
