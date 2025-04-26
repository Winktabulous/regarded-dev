package com.zenyte.game.world.object;

import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

/**
 * @author Kris | 4. dets 2017 : 23:40.41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class TemporaryDoubleDoor {
	private static final Logger log = LoggerFactory.getLogger(TemporaryDoubleDoor.class);

	public static final WorldObject getNearbyDoor(final Player player, final WorldObject handledDoor) {
		if (handledDoor.getRotation() == 1 || handledDoor.getRotation() == 3) {
			WorldObject object = World.getObjectWithType(new Location(handledDoor.getX() - 1, handledDoor.getY(), handledDoor.getPlane()), handledDoor.getType());
			if (object == null || !WorldObjectUtils.getObjectNameOfPlayer(object, player).equals(WorldObjectUtils.getObjectNameOfPlayer(handledDoor, player))) {
				object = World.getObjectWithType(new Location(handledDoor.getX() + 1, handledDoor.getY(), handledDoor.getPlane()), handledDoor.getType());
			}
			return object;
		} else {
			WorldObject object = World.getObjectWithType(new Location(handledDoor.getX(), handledDoor.getY() - 1, handledDoor.getPlane()), handledDoor.getType());
			if (object == null || !WorldObjectUtils.getObjectNameOfPlayer(object, player).equals(WorldObjectUtils.getObjectNameOfPlayer(handledDoor, player))) {
				object = World.getObjectWithType(new Location(handledDoor.getX(), handledDoor.getY() + 1, handledDoor.getPlane()), handledDoor.getType());
			}
			return object;
		}
	}

	public static final WorldObject getNearbyDoor(final WorldObject handledDoor) {
		if (handledDoor.getRotation() == 1 || handledDoor.getRotation() == 3) {
			WorldObject object = World.getObjectWithType(new Location(handledDoor.getX() - 1, handledDoor.getY(), handledDoor.getPlane()), handledDoor.getType());
			if (object == null || !object.getName().equals(handledDoor.getName())) {
				object = World.getObjectWithType(new Location(handledDoor.getX() + 1, handledDoor.getY(), handledDoor.getPlane()), handledDoor.getType());
			}
			return object;
		} else {
			WorldObject object = World.getObjectWithType(new Location(handledDoor.getX(), handledDoor.getY() - 1, handledDoor.getPlane()), handledDoor.getType());
			if (object == null || !object.getName().equals(handledDoor.getName())) {
				object = World.getObjectWithType(new Location(handledDoor.getX(), handledDoor.getY() + 1, handledDoor.getPlane()), handledDoor.getType());
			}
			return object;
		}
	}

	public static void handleBarrowsDoubleDoor(final Player player, final WorldObject object) {
		handleBarrowsDoubleDoor(player, object, null);
	}

	public static void handleBarrowsDoubleDoor(final Player player, final WorldObject object, final Consumer<Location> callback) {
		final WorldObject nearbyDoor = getNearbyDoor(player, object);
		if (nearbyDoor == null) {
			return;
		}
		final Location walkLocation = getWalkLocation(player, object);
		player.lock();
		player.setFaceLocation(walkLocation);
		final WorldObject doorA = new WorldObject(object.getX() == nearbyDoor.getX() ? (object.getY() < nearbyDoor.getY() ? object : nearbyDoor) : (object.getX() < nearbyDoor.getX() ? nearbyDoor : object));
		final WorldObject doorB = new WorldObject(doorA.getPositionHash() == object.getPositionHash() ? nearbyDoor : object);
		final WorldObject leftDoor = object.getRotation() == 0 || object.getRotation() == 3 ? doorB : doorA;
		final WorldObject rightDoor = leftDoor == doorA ? doorB : doorA;
		object.setLocked(true);
		nearbyDoor.setLocked(true);
		if (callback != null) {
			callback.accept(walkLocation);
		}
		leftDoor.setRotation((leftDoor.getRotation() + 1) & 3);
		rightDoor.setRotation((rightDoor.getRotation() - 1) & 3);
		World.spawnGraphicalDoor(leftDoor);
		World.spawnGraphicalDoor(rightDoor);
		World.sendSoundEffect(object, !doorA.getDefinitions().containsOption("Open") ? Door.doorOpenSound : Door.doorCloseSound);
		WorldTasksManager.schedule(new WorldTask() {
			private int ticks;
			@Override
			public void run() {
				switch (ticks++) {
				case 0: 
					player.addWalkSteps(walkLocation.getX(), walkLocation.getY(), 1, false);
					break;
				case 1: 
					player.unlock();
					break;
				case 2: 
					World.sendSoundEffect(object, leftDoor.getDefinitions().containsOption("Open") ? Door.doorOpenSound : Door.doorCloseSound);
					World.spawnGraphicalDoor(object);
					World.spawnGraphicalDoor(nearbyDoor);
					break;
				case 3: 
					object.setLocked(false);
					nearbyDoor.setLocked(false);
					stop();
					break;
				}
			}
		}, 0, 0);
	}

	public static void executeBarrowsDoors(@NotNull final Player player, @NotNull final WorldObject object, @NotNull final Consumer<Location> callback) {
		if (object.isLocked()) {
			return;
		}
		final WorldObject nearbyDoor = getNearbyDoor(player, object);
		if (nearbyDoor == null) {
			return;
		}
		final Location walkLocation = getWalkLocation(player, object);
		player.lock(2);
		player.setFaceLocation(walkLocation);
		final WorldObject doorA = new WorldObject(object.getX() == nearbyDoor.getX() ? (object.getY() < nearbyDoor.getY() ? object : nearbyDoor) : (object.getX() < nearbyDoor.getX() ? nearbyDoor : object));
		final WorldObject doorB = new WorldObject(doorA.getPositionHash() == object.getPositionHash() ? nearbyDoor : object);
		final WorldObject leftDoor = object.getRotation() == 0 || object.getRotation() == 3 ? doorB : doorA;
		final WorldObject rightDoor = leftDoor == doorA ? doorB : doorA;
		object.setLocked(true);
		nearbyDoor.setLocked(true);
		try {
			callback.accept(walkLocation);
		} catch (Exception e) {
			log.error("", e);
		}
		leftDoor.setRotation((leftDoor.getRotation() + 1) & 3);
		rightDoor.setRotation((rightDoor.getRotation() - 1) & 3);
		World.spawnObject(leftDoor, false);
		World.spawnObject(rightDoor, false);
		World.sendSoundEffect(object, !doorA.getDefinitions().containsOption("Open") ? Door.doorOpenSound : Door.doorCloseSound);
		WorldTasksManager.schedule(new WorldTask() {
			private int ticks;
			@Override
			public void run() {
				switch (ticks++) {
				case 0: 
					player.addWalkSteps(walkLocation.getX(), walkLocation.getY(), 1, false);
					break;
				case 2: 
					World.sendSoundEffect(object, leftDoor.getDefinitions().containsOption("Open") ? Door.doorOpenSound : Door.doorCloseSound);
					World.spawnObject(object, false);
					World.spawnObject(nearbyDoor, false);
					break;
				case 3: 
					object.setLocked(false);
					nearbyDoor.setLocked(false);
					stop();
					break;
				}
			}
		}, 0, 0);
	}

	public static final void handleDoubleDoor(final Player player, final WorldObject object) {
		final WorldObject nearbyDoor = getNearbyDoor(player, object);
		if (nearbyDoor == null) {
			return;
		}
		player.lock();
		final WorldObject doorA = new WorldObject(object.getX() == nearbyDoor.getX() ? (object.getY() < nearbyDoor.getY() ? object : nearbyDoor) : (object.getX() < nearbyDoor.getX() ? nearbyDoor : object));
		final WorldObject doorB = new WorldObject(doorA.getPositionHash() == object.getPositionHash() ? nearbyDoor : object);
		final WorldObject leftDoor = object.getRotation() == 0 || object.getRotation() == 3 ? doorB : doorA;
		final WorldObject rightDoor = leftDoor == doorA ? doorB : doorA;
		object.setLocked(true);
		nearbyDoor.setLocked(true);
		final byte[][] offsets = DoubleDoor.OFFSETS[object.getRotation()];
		final byte[] leftOffsets = offsets[0];
		final byte[] rightOffsets = offsets[1];
		WorldTasksManager.schedule(new WorldTask() {
			private Location walkLocation;
			private int ticks;
			@Override
			public void run() {
				switch (ticks++) {
				case 0: 
					//if (player.getX() != walkLocation.getX() && player.getY() != walkLocation.getY()) {
					player.addWalkSteps(object.getX(), object.getY());
					//	}
					leftDoor.setRotation(leftOffsets[2] & 3);
					rightDoor.setRotation(rightOffsets[2] & 3);
					leftDoor.moveLocation(leftOffsets[0], leftOffsets[1], 0);
					rightDoor.moveLocation(rightOffsets[0], rightOffsets[1], 0);
					World.spawnGraphicalDoor(leftDoor);
					World.spawnGraphicalDoor(rightDoor);
					World.removeGraphicalDoor(object);
					World.removeGraphicalDoor(nearbyDoor);
					break;
				case 1: 
					walkLocation = getWalkLocation(player, object);
					player.addWalkSteps(walkLocation.getX(), walkLocation.getY(), 1, false);
					break;
				case 3: 
					World.spawnGraphicalDoor(object);
					World.spawnGraphicalDoor(nearbyDoor);
					World.removeGraphicalDoor(leftDoor);
					World.removeGraphicalDoor(rightDoor);
					player.unlock();
					break;
				case 4: 
					object.setLocked(false);
					nearbyDoor.setLocked(false);
					stop();
					break;
				}
			}
		}, 0, 0);
	}

	private static final Location getWalkLocation(final Player player, final WorldObject object) {
		if (player.getLocation().getPositionHash() != object.getPositionHash() && (player.isProjectileClipped(object, false))) {
			return object;
		}
		switch (object.getRotation()) {
		case 0: 
			return new Location(player.getX() - 1, player.getY());
		case 1: 
			return new Location(player.getX(), player.getY() + 1);
		case 2: 
			return new Location(player.getX() + 1, player.getY());
		default: 
			return new Location(player.getX(), player.getY() - 1);
		}
	}
}
