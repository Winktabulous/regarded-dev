package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Setting;
import com.zenyte.game.world.entity.player.Settings;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.DestroyItemMessage;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.plugins.dialogue.PlainChat;


public class SwampBarkScroll extends ItemPlugin {
	private static final Animation READ_ANIM = new Animation(7403);

	@Override
	public void handle() {
		bind("Read", (player, item, slotId) -> {
			final int id = item.getId();
			final String name = id == ItemId.RUNESCROLL_OF_BLOODBARK ? "Bloodbark" : "Swampbark";
			if (id == ItemId.RUNESCROLL_OF_BLOODBARK && player.getSettings().learnedBloodbark()
					|| id == ItemId.RUNESCROLL_OF_SWAMPBARK && player.getSettings().learnedSwampbark()) {
				player.getDialogueManager().start(new PlainChat(player, "There's nothing more you can learn from this scroll."));
				return;
			}
			player.getDialogueManager().start(new Dialogue(player) {
				@Override
				public void buildDialogue() {
					destroyItem(item, "Are you sure you wish to learn from this scroll?", "It will destroy the scroll in the process.").onYes(() -> readScroll(player, item, slotId));
				}
			});
		});
	}

	private final void readScroll(final Player player, final Item item, final int slotId) {
		final Inventory inventory = player.getInventory();
		final Item inSlot = inventory.getItem(slotId);
		if (inSlot != item) {
			return;
		}
		final int id = item.getId();
		final String name = id == ItemId.RUNESCROLL_OF_BLOODBARK ? "Bloodbark" : "Swampbark";
		final String altar = id == ItemId.RUNESCROLL_OF_BLOODBARK ? "Blood" : "Nature";
		player.lock(5);
		player.setAnimation(READ_ANIM);
		inventory.deleteItem(slotId, item);
		player.addAttribute(id == ItemId.RUNESCROLL_OF_BLOODBARK ? "bloodbark" : "swampbark", 1);
		player.sendMessage("As you close the scroll it starts to disintergrate in your hands.");
	}

	@Override
	public int[] getItems() {
		return new int[] {ItemId.RUNESCROLL_OF_BLOODBARK, ItemId.RUNESCROLL_OF_SWAMPBARK};
	}
}
