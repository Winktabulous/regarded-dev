package com.zenyte.game.world.object;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

import java.util.HashMap;
import java.util.Map;

public class DoubleDoor {

	/**
	 * A concurrent map of currently opened doors.
	 */
	private static final Map<WorldObject, DoubleDoor> HANDLED_DOORS = new HashMap<WorldObject, DoubleDoor>();
	
	private final WorldObject mapLeftDoor, mapRightDoor;
	private final WorldObject leftdoor, rightDoor;
	
	private static final Int2IntOpenHashMap map = new Int2IntOpenHashMap();
	
	/** Temporary solution until we match them all and load externally. */
	static {
		add(14233, 14234);
		add(14235, 14236);
		add(14237, 14238);
		add(14239, 14240);
		add(14241, 14242);
		add(14243, 14244);
		add(14245, 14246);
		add(14247, 14248);
		add(10262, 10263);
		add(10264, 10265);
		add(35009, 35011);
		add(35010, 35012);
		add(28460, 28464);
		add(28461, 28465);
		add(28462, 28466);
		add(28463, 28467);
		add(28456, 28468);
		add(28457, 28469);
		add(28458, 28470);
		add(28459, 28471);
		add(33572, 28470);
		add(33570, 28467);
		add(4423, 4425);
		add(4424, 4426);
        add(4427, 4429);
        add(4428, 4430);
		add(5183, 5187);
		add(5186, 5188);
		add(24059, 24063);
		add(24064, 24061);
	}
	
	private static final void add(final int key, final int value) {
		map.put(key, value);
		map.put(value, key);
	}
	
	public DoubleDoor(final WorldObject mapLeftDoor, final WorldObject mapRightDoor,
			final WorldObject leftDoor, final WorldObject rightDoor) {
		this.mapLeftDoor = mapLeftDoor;
		this.mapRightDoor = mapRightDoor;
		leftdoor = leftDoor;
		this.rightDoor = rightDoor;
	}
	
	/**
	 * An array based on the original object rotation.
	 * First index = original object rotation,
	 * Second index array contains two arrays for left and right door respectively.
	 * In the arrays, you have x offset, y offset and new rotation for the door.
	 */
	public static final byte[][][] OFFSETS = new byte[][][] {
		new byte[][] {
			new byte[] { -1, 0, 1 },
			new byte[] { -1, 0, 3 }
		}, 
		new byte[][] {
			new byte[] { 0, 1, 2 },
			new byte[] { 0, 1, 0 }
		}, 
		new byte[][] {
			new byte[] { 1, 0, 3 },
			new byte[] { 1, 0, 1 }
		},
		new byte[][] {
			new byte[] { 0, -1, 0},
			new byte[] { 0, -1, 2 },
		}
	};
	
