package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.plugins.dialogue.ItemChat;

/**
 * @author Tommeh | 4 jul. 2018 | {time
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ElixirOnSpiritShieldItemOnItemAction implements ItemOnItemAction {
	
	private static final Item BLESSED_SPIRIT_SHIELD =  new Item(12831);

	@Override
	public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
		if (player.getSkills().getLevel(SkillConstants.PRAYER) < 85) {
			player.sendMessage("You need a Prayer level of at least 85 to enqueue the elixir onto the shield.");
			return;
		}
		player.getDialogueManager().start(new ItemChat(player, BLESSED_SPIRIT_SHIELD, "The spirit shield glows an eerie holy glow."));
		player.getInventory().deleteItem(from);
		player.getInventory().deleteItem(to);
		player.getInventory().addItem(BLESSED_SPIRIT_SHIELD);
	}

	@Override
	public int[] getItems() {
		return new int[] { 12829, 12833 };
	}

}
