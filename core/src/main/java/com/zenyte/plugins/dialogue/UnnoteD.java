package com.zenyte.plugins.dialogue;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.GameSetting;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 17 feb. 2018 : 20:04:28
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class UnnoteD extends Dialogue {
	private final Item item;

	public UnnoteD(final Player player, final Item item) {
		super(player);
		this.item = item;
	}

	@Override
	public void buildDialogue() {
		if (player.getNumericAttribute(GameSetting.CONFIRMATION_WHEN_NOTING_OR_UNNOTING.toString()).intValue() == 1) {
			options("Un-note the banknote" + (item.getAmount() > 1 ? "s?" : "?"), "Yes", "No").onOptionOne(this::unnote);
		} else {
			unnote();
		}
	}

	private void unnote() {
		final int amount = player.getInventory().getFreeSlots();
		if (item.getAmount() == 1) {
			final int succeeded = player.getInventory().deleteItem(item).getSucceededAmount();
			player.getInventory().addOrDrop(new Item(item.getDefinitions().getNotedId(), succeeded));
			player.getDialogueManager().start(new PlainChat(player, amount == 0 ? "Nothing was un-noted since you have no empty space in your inventory." : "The bank exchanges your banknote(s) for item(s)."));
			return;
		}
		final int succeeded = player.getInventory().deleteItem(item.getId(), amount).getSucceededAmount();
		player.getInventory().addOrDrop(new Item(item.getDefinitions().getNotedId(), succeeded));
		player.getDialogueManager().start(new PlainChat(player, amount == 0 ? "Nothing was un-noted since you have no empty space in your inventory." : "The bank exchanges your banknote(s) for item(s)."));
	}
}
