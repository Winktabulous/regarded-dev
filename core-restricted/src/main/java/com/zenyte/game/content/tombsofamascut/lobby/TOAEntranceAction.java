package com.zenyte.game.content.tombsofamascut.lobby;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Savions.
 */
public class TOAEntranceAction implements ObjectAction {

	private static final Location OUTSIDE_LOCATION = new Location(3357, 2713);
	private static final Location INSIDE_LOCATION = new Location(3359, 9128);

	@Override public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		final FadeScreen screen = new FadeScreen(player, () -> {
			player.setLocation(object.getId() == 46087 ? OUTSIDE_LOCATION : INSIDE_LOCATION);
			player.faceDirection(object.getId() == 46087 ? Direction.NORTH_WEST : Direction.SOUTH);
		});
		screen.fade();
		WorldTasksManager.schedule(screen::unfade, 2);
	}

	@Override public Object[] getObjects() {
		return new Object[] {46087, 44596};
	}
}
