package com.zenyte.plugins.dialogue;

import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * 
 * @author Tommeh 14 nov. 2017 : 13:35:58
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class RoomCreationD extends Dialogue {

	private final RoomReference reference;

	public RoomCreationD(Player player, RoomReference reference) {
		super(player);
		this.reference = reference;
	}

	@Override
	public void buildDialogue() {
		int rotation = reference.getRotation();
		player.getConstruction().roomPreview(reference, false);
		options(TITLE, "Rotate clockwise", "Rotate anticlockwise", "Build", "Cancel").onOptionOne(() -> {
			player.getConstruction().roomPreview(reference, true);
			reference.setRotation((reference.getRotation() + 1) & 0x3);
			player.getDialogueManager().start(new RoomCreationD(player, reference));
		}).onOptionTwo(() -> {
			player.getConstruction().roomPreview(reference, true);
			reference.setRotation((reference.getRotation() - 1) & 0x3);
			player.getDialogueManager().start(new RoomCreationD(player, reference));
		}).onOptionThree(() -> {
			player.getConstruction().createRoom(reference);
			finish();
		}).onOptionFour(() -> {
			reference.setRotation(rotation);
			player.getConstruction().roomPreview(reference, true);
			finish();
		});
		player.getTemporaryAttributes().put("CreatingRoom", reference);
	}

}
