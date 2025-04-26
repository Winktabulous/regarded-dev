package com.zenyte.plugins.object;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 10 jan. 2018 : 18:46:16
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public final class WildernessDitchObject implements ObjectAction {
	private static final Animation ANIM = new Animation(6132);
	private static final SoundEffect sound = new SoundEffect(2462, 0, 25);

	@Override
	public int getDelay() {
		return 1;
	}

	@Override
	public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
		if (player.getY() > 3521 || player.getTemporaryAttributes().get("EnteredWildernessDitch") != null) {
			jump(player, object);
		} else {
			player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 475);
			player.getPacketDispatcher().sendComponentText(475, 10, "If you go any further you will enter the Wilderness. This is a very dangerous area where other players can attack you!<br><br>The further north you go the more dangerous it becomes. An indicator at the bottom-right of the screen will show the current level of danger.");
			player.getPacketDispatcher().sendComponentVisibility(475, 13, true);
			player.getTemporaryAttributes().put("WildernessDitch", object);
		}
	}

	public static final void jump(@NotNull final Player player, @NotNull final WorldObject object) {
		player.stopAll();
		player.faceObject(object);
		player.setAnimation(ANIM);
		player.sendSound(sound);
		final Direction direction = ((object.getRotation() & 1) != 0) ? (player.getX() > object.getX()) ? Direction.WEST : Direction.EAST : (player.getY() > object.getY()) ? Direction.SOUTH : Direction.NORTH;
		player.lock(2);
		WorldTasksManager.schedule(() -> player.autoForceMovement(player.getLocation().transform(direction, 3), 30));
	}

	@Override
	public Object[] getObjects() {
		return new Object[] {"Wilderness ditch"};
	}
}
