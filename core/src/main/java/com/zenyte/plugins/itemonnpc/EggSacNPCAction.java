package com.zenyte.plugins.itemonnpc;

import com.zenyte.game.content.follower.Follower;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.ItemChat;

import java.util.Objects;

public class EggSacNPCAction implements ItemOnNPCAction {

	@Override
	public void handleItemOnNPCAction(final Player player, final Item item, final int slot, final NPC npc) {
		if (!(npc instanceof Follower) || !Objects.equals(((Follower) npc).getOwner(), player)) {
			player.sendMessage("This is not your pet.");
			return;
		}
		switch (item.getId()) {
			case ItemId.BLUE_EGG_SAC: {
				useItemOn(player, item, "In a scene so disgusting the artists refused to animate it, Sraracha brutally devours the egg sac. It can now turn blue on command.", "sraracha_blue");
				break;
			}
			case ItemId.ORANGE_EGG_SAC: {
				useItemOn(player, item, "In a scene so disgusting the artists refused to animate it, Sraracha brutally devours the egg sac. It can now turn orange on command.", "sraracha_orange");
				break;
			}
		}
	}

	private static final void useItemOn(Player player, Item item, String message, String attribute) {
		if (player.getNumericAttribute(attribute).intValue() > 0) {
			player.sendMessage("You've used this kind of sac on the Sraracha.");
			return;
		}

		player.getDialogueManager().start(new ItemChat(player, item, message));
		player.addAttribute(attribute, 1);
		player.getInventory().deleteItem(item);
	}

	@Override
	public Object[] getItems() {
		return new Object[] { ItemId.ORANGE_EGG_SAC, ItemId.BLUE_EGG_SAC };
	}

	@Override
	public Object[] getObjects() {
		return new Object[] { NpcId.SRARACHA_2144, NpcId.SRARACHA_11159, NpcId.SRARACHA_11160 };
	}

}