package com.zenyte.game.content.sailing;

import com.zenyte.game.model.MinimapState;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.utils.TimeUnit;
import mgi.types.config.AnimationDefinitions;

/**
 * @author Kris | 22. sept 2018 : 22:04:49
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public class FremennikIslesSailing {
	private static final int INTERFACE_ID = 224;
	private static final int SHIP_COMPONENT_ID = 7;


	public enum SailingDestination {
		MISCELLANIA(1372, 1373, "Miscellania", new Location(2631, 3693, 0), new Location(2578, 3844, 0)), WATERBIRTH_ISLAND(2344, 2345, "Waterbirth Island", new Location(2620, 3686, 0), new Location(2546, 3758, 0)), NEITIZNOT(5764, 5765, "Neitiznot", new Location(2646, 3709, 0), new Location(2310, 3783, 0)), JATIZSO(5766, 5767, "Jatizso", new Location(2646, 3709, 0), new Location(2419, 3781, 0));
		private final int toAnimation;
		private final int fromAnimation;
		private final String areaName;
		private final Location rellekka;
		private final Location destination;

		SailingDestination(int toAnimation, int fromAnimation, String areaName, Location rellekka, Location destination) {
			this.toAnimation = toAnimation;
			this.fromAnimation = fromAnimation;
			this.areaName = areaName;
			this.rellekka = rellekka;
			this.destination = destination;
		}
	}

	/**
	 * Sails the player to the requested location.
	 * 
	 * @param player
	 *            the player who is sailing.
	 * @param destination
	 *            the destination.
	 */
	public static final void sail(final Player player, final SailingDestination destination) {
		final boolean atDestination = player.inArea(destination.areaName);
		player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, INTERFACE_ID);
		final int animation = atDestination ? destination.fromAnimation : destination.toAnimation;
		player.getPacketDispatcher().sendComponentAnimation(INTERFACE_ID, SHIP_COMPONENT_ID, animation);
		final int duration = (int) TimeUnit.MILLISECONDS.toTicks(AnimationDefinitions.get(animation).getDuration());
		player.setLocation(atDestination ? destination.rellekka : destination.destination);
		player.getPacketDispatcher().sendMinimapState(MinimapState.MAP_DISABLED);
		player.lock();
		WorldTasksManager.schedule(() -> player.getDialogueManager().start(new Dialogue(player) {
			@Override
			public void buildDialogue() {
				player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
				player.getPacketDispatcher().sendMinimapState(MinimapState.ENABLED);
				player.unlock();
				plain("The ship arrives at " + (atDestination ? "Rellekka" : destination.areaName) + ".");
			}
		}), duration);
	}
}
