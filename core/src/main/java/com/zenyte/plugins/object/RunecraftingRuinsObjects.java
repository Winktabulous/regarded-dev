package com.zenyte.plugins.object;

import com.zenyte.game.content.achievementdiary.diaries.WildernessDiary;
import com.zenyte.game.content.skills.runecrafting.Runecrafting;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

import java.util.ArrayList;

/**
 * @author Tommeh | 3 okt. 2018 | 21:50:40
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public final class RunecraftingRuinsObjects implements ObjectAction {
	@Override
	public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
		final Runecrafting rune = Runecrafting.getRuneByRuinsObject(object.getId());
		if (rune == null) {
			return;
		}
		if (rune.equals(Runecrafting.CHAOS_RUNE)) {
			player.getAchievementDiaries().update(WildernessDiary.ENTER_CHAOS_RUNECRAFTING_TEMPLE);
		}
		player.setLocation(rune.getPortalCoords());
	}

	@Override
	public Object[] getObjects() {
		final ArrayList<Object> list = new ArrayList<Object>();
		for (final Runecrafting r : Runecrafting.VALUES) {
			if (r.getRuinsObjectId() == -1) {
				continue;
			}
			list.add(r.getRuinsObjectId());
		}
		return list.toArray(new Object[list.size()]);
	}
}
