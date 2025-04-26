package com.zenyte.game.content.area.strongholdofsecurity;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import mgi.utilities.CollectionUtils;

import java.util.function.Predicate;

/**
 * @author Kris | 4. sept 2018 : 21:35:25
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class SOSPortal implements ObjectAction {

	private enum Portal {
		FIRST_FLOOR(20786, new Location(1914, 5222, 0), player -> player.getBooleanAttribute("sos first claimed")), SECOND_FLOOR(19005, new Location(2021, 5223, 0), player -> player.getBooleanAttribute("sos second claimed")), THIRD_FLOOR(23707, new Location(2146, 5287, 0), player -> player.getBooleanAttribute("sos third claimed")), FOURTH_FLOOR(23922, new Location(2341, 5219, 0), player -> player.getBooleanAttribute("sos fourth claimed"));
		private final int id;
		private final Location destination;
		private final Predicate<Player> predicate;
		private static final Portal[] VALUES = values();

		Portal(int id, Location destination, Predicate<Player> predicate) {
			this.id = id;
			this.destination = destination;
			this.predicate = predicate;
		}
	}

	@Override
	public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
		final SOSPortal.Portal portal = CollectionUtils.findMatching(Portal.VALUES, p -> p.id == object.getId());
		if (portal == null) {
			throw new RuntimeException("Unable to locate a portal for object " + object);
		}
		if (!portal.predicate.test(player)) {
			player.sendMessage("You must have completed this level to take this shortcut.");
			return;
		}
		player.setLocation(portal.destination);
		player.sendMessage("You enter the portal to be whisked through to the treasure room.");
	}

	@Override
	public Object[] getObjects() {
		final IntArrayList list = new IntArrayList();
		for (final SOSPortal.Portal portal : Portal.VALUES) {
			list.add(portal.id);
		}
		return list.toArray();
	}
}
