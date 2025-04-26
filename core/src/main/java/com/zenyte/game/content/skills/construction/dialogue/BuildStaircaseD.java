package com.zenyte.game.content.skills.construction.dialogue;

import com.zenyte.game.content.skills.construction.FurnitureData;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 28. nov 2017 : 1:51.23
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class BuildStaircaseD extends Dialogue {

	public BuildStaircaseD(Player player, final WorldObject object, final RoomReference reference, final FurnitureData data, final int slotId) {
		super(player);
		this.object = object;
		this.reference = reference;
		this.data = data;
		this.slotId = slotId;
	}
	
	private final WorldObject object;
	private final RoomReference reference;
	private final FurnitureData data;
	private final int slotId;

	@Override
	public void buildDialogue() {
		options("Build stairs in which direction?", "Up", "Down")
		.onOptionOne(() -> {
			finish();
			player.getConstruction().buildStaircase(true, object, reference, data, slotId);
		})
		.onOptionTwo(() -> {
			finish();
			player.getConstruction().buildStaircase(false, object, reference, data, slotId);
		});
	}

}
