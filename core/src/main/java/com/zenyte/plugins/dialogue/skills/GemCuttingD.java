package com.zenyte.plugins.dialogue.skills;

import com.zenyte.game.content.skills.crafting.CraftingDefinitions.GemCuttingData;
import com.zenyte.game.content.skills.crafting.actions.GemCuttingCrafting;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.SkillDialogue;

/**
 * @author Tommeh | 25 aug. 2018 | 20:39:46
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class GemCuttingD extends SkillDialogue {

	private final GemCuttingData data;

	public GemCuttingD(final Player player, final GemCuttingData data) {
		super(player, getInterfaceItems(data));
		this.data = data;
	}

	@Override
	public void run(final int slotId, final int amount) {
		if (data != null) {
			player.getActionManager().setAction(new GemCuttingCrafting(data, slotId, amount));
		}
	}

	public static Item[] getInterfaceItems(GemCuttingData data) {
		Item[] items = null;
		if (data.equals(GemCuttingData.AMETHYST)) {
			items = data.getProducts();
		} else {
			items = new Item[] { data.getMaterial() };
		}
		return items;
	}

}
