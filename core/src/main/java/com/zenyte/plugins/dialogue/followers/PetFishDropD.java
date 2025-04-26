package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 3. nov 2017 : 1:41.41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 */
public class PetFishDropD extends Dialogue {

	public PetFishDropD(Player player, final Item item, final int slotId) {
		super(player);
		this.item = item;
		this.slotId = slotId;
	}

	private final Item item;
	private final int slotId;

	@Override
	public void buildDialogue() {
		plain("If you drop your fishbowl it will break!");
		options(TITLE, "Drop it regardless.", "Keep it.").onOptionOne(() -> {
			player.getInventory().deleteItem(slotId, item);
			finish();
		}).onOptionTwo(() -> finish());
	}

}
