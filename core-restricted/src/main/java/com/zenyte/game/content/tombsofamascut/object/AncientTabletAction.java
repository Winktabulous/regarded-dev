package com.zenyte.game.content.tombsofamascut.object;

import com.zenyte.game.content.tombsofamascut.encounter.ScabarasEncounter;
import com.zenyte.game.content.tombsofamascut.raid.ScabarasPuzzleType;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Savions.
 */
public class AncientTabletAction implements ObjectAction {

	private static final SoundEffect SOUND_EFFECT = new SoundEffect(6548);

	@Override public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		if (player.getArea() instanceof final ScabarasEncounter encounter &&
				encounter.canUse(player, ScabarasPuzzleType.SUM, "You can't seem to make out the writing. Weird.")) {
			player.sendMessage("The number <col=ef1020>" + encounter.getSumGoal() + "</col> has been hastily chipped into the stone.");
			player.sendSound(SOUND_EFFECT);
		}
	}

	@Override public Object[] getObjects() {
		return new Object[] {ScabarasEncounter.SUM_TABLET_ID};
	}
}
