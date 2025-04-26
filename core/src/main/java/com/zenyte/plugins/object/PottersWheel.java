package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.skills.PotteryShapingD;

public class PottersWheel implements ObjectAction {

	@Override
	public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		player.getDialogueManager().start(new PotteryShapingD(player));
	}

	@Override
	public Object[] getObjects() {
		return new Object[] { ObjectId.POTTERS_WHEEL };
	}

}
