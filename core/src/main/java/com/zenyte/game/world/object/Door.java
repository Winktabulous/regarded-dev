package com.zenyte.game.world.object;

import com.zenyte.game.world.DefaultGson;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

public enum Door {
	;

	private static final Logger log = LoggerFactory.getLogger(Door.class);
	public static final SoundEffect doorOpenSound = new SoundEffect(81, 5, 0);
	public static final SoundEffect doorCloseSound = new SoundEffect(58, 5, 0);
	private static final Int2IntOpenHashMap map = new Int2IntOpenHashMap(500);
	private static final int X = 0;
	private static final int Y = 1;
	private static final int[][] COORD_OFFSETS = new int[][] {new int[] {-1, 0}, new int[] {0, 1}, new int[] {1, 0}, new int[] {0, -1}};
	private static final Map<WorldObject, WorldObject> interactedDoors = new Object2ObjectOpenHashMap<>();

	static WorldObject handleDoor(final WorldObject door, final WorldObject interacted, final boolean open) {
		final int doorId = door.getId();
		final int type = door.getType();
		final int rotation = door.getRotation();
		final int nextRotation = open ? ((rotation + 1) & 3) : rotation;
		final int[] offsets = COORD_OFFSETS[type == 9 ? ((nextRotation + 1) & 3) : nextRotation];
		final Location location = new Location(door.getX() + offsets[X], door.getY() + offsets[Y], door.getPlane());
		final int id = interacted == null ? map.getOrDefault(doorId, doorId) : interacted.getId();
		final WorldObject object = new WorldObject(id, type, open ? ((rotation - 1) & 3) : ((rotation + 1) & 3), location);
		World.removeObject(door);
		World.spawnObject(object);
		World.sendSoundEffect(door, open ? doorOpenSound : doorCloseSound);
		if (interacted == null) {
			interactedDoors.put(object, door);
		}
		return object;
	}

	public static WorldObject handleDoor(final WorldObject door, final boolean isOpen) {
		final WorldObject interacted = interactedDoors.remove(door);
		return handleDoor(door, interacted, isOpen);
	}

	static WorldObject handleDoor(final WorldObject door) {
		final WorldObject interacted = interactedDoors.remove(door);
		return handleDoor(door, interacted, interacted == null ? door.getDefinitions().containsOption("Close") : interacted.getDefinitions().containsOption("Open"));
	}

	public static WorldObject handleGraphicalDoor(final WorldObject door, final WorldObject original) {
		final int doorId = door.getId();
		final boolean open = original != null ? original.getDefinitions().containsOption("Open") : door.getDefinitions().containsOption("Close");
		final int type = door.getType();
		final int rotation = door.getRotation();
		final int nextRotation = open ? ((rotation + 1) & 3) : rotation;
		final int[] offsets = COORD_OFFSETS[type == 9 ? ((nextRotation + 1) & 3) : nextRotation];
		final Location location = new Location(door.getX() + offsets[X], door.getY() + offsets[Y], door.getPlane());
		final int id = map.getOrDefault(doorId, doorId);
		final WorldObject object = new WorldObject(id, type, open ? ((rotation - 1) & 3) : ((rotation + 1) & 3), location);
		World.sendSoundEffect(door, open ? doorOpenSound : doorCloseSound);
		if (original == null) {
			World.spawnGraphicalDoor(object);
			World.removeGraphicalDoor(door);
		} else {
			World.spawnGraphicalDoor(original);
			World.removeGraphicalDoor(door);
		}
		return object;
	}

	public static void load() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("cache/data/objects/Door definitions.json"));
			final Int2IntOpenHashMap map = DefaultGson.getGson().fromJson(br, Int2IntOpenHashMap.class);
			Door.map.putAll(map);
		} catch (final FileNotFoundException e) {
			log.error("", e);
		}
	}
}
