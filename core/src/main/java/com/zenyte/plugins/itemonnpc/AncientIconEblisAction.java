package com.zenyte.plugins.itemonnpc;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Savions.
 */
public class AncientIconEblisAction implements ItemOnNPCAction {

	@Override public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
		player.getDialogueManager().start(new Dialogue(player) {
			@Override public void buildDialogue() {
				player("I found this weird icon. Do you know what it is?");
				item(ItemId.ANCIENT_ICON, "You show the icon to Eblis.");
				npc("Fascinating... I've never seen anything like this before, but the magic contained within is unmistakable. I wonder...");
				player("Wonder what?");
				npc("Stranger, if you were to bring me an Ancient staff, I believe it would be possible to combine it with this icon to create a powerful new weapon.");
				player("That sounds pretty neat.");
				if (!player.getInventory().containsItem(ItemId.ANCIENT_STAFF)) {
					npc("Then return to me when you have the staff.");
					return;
				}
				npc("I see you have the staff with you. Would you like me to combine your Ancient staff with the icon to create an Ancient sceptre? " +
						"You won't be able to separate them once this is done.");
				options("Select an option", "Yes please!", "No, thanks.").onOptionOne(() -> setKey(12)).onOptionTwo(() -> setKey(20));
				player(12, "Yes, please!");
				npc(13, "There you go. Wield its power responsibly, stranger...").executeAction(() -> {
					player.getInventory().ifDeleteItem(new Item(ItemId.ANCIENT_ICON, 1), () -> player.getInventory().addItem(new Item(ItemId.ANCIENT_SCEPTRE, 1)));
				});
				item(14, ItemId.ANCIENT_SCEPTRE, "You've been given an Ancient sceptre.");
				player(20, "No, thanks.");
				npc(21, "Suit yourself.");
			}
		});
	}

	@Override public Object[] getItems() {
		return new Object[] {ItemId.ANCIENT_ICON};
	}

	@Override public Object[] getObjects() {
		return new Object[] {NpcId.EBLIS, NpcId.EBLIS_689};
	}
}
