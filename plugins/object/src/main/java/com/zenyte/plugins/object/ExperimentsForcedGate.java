package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.ForcedGate;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

import java.util.Optional;


public class ExperimentsForcedGate implements ObjectAction {

	@Override
	public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
		new ForcedGate<>(player, object).handle(Optional.empty());
	}

	@Override
	public Object[] getObjects() {
		return new Object[] { 5170};
	}
}