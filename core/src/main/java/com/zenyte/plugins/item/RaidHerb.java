package com.zenyte.plugins.item;

import com.zenyte.game.content.chambersofxeric.skills.RaidHerblore;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

import java.util.OptionalInt;

/**
 * @author Kris | 25. aug 2018 : 22:38:49
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class RaidHerb extends ItemPlugin {

	@Override
	public void handle() {
		bind("Clean", (player, item, slotId) -> {
            RaidHerblore.RaidHerbCleaningAction.clean(player, item.getId(), OptionalInt.of(slotId));
            player.getActionManager().setAction(new RaidHerblore.RaidHerbCleaningAction(item.getId()));
        });
	} 

	@Override
	public int[] getItems() {
		return new int[] { 20901, 20904, 20907 };
	}

}
