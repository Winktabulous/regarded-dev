package com.zenyte.plugins.dialogue.skills;

import com.zenyte.game.content.skills.smithing.SmeltableBar;
import com.zenyte.game.content.skills.smithing.Smelting;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.SkillDialogue;

/**
 * @author Tommeh | 25 aug. 2018 | 16:54:47
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class SmeltingD extends SkillDialogue {
	private final WorldObject object;

	public SmeltingD(final Player player, final WorldObject object) {
		super(player, "What would you like to smelt?", new Item(2349), new Item(2351), new Item(9467), new Item(2355), new Item(2353), new Item(2357), new Item(2359), new Item(2361), new Item(2363));
		this.object = object;
	}

	@Override
	public void run(int slotId, int amount) {
		final SmeltableBar data = SmeltableBar.VALUES[slotId];
		if (data != null) {
			player.getActionManager().setAction(new Smelting(data, amount, object));
		}
	}
}
