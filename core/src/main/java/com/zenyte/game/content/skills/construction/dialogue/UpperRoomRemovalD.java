package com.zenyte.game.content.skills.construction.dialogue;

import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 28. nov 2017 : 23:07.54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class UpperRoomRemovalD extends Dialogue {

	public UpperRoomRemovalD(final Player player, final RoomReference ref) {
		super(player);
		this.ref = ref;
	}
	
	private final RoomReference ref;

	@Override
	public void buildDialogue() {
		plain("Are you sure you wish to remove the " + ref.getRoom().toString().toLowerCase() + " above?");
		options(TITLE, "Yes, remove it.", "No, don't remove it.")
		.onOptionOne(() -> {
			finish();
			player.getConstruction().getReferences().remove(ref);
			player.getConstruction().enterHouse(true, player.getConstruction().getRelationalSpawnTile());
		})
		.onOptionTwo(() -> finish());
	}

}
