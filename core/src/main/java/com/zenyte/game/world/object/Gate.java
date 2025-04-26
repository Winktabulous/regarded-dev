package com.zenyte.game.world.object;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Kris | 12. dets 2017 : 17:35.33
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class Gate {

	/**
	 * A 2d array consisting of open to closed gate ids.
	 */
	private static final int[][] RESPECTIVE_GATES = new int[][] {
		{ 48, 1564 }, { 47, 1563 }, { 167, 1567 }, { 166, 1563 }, { 3726, 1564 },
		{ 3725, 1563 }, { 1562, 3728 }, { 1561, 3727 }, { 8811, 8813 }, { 8810, 8812 },
		{ 12989, 9708 }, { 12988, 9470 }, { 2051, 15513 }, { 2050, 15511 }, { 23919, 9780 },
		{ 23918, 9470 }, { 1560, 1567 }, { 1558, 1559 }, { 1731, 1567 }, { 1730, 1559 },
		{ 2439, 8813 }, { 2438, 8812 }, { 4312, 3728 }, { 4311, 3727 }, { 9708, 8813 },
		{ 9470, 8812 }, { 12987, 12989 }, { 12896, 12988 }, { 15512, 15513 }, { 15510, 15511 },
		{ 15516, 15517 }, { 15514, 15515 }, { 24561, 15517 }, { 24560, 15515 },
	};
	
	/**
	 * A map of open to closed gate ids. Using a map over looping the above 2d array 
	 * for better performance.
	 */
	private static final Int2IntOpenHashMap GATES = new Int2IntOpenHashMap();
	
	static {
		for (final int[] set : RESPECTIVE_GATES)
			GATES.put(set[0], set[1]);
	}
	
	/**
	 * A concurrent map of currently opened gates.
	 */
	private static final Map<WorldObject, Gate> HANDLED_GATES = new ConcurrentHashMap<WorldObject, Gate>();
	
	
	/**
	 * Gets the respective closed gate id; if none could be found, returns the same open id.
	 * @param id open gate id.
	 * @return closed gate id.
	 */
	static final int getRespectiveId(final int id) {
        return GATES.getOrDefault(id, id);
	}
	
	/**
	 * Gets an array of two worldtiles based off of the inner gate.
	 * @param object inner gate that was clicked.
	 * @return the new coordinates for inner[0] & outer[1] gates.
	 */
	static Location[] getOpenLocations(final WorldObject object) {
		switch (object.getRotation()) {
		case 0:
			return new Location[] { new Location(object.getX() - 1, object.getY(), object.getPlane()),
					new Location(object.getX() - 2, object.getY(), object.getPlane()) };
		case 1:
			return new Location[] { new Location(object.getX(), object.getY() + 1, object.getPlane()),
					new Location(object.getX(), object.getY() + 2, object.getPlane()) };
		case 2:
			return new Location[] { new Location(object.getX() + 1, object.getY(), object.getPlane()),
					new Location(object.getX() + 2, object.getY(), object.getPlane()) };
		default:
			return new Location[] { new Location(object.getX(), object.getY() - 1, object.getPlane()),
					new Location(object.getX(), object.getY() - 2, object.getPlane()) };
		}
	}
	
	/**
	 * Gets the other respective gate nearby.
	 * @param gate object that was clicked.
	 * @return the gate that goes with the specific object.
	 */
	static final WorldObject getOtherGate(final WorldObject gate) {
		WorldObject object;
		if ((object = World.getObjectWithType(new Location(gate.getX() + 1, gate.getY(), gate.getPlane()), gate.getType())) != null && object.getName().equals(gate.getName())
				|| (object = World.getObjectWithType(new Location(gate.getX() - 1, gate.getY(), gate.getPlane()), gate.getType())) != null && object.getName().equals(gate.getName())
				|| (object = World.getObjectWithType(new Location(gate.getX(), gate.getY() + 1, gate.getPlane()), gate.getType())) != null && object.getName().equals(gate.getName())
				|| (object = World.getObjectWithType(new Location(gate.getX(), gate.getY() - 1, gate.getPlane()), gate.getType())) != null && object.getName().equals(gate.getName()))
			return object;
		return null;
	}
	
	/**
	 * Specifies which of the two gates is the inner one.
	 * @param object clicked object.
	 * @param otherGate the other object nearby.
	 * @return the inner gate.
	 */
	static final WorldObject getInnerMapGate(final WorldObject object, final WorldObject otherGate) {
		if (object.getRotation() == 0) 
			return object.getY() < otherGate.getY() ? object : otherGate;
		else if (object.getRotation() == 2) 
			return object.getY() > otherGate.getY() ? object : otherGate;
		else if (object.getRotation() == 1) 
			return object.getX() < otherGate.getX() ? object : otherGate;			
		return object.getX() > otherGate.getX() ? object : otherGate;
	}
	
	/**
	 * Handles the gate, if the gate is open (in the list),
	 * it will shut the gates and remove the gates from the map.
	 * However if the gate is closed, it will find and specify
	 * both gates and spawn new ones instead of those.
	 * There will also be a minute long task initiated when
	 * a gate is opened that will close the gate if it hasn't been
	 * closed already prior.
	 * @param object the object that was clicked/requested.
	 */
	public static final void handleGate(final WorldObject object) {
		final WorldObject otherGate = getOtherGate(object);
		if (otherGate == null)
			return;
		Gate gate = HANDLED_GATES.get(object);
		if (gate != null) {
			HANDLED_GATES.remove(gate.innerGate);
			HANDLED_GATES.remove(gate.outerGate);
			World.removeObject(gate.innerGate);
			World.removeObject(gate.outerGate);
            World.sendSoundEffect(object, Door.doorCloseSound);
			World.spawnObject(gate.mapInnerGate);
			World.spawnObject(gate.mapOuterGate);
			return;
		}
		final WorldObject innerMapGate = getInnerMapGate(object, otherGate);
		final WorldObject outerMapGate = otherGate == innerMapGate ? object : otherGate;
		final Location[] tiles = getOpenLocations(innerMapGate);
		final WorldObject innerGate = new WorldObject(getRespectiveId(innerMapGate.getId()), innerMapGate.getType(), (innerMapGate.getRotation() - 1) & 0x3, tiles[0]);
		final WorldObject outerGate = new WorldObject(getRespectiveId(outerMapGate.getId()), outerMapGate.getType(), (outerMapGate.getRotation() - 1) & 0x3, tiles[1]);
		World.removeObject(innerMapGate);
		World.removeObject(outerMapGate);
		World.spawnObject(innerGate);
		World.spawnObject(outerGate);
        World.sendSoundEffect(object, Door.doorOpenSound);
		gate = new Gate(innerMapGate, outerMapGate, innerGate, outerGate);
		HANDLED_GATES.put(innerGate, gate);
		HANDLED_GATES.put(outerGate, gate);
		WorldTasksManager.schedule(() -> {
			if (World.containsSpawnedObject(outerGate))
				handleGate(outerGate);
		}, 100);
	}
	
	private final WorldObject mapInnerGate, mapOuterGate;
	private final WorldObject innerGate, outerGate;
	
	public Gate(final WorldObject mapInnerGate, final WorldObject mapOuterGate,
			final WorldObject innerGate, final WorldObject outerGate) {
		this.mapInnerGate = mapInnerGate;
		this.mapOuterGate = mapOuterGate;
		this.innerGate = innerGate;
		this.outerGate = outerGate;
	}
	
}