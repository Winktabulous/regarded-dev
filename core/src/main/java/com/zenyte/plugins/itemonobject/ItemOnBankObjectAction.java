package com.zenyte.plugins.itemonobject;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.CurrencyConversionD;
import com.zenyte.plugins.dialogue.NoteD;
import com.zenyte.plugins.dialogue.PlainChat;
import com.zenyte.plugins.dialogue.UnnoteD;

/**
 * @author Tommeh | 17 feb. 2018 : 19:30:44
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class ItemOnBankObjectAction implements ItemOnObjectAction {

	@Override
	public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {

		// Open chest's that aren't banks can't be used for noting
		if (object.getName().equals("Open chest") && !object.getDefinitions().containsOption("Bank"))
			return;

		if (item.getId() == 995 || item.getId() == 13204) {
			if (item.getId() == 995 && item.getAmount() < 1000) {
				player.sendMessage("You need at least 1,000 coins to convert the coins into platinum token(s).");
				return;
			}
			player.getDialogueManager().start(new CurrencyConversionD(player, item, slot));
			return;
		}
		if (item.getDefinitions().isNoted()) {
			player.getDialogueManager().start(new UnnoteD(player, item));
		} else {
			if (item.getDefinitions().getNotedId() == -1) {
				player.getDialogueManager().start(new PlainChat(player, "You cannot turn this into banknotes, try another item."));
				return;
			}
			player.getDialogueManager().start(new NoteD(player, item));
		}
	}

	@Override
	public Object[] getItems() {
		return null;
	}

	@Override
	public Object[] getObjects() {
		return new Object[] { "Bank", "Bank booth", "Bank chest", "Open chest", "Grand Exchange booth", "Bank counter", "Deposit box", "Bank deposit box", 12309, 12308};
	}
	

}
