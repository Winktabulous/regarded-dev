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

/**
 * @author Savions.
 */
public class ChargedIceMuphin implements ItemOnNPCAction {

	@Override public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
		if (!(npc instanceof Follower) || !Objects.equals(((Follower) npc).getOwner(), player)) {
			player.sendMessage("This is not your pet.");
			return;
		}
		if (player.getNumericAttribute("Muspah charged ice").intValue() > 0) {
			player.sendMessage("You've already used charged ice on your muphin pet. There's no need to do that any more.");
			return;
		}
		player.getDialogueManager().start(new ItemChat(player, item, "Congratulations! You've unlocked new metamorphosis options for your pet."));
		player.addAttribute("Muspah charged ice", 1);
		player.getInventory().deleteItem(item);
	}

	@Override public Object[] getItems() {
		return new Object[] {ItemId.CHARGED_ICE};
	}

	@Override public Object[] getObjects() {
		return new Object[] {NpcId.MUPHIN};
	}
}
