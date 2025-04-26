package com.zenyte.plugins.dialogue;

import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 17 nov. 2017 : 17:33:44
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class RoomRemovingD extends Dialogue {

	private final RoomReference reference;

	public RoomRemovingD(Player player, RoomReference reference) {
		super(player);
		this.reference = reference;
	}

	@Override
	public void buildDialogue() {
		if (player.getPlane() == 1 && player.getConstruction().getReference(reference.getX(), reference.getY(), 2) != null) {
			plain("You can't remove a room supporting another room.");
			return;
		}
		options("Remove the " + reference.getRoom().toString().toLowerCase() + "?", "Yes", "No").onOptionOne(() -> {
			player.getConstruction().removeRoom(reference);
			finish();
		}).onOptionTwo(() -> finish());
	}

}
