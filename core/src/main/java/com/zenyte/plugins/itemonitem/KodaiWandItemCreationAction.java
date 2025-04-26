package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.BossDropItem;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 27 mei 2018 | 01:05:41
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class KodaiWandItemCreationAction implements ItemOnItemAction {

	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		final BossDropItem item = BossDropItem.getItemByMaterials(from, to);
		player.getDialogueManager().start(new Dialogue(player) {
			@Override
			public void buildDialogue() {
				plain("Warning: This process will combine your Master wand with the<br>Kodai insignia. This cannot be reverted.");
				options("Do you wish to go ahead?", "Proceed", "Cancel")
					.onOptionOne(() -> { 
						for (final Item material : item.getMaterials()) {
							player.getInventory().deleteItem(material);
						}
						player.getInventory().addItem(item.getItem());
						player.sendMessage("Power shoots down the length of the wand, empowering it with the darkness of Xeric's Kodai wizards.");
				});
			}
		});
	}

	@Override
	public int[] getItems() {
		return new int[] { 6914, 21043 };
	}

}
