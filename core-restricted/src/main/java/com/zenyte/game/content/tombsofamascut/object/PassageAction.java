package com.zenyte.game.content.tombsofamascut.object;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.content.tombsofamascut.encounter.ScabarasEncounter;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Savions.
 */
public class PassageAction implements Shortcut {

	private static final Location NORTH_LOCATION = new Location(3548, 5284);
	private static final Location SOUTH_LOCATION = new Location(3548, 5276);
	private static final Animation ANIM = new Animation(2796);
	private static final SoundEffect SOUND_EFFECT = new SoundEffect(2454, 1, 4, 2);

	@Override public void startSuccess(Player player, WorldObject object) {
		if (player.getArea() instanceof final ScabarasEncounter encounter) {
			player.setAnimation(ANIM);
			player.sendSound(SOUND_EFFECT);
			WorldTasksManager.schedule(() -> {
				player.setAnimation(Animation.STOP);
				player.setLocation(encounter.getLocation(player.getY() < object.getY() ? NORTH_LOCATION : SOUTH_LOCATION));
			}, 1);
		}
	}

	@Override public int getLevel(WorldObject object) {
		return 0;
	}

	@Override public int[] getObjectIds() {
		return new int[] {45343};
	}

	@Override public int getDuration(boolean success, WorldObject object) {
		return 2;
	}

	@Override public double getSuccessXp(WorldObject object) {
		return 0;
	}

	@Override public Location getRouteEvent(Player player, WorldObject object) {
		if (player.getArea() instanceof final ScabarasEncounter encounter) {
			return encounter.getLocation(player.getY() < encounter.getY(NORTH_LOCATION.getY()) ? SOUTH_LOCATION : NORTH_LOCATION);
		}
		return player.getLocation();
	}
}
