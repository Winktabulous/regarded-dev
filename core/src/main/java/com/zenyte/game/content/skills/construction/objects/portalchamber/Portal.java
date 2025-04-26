package com.zenyte.game.content.skills.construction.objects.portalchamber;

import com.zenyte.game.content.achievementdiary.diaries.MorytaniaDiary;
import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.content.skills.construction.TeleportPortal;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

import java.util.ArrayList;

/**
 * @author Kris | 25. veebr 2018 : 22:58.14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 * TODO: Restrictions for leaving through portal.
 */
public final class Portal implements ObjectInteraction {
	@Override
	public Object[] getObjects() {
		final ArrayList<Object> list = new ArrayList<Object>(TeleportPortal.VALUES.length * 3);
		for (final TeleportPortal portal : TeleportPortal.VALUES) {
			for (int i : portal.getPortalIds()) list.add(i);
		}
		return list.toArray(new Object[list.size()]);
	}

	@Override
	public void handleObjectAction(Player player, Construction construction, RoomReference reference, WorldObject object, int optionId, String option) {
		if (option.equals("enter")) {
			final TeleportPortal portal = TeleportPortal.MAP.get(object.getId());
			if (portal == null) {
				return;
			}
			if (portal.equals(TeleportPortal.KHARYRLL_PORTAL)) {
				player.getAchievementDiaries().update(MorytaniaDiary.ENTER_KHARYRLL_PORTAL);
			}
			player.setLocation(portal.getTeleport().getDestination());
		}
	}
}
