package com.zenyte.game.content.skills.construction.dialogue;

import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.content.skills.construction.constants.RoomType;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 22. nov 2017 : 6:13.08
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class DungeonEnterD extends Dialogue {

	public DungeonEnterD(Player player, final RoomReference reference, final WorldObject object) {
		super(player);
		this.reference = reference;
		this.object = object;
	}
	
	private final RoomReference reference;
	private final WorldObject object;

	@Override
	public void buildDialogue() {
		final Location tile = new Location(player.getX(), player.getY(), player.getPlane() - 1);
		final RoomReference ref = player.getConstruction().getReference(tile);
		if (ref == null) 
			player.getDialogueManager().start(new ClimbEmptyStaircaseD(player, reference, reference.getRoom() == RoomType.SKILL_HALL || reference.getRoom() == RoomType.QUEST_HALL, object));
		else 
			player.setLocation(tile);
		/**options("Would you like to build a dungeon?",
				"Yes, build a dungeon.",
				"No, don't build a dungeon.")
		.onOptionOne(() -> {
			RoomReference dungeon = new RoomReference(Room.DUNGEON_STAIRS_ROOM, room.getX(), room.getY(), 0, 0);
			player.getConstruction().createRoom(dungeon);
			finish();
		});*/
	}

}
