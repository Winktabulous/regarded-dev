package com.zenyte.game.world.object;

import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;

import java.util.HashMap;
import java.util.Map;

public enum DoorHandler {
	;

	private static final Map<WorldObject, WorldTask> runningDoors = new HashMap<>();

	public static void handleDoor(final WorldObject object) {
		handleDoor(object, 500);
	}

	public static void handleDoor(final WorldObject object, final int delay) {
		final boolean spawned = World.isSpawnedObject(object);
		final WorldObject door = Door.handleDoor(object);
		final WorldTask runningTask = runningDoors.remove(object);
		if (runningTask != null) {
			runningTask.stop();
		}
		if (!spawned) {
			WorldTask task = new WorldTask() {
				@Override
				public void run() {
					if (runningDoors.get(door) != this) {
						return;
					}
					Door.handleDoor(door);
					runningDoors.remove(door);
				}
			};
			WorldTasksManager.schedule(task, delay);
			runningDoors.put(door, task);
		}
	}

	public static void handleDoor(final WorldObject object, final int delay, final boolean isOpen) {
		final boolean spawned = World.isSpawnedObject(object);
		final WorldObject door = Door.handleDoor(object, isOpen);
		final WorldTask runningTask = runningDoors.remove(object);
		if (runningTask != null) {
			runningTask.stop();
		}
		if (!spawned) {
			WorldTask task = new WorldTask() {
				@Override
				public void run() {
					if (runningDoors.get(door) != this) {
						return;
					}
					Door.handleDoor(door);
					runningDoors.remove(door);
				}
			};
			WorldTasksManager.schedule(task, delay);
			runningDoors.put(door, task);
		}
	}
}
