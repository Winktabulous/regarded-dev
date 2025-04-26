package com.zenyte.game.content.skills.construction.dialogue;

import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Kris | 28. nov 2017 : 4:03.16
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ClimbStaircaseD extends Dialogue {

	public ClimbStaircaseD(Player player, final WorldObject object) {
		super(player);
		this.object = object;
	}
	
	private final WorldObject object;

	@Override
	public void buildDialogue() {
		final String option = object.getDefinitions().getOption(1);
		if (option.equals("Climb-up")) {
			climbStaircase(true);
			return;
		} else if (option.equals("Climb-down")) {
			climbStaircase(false);
			return;
		}
		options(TITLE, "Climb up", "Climb down")
		.onOptionOne(() -> {
			finish();
			climbStaircase(true);
		})
		.onOptionTwo(() -> {
			finish();
			climbStaircase(false);
		});
	}
	
	private void climbStaircase(final boolean up) {
		if (up) {
			if (player.getPlane() == 2) {
				player.getDialogueManager().start(new PlainChat(player, "You cannot climb any higher than this."));
				return;
			}
			final Location tile = new Location(player.getX(), player.getY(), player.getPlane() + 1);
			final RoomReference ref = player.getConstruction().getReference(tile);
			final RoomReference reference = player.getConstruction().getReference(player.getLocation());
			if (ref == null) 
				player.getDialogueManager().start(new ClimbEmptyStaircaseD(player, reference, true, object));
			else 
				player.setLocation(tile);

		} else {
			if (player.getPlane() == 0) {
				player.getDialogueManager().start(new PlainChat(player, "You cannot climb any lower than this."));
				return;
			}
			final Location tile = new Location(player.getX(), player.getY(), player.getPlane() - 1);
			final RoomReference ref = player.getConstruction().getReference(tile);
			final RoomReference reference = player.getConstruction().getReference(player.getLocation());
			if (ref == null) 
				player.getDialogueManager().start(new ClimbEmptyStaircaseD(player, reference, false, object));
			else 
				player.setLocation(tile);
		}
	}

}