	public static final void handleDoubleDoor(final Player player, final WorldObject object) {
		final DoubleDoor obj = HANDLED_DOORS.get(object);
		if (obj != null) {
			final WorldObject leftDoor = obj.leftdoor;
			final WorldObject rightDoor = obj.rightDoor;
			final WorldObject mapLeftDoor = obj.mapLeftDoor;
			final WorldObject mapRightDoor = obj.mapRightDoor;
            World.sendSoundEffect(object, leftDoor.getDefinitions().containsOption("Open") ? Door.doorOpenSound : Door.doorCloseSound);
			World.removeObject(leftDoor);
			World.removeObject(rightDoor);
			World.spawnObject(mapLeftDoor);
			World.spawnObject(mapRightDoor);
			HANDLED_DOORS.remove(rightDoor);
			HANDLED_DOORS.remove(leftDoor);
			return;
		}
		final WorldObject nearbyDoor = TemporaryDoubleDoor.getNearbyDoor(player, object);
		if (nearbyDoor == null) {
			return;
		}
		final WorldObject doorA = new WorldObject(object.getX() == nearbyDoor.getX() ? (object.getY() < nearbyDoor.getY() ? object : nearbyDoor) : (object.getX() < nearbyDoor.getX() ? nearbyDoor : object));
		final WorldObject doorB = new WorldObject(doorA.getPositionHash() == object.getPositionHash() ? nearbyDoor : object);
		final WorldObject leftDoor = object.getRotation() == 0 || object.getRotation() == 3 ? doorB : doorA;
		final WorldObject rightDoor = leftDoor == doorA ? doorB : doorA;
		final byte[][] offsets = OFFSETS[object.getRotation()];
		leftDoor.setRotation(offsets[0][2]);
		rightDoor.setRotation(offsets[1][2]);
		leftDoor.moveLocation(offsets[0][0], offsets[0][1], 0);
		rightDoor.moveLocation(offsets[1][0], offsets[1][1], 0);
		World.removeObject(object);
		World.removeObject(nearbyDoor);
		
		if (map.containsKey(leftDoor.getId())) {
			leftDoor.setId(map.get(leftDoor.getId()));
		}
		
		if (map.containsKey(rightDoor.getId())) {
			rightDoor.setId(map.get(rightDoor.getId()));
		}
		World.spawnObject(leftDoor);
		World.spawnObject(rightDoor);
        World.sendSoundEffect(object, !doorA.getDefinitions().containsOption("Open") ? Door.doorOpenSound : Door.doorCloseSound);
		
		final DoubleDoor door = new DoubleDoor(object, nearbyDoor, leftDoor, rightDoor);
		HANDLED_DOORS.put(leftDoor, door);
		HANDLED_DOORS.put(rightDoor, door);
		WorldTasksManager.schedule(() -> {
			if (!World.containsSpawnedObject(leftDoor)) {
				return;
			}
			World.removeObject(leftDoor);
			World.removeObject(rightDoor);
			World.spawnObject(object);
			World.spawnObject(nearbyDoor);
            World.sendSoundEffect(object, leftDoor.getDefinitions().containsOption("Open") ? Door.doorOpenSound : Door.doorCloseSound);
			HANDLED_DOORS.remove(leftDoor);
			HANDLED_DOORS.remove(rightDoor);
		}, 500);
	}

	public static final DoubleDoor handleGraphicalDoubleDoor(final Player player, final WorldObject object,
                                                             final DoubleDoor doubleDoor) {
	    if (doubleDoor != null) {
            World.removeGraphicalDoor(doubleDoor.leftdoor);
            World.removeGraphicalDoor(doubleDoor.rightDoor);
            World.spawnGraphicalDoor(doubleDoor.mapLeftDoor);
            World.spawnGraphicalDoor(doubleDoor.mapRightDoor);
            World.sendSoundEffect(object, doubleDoor.leftdoor.getDefinitions().containsOption("Open") ? Door.doorOpenSound : Door.doorCloseSound);
            return null;
        }
        final WorldObject nearbyDoor = TemporaryDoubleDoor.getNearbyDoor(player, object);
        if (nearbyDoor == null) {
            throw new IllegalStateException();
        }
        final WorldObject doorA = new WorldObject(object.getX() == nearbyDoor.getX() ? (object.getY() < nearbyDoor.getY() ? object : nearbyDoor) : (object.getX() < nearbyDoor.getX() ? nearbyDoor : object));
        final WorldObject doorB = new WorldObject(doorA.getPositionHash() == object.getPositionHash() ? nearbyDoor : object);
        final WorldObject leftDoor = object.getRotation() == 0 || object.getRotation() == 3 ? doorB : doorA;
        final WorldObject rightDoor = leftDoor == doorA ? doorB : doorA;
        final byte[][] offsets = OFFSETS[object.getRotation()];
        leftDoor.setRotation(offsets[0][2]);
        rightDoor.setRotation(offsets[1][2]);
        leftDoor.moveLocation(offsets[0][0], offsets[0][1], 0);
        rightDoor.moveLocation(offsets[1][0], offsets[1][1], 0);

        if (map.containsKey(leftDoor.getId())) {
            leftDoor.setId(map.get(leftDoor.getId()));
        }

        if (map.containsKey(rightDoor.getId())) {
            rightDoor.setId(map.get(rightDoor.getId()));
        }

        World.removeGraphicalDoor(object);
        World.removeGraphicalDoor(nearbyDoor);
        World.spawnGraphicalDoor(leftDoor);
        World.spawnGraphicalDoor(rightDoor);
        World.sendSoundEffect(object, !object.getDefinitions().containsOption("Open") ? Door.doorOpenSound : Door.doorCloseSound);

        return new DoubleDoor(object, nearbyDoor, leftDoor, rightDoor);
    }
	
}
