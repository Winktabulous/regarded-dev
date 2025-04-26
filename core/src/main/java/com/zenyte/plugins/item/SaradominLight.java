package com.zenyte.plugins.item;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.Setting;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 25. aug 2018 : 22:44:48
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class SaradominLight extends ItemPlugin {

	@Override
	public void handle() {
		bind("Consume", (player, item, slotId) -> {
			if (player.getBooleanAttribute(Setting.SARADOMIN_LIGHT.toString())) {
				player.sendMessage("Your mind is already enlightened by saradomin's light.");
				return;
			}
			player.getDialogueManager().start(new Dialogue(player) {
				@Override
				public void buildDialogue() {
					item(item, "As you commune with the holy star, you feel the light of Saradomin preparing to fill your mind and enlighten your vision. If you submit, it will help you see through the darkness of Zamorak's evil. ");
					options("Submit to the light of Saradomin?", "Yes, let the light in.", "No, cancel.").onOptionOne(() -> {
						item(item, "You submit to the light of Saradomin.<br> Zamorak's darkness will henceforth have no effect on you.");
						player.getPacketDispatcher().sendClientScript(1068);
						player.getSettings().toggleSetting(Setting.SARADOMIN_LIGHT);
						player.getInventory().deleteItem(item);
						finish();
					}).onOptionTwo(() -> finish());
				}
			});
		});
	}

	@Override
	public int[] getItems() {
		return new int[] { 13256 };
	}

}
