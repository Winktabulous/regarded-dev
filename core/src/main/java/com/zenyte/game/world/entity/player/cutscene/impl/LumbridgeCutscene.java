package com.zenyte.game.world.entity.player.cutscene.impl;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.cutscene.Cutscene;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraLookAction;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraPositionAction;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraResetAction;
import com.zenyte.game.world.entity.player.cutscene.actions.FadeScreenAction;

/**
 * @author Kris | 4. dets 2017 : 14:52.45
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class LumbridgeCutscene extends Cutscene {

	@Override
	public void build() {
		player.lock();
		addActions(0, new FadeScreenAction(player, 2));
		addActions(2, () -> player.setLocation(new Location(3221, 3218, 0)));
		addActions(4, new CameraPositionAction(player, new Location(3225, 3225), 3000, (byte) 5, (byte) 10),
				new CameraLookAction(player, new Location(3215, 3220, 0), 0, (byte) 5, (byte) 10));
	
		addActions(8, new CameraPositionAction(player, new Location(3220, 3237), 3000, (byte) 5, (byte) 10),
				new CameraLookAction(player, new Location(3209, 3221, 0), 0, (byte) 5, (byte) 10));
		
		addActions(12, new CameraPositionAction(player, new Location(3206, 3239), 3000, (byte) 5, (byte) 10),
				new CameraLookAction(player, new Location(3207, 3221, 0), 0, (byte) 5, (byte) 10));
		
		addActions(16, new CameraPositionAction(player, new Location(3194, 3225), 3000, (byte) 5, (byte) 10),
				new CameraLookAction(player, new Location(3209, 3220, 0), 0, (byte) 5, (byte) 10));
	
		addActions(20, new CameraPositionAction(player, new Location(3192, 3208), 3000, (byte) 5, (byte) 10),
				new CameraLookAction(player, new Location(3209, 3214, 0), 0, (byte) 5, (byte) 10));
	
		addActions(24, new CameraPositionAction(player, new Location(3208, 3194), 3000, (byte) 5, (byte) 10),
				new CameraLookAction(player, new Location(3210, 3213, 0), 0, (byte) 5, (byte) 10));
	
		addActions(28, new CameraPositionAction(player, new Location(3222, 3205), 3000, (byte) 5, (byte) 10),
				new CameraLookAction(player, new Location(3211, 3216, 0), 0, (byte) 5, (byte) 10));
	
		addActions(32, new CameraPositionAction(player, new Location(3226, 3218), 3000, (byte) 5, (byte) 10),
				new CameraLookAction(player, new Location(3211, 3219, 0), 0, (byte) 5, (byte) 10));
		
		addActions(38, new CameraResetAction(player), () -> player.unlock());
	}

}
