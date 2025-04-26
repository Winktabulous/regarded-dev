package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Setting;
import com.zenyte.game.world.entity.player.Settings;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Kris | 25. aug 2018 : 22:37:05
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>}
 */
public class PrayerScroll extends ItemPlugin {
	private static final Animation READ_ANIM = new Animation(7403);

	@Override
	public void handle() {
		bind("Read", (player, item, slotId) -> {
			final int id = item.getId();
			final Settings settings = player.getSettings();
			final String name = id == 21079 ? "Augury" : id == 21034 ? "Rigour" : "Preserve";
			if (id == 21079 && settings.isAugury() || id == 21047 && settings.isPreserve() || id == 21034 && settings.isRigour()) {
				player.getDialogueManager().start(new PlainChat(player, "You can make out some faded words on the " +
                        "ancient parchment. It appears to be an archaic invocation of the gods. However there's " +
                        "nothing more for you to learn."));
				return;
			}
			player.getDialogueManager().start(new Dialogue(player) {
				@Override
				public void buildDialogue() {
					item(item, "You can make out some faded words on the ancient parchment. It appears to be an archaic invocation of the gods! Would you like to absorb its power?");
					options("This will consume the scroll.", "Learn " + name, "Cancel").onOptionOne(() -> readScroll(player, item, slotId));
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
		final String name = id == 21079 ? "Augury" : id == 21034 ? "Rigour" : "Preserve";
		final Setting setting = id == 21079 ? Setting.AUGURY : id == 21034 ? Setting.RIGOUR : Setting.PRESERVE;
		player.lock(5);
		player.setAnimation(READ_ANIM);
		inventory.deleteItem(slotId, item);
		player.getSettings().setSetting(setting, 1);
		player.getDialogueManager().start(new ItemChat(player, item, "You study the scroll and learn a new prayer: <col=FF0040>" + name + "</col>"));
	}

	@Override
	public int[] getItems() {
		return new int[] {21079, 21047, 21034};
	}
}
