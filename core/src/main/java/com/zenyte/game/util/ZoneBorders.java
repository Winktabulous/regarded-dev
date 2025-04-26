package com.zenyte.game.util;

import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.*;
import java.util.function.Predicate;

/**
 * Represents the borders of a zone.
 */
public class ZoneBorders {

	/**
	 * The south west x-coordinate.
	 */
	private final int southWestX;

	/**
	 * The south west y-coordinate.
	 */
	private final int southWestY;

	/**
	 * The north east x-coordinate.
	 */
	private final int northEastX;

	/**
	 * The north east y-coordinate.
	 */
	private final int northEastY;

	/**
	 * The plane required to be on.
	 */
	private final int plane;

	/**
	 * The list of exceptions.
	 */
	private Set<ZoneBorders> exceptions;

	/**
	 * If we need to do a zero plane check.
	 */
	private boolean zeroPlaneCheck;
	private boolean anyLevel;

	public ZoneBorders(Location southWest, Location northEast) {
		this(southWest, northEast, 0);
	}

	public ZoneBorders(Location southWest, Location northEast, int plane) {
		this(southWest.getX(), southWest.getY(), northEast.getX(), northEast.getY(), plane);
	}

	public ZoneBorders(Location southWest, Location northEast, int plane, boolean zeroPlaneCheck) {
		this(southWest.getX(), southWest.getY(), northEast.getX(), northEast.getY(), plane, zeroPlaneCheck);
	}

	/**
	 * Constructs a new {@code ZoneBorders} {@code Object}.
	 *
	 * @param southWestX The south west x-coordinate.
	 * @param southWestY The south west y-coordinate.
	 * @param northEastX The north east x-coordinate.
	 * @param northEastY The north east y-coordinate.
	 */
	public ZoneBorders(int southWestX, int southWestY, int northEastX, int northEastY) {
		this(southWestX, southWestY, northEastX, northEastY, 0);
	}

	/**
	 * Constructs a new {@code ZoneBorders} {@code Object}.
	 *
	 * @param southWestX The south west x-coordinate.
	 * @param southWestY The south west y-coordinate.
	 * @param northEastX The north east x-coordinate.
	 * @param northEastY The north east y-coordinate.
	 * @param plane      the plane.
	 */
	public ZoneBorders(int southWestX, int southWestY, int northEastX, int northEastY, int plane) {
		this.southWestX = southWestX;
		this.southWestY = southWestY;
		this.northEastX = northEastX;
		this.northEastY = northEastY;
		this.plane = plane;
	}

	/**
	 * Constructs a new {@code ZoneBorders} {@code Object}.
	 *
	 * @param southWestX     The south west x-coordinate.
	 * @param southWestY     The south west y-coordinate.
	 * @param northEastX     The north east x-coordinate.
	 * @param northEastY     The north east y-coordinate.
	 * @param plane          the plane.
	 * @param zeroPlaneCheck the plane check.
	 */
	public ZoneBorders(int southWestX, int southWestY, int northEastX, int northEastY, int plane, boolean zeroPlaneCheck) {
		this(southWestX, southWestY, northEastX, northEastY, plane);
		this.zeroPlaneCheck = zeroPlaneCheck;
	}

	public ZoneBorders(int southWestX, int southWestY, int northEastX, int northEastY, int plane, boolean zeroPlaneCheck, Set<ZoneBorders> exceptions) {
		this(southWestX, southWestY, northEastX, northEastY, plane);
		this.zeroPlaneCheck = zeroPlaneCheck;
		this.exceptions = exceptions;
	}

	public boolean insideBorder(int x, int y, int z) {
		if (!anyLevel && zeroPlaneCheck ? z != plane : (plane != 0 && z != plane)) {
			return false;
		}
		if (southWestX <= x && southWestY <= y && northEastX >= x && northEastY >= y) {
			if (exceptions != null) {
				for (ZoneBorders exception : exceptions) {
					if (exception.insideBorder(x, y, z)) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	public boolean overlapping(ZoneBorders other) {
		if (northEastY < other.southWestY || southWestY > other.northEastY) {
			return false;
		}

		if (northEastX < other.southWestX || southWestX > other.northEastX) {
			return false;
		}

		if (exceptions != null) {
			for (ZoneBorders exception : exceptions) {
				if (exception.overlapping(this)) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Gets the ids of all the regions inside these borders.
	 *
	 * @return The region ids.
	 */
	public IntList getRegionIds() {
		final IntList regionIds = new IntArrayList();
		for (int x = southWestX >> 6; x < (northEastX >> 6) + 1; x++) {
			for (int y = southWestY >> 6; y < (northEastY >> 6) + 1; y++) {
				int id = y | x << 8;
				regionIds.add(id);
			}
		}
		return regionIds;
	}

	/**
	 * Gets the southWestX.
	 *
	 * @return The southWestX.
	 */
	public int getSouthWestX() {
		return southWestX;
	}

	/**
	 * Gets the southWestY.
	 *
	 * @return The southWestY.
	 */
	public int getSouthWestY() {
		return southWestY;
	}

	/**
	 * Gets the northEastX.
	 *
	 * @return The northEastX.
	 */
	public int getNorthEastX() {
		return northEastX;
	}

	/**
	 * Gets the northEastY.
	 *
	 * @return The northEastY.
	 */
	public int getNorthEastY() {
		return northEastY;
	}

	/**
	 * Gets the exceptions.
	 *
	 * @return The exceptions.
	 */
	public Set<ZoneBorders> getExceptions() {
		return exceptions;
	}

	/**
	 * Adds an exception.
	 *
	 * @param exception The exception to add.
	 */
	public void addException(ZoneBorders exception) {
		addException(exception, true);
	}

	public void addException(ZoneBorders exception, boolean addOSRSException) {
		if (exceptions == null) {
			this.exceptions = new HashSet<>();
		}
		exceptions.add(exception);
	}

	@Override
	public String toString() {
		return "ZoneBorders [southWestX=" + southWestX + ", southWestY=" + southWestY + ", northEastX=" + northEastX + ", northEastY=" + northEastY + ", exceptions=" + exceptions + "]";
	}

	/**
	 * Gets the bplane.
	 *
	 * @return the plane
	 */
	public int getPlane() {
		return plane;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ZoneBorders)) return false;
		ZoneBorders that = (ZoneBorders) o;
		return southWestX == that.southWestX &&
				southWestY == that.southWestY &&
				northEastX == that.northEastX &&
				northEastY == that.northEastY &&
				plane == that.plane;
	}

	public static ZoneBorders forRadius(Location center, int radius) {
		int baseX = center.getX() - radius;
		int baseY = center.getY() - radius;
		return new ZoneBorders(baseX, baseY, center.getX() + radius, center.getY() + radius, center.getPlane());
	}

	public static ZoneBorders forDimensions(final Location southWest, int width, int height) {
		width = Math.max(0, width - 1);
		height = Math.max(0, height - 1);
		return new ZoneBorders(southWest, southWest.transform(width, height));
	}

	private List<Location> randomTiles;

	private void resetRandomTiles() {
		int width = northEastX - southWestX;
		int height = northEastY - southWestY;
		this.randomTiles = new ArrayList<>(width * height);
		for (int x = southWestX; x <= northEastX; x++) {
			for (int y = southWestY; y <= northEastY; y++) {
				this.randomTiles.add(new Location(x, y, 0));
			}
		}
	}

	private Location getRandomTile(boolean unique, boolean ignoreClipping) {
		int length = randomTiles.size();
		if (length <= 0) {
			return null;
		}

		int index = Utils.randomNoPlus(length);
		Location location = randomTiles.get(index);
		if (unique) {
			randomTiles.remove(index);
		}

		return location;
	}

	private int getRandomTilesRemaining() {
		return randomTiles.size();
	}

	public Location getSpawnLocation(final int nodeSize, final Predicate<Location> predicate) {
		resetRandomTiles();
		Location tile = null;
		main: do {
			tile = getRandomTile(true, false);
			for (int x = tile.getX(); x < tile.getX() + nodeSize; x++) {
				for (int y = tile.getY(); y < tile.getY() + nodeSize; y++) {
					final Location loc = new Location(x, y, tile.getPlane());
					if (!World.isSquareFree(loc, 1) || !insideBorder(loc)) {
						continue main;
					}
				}
			}
			if (predicate != null && !predicate.test(tile)) {
				System.out.println(predicate + " not matching");
				continue;
			}
			return tile;
		} while (getRandomTilesRemaining() > 0);
		return null;
	}

	public boolean insideBorder(ZoneBorders zoneBorders) {
		return southWestX < zoneBorders.northEastX && northEastX > zoneBorders.southWestX && southWestY < zoneBorders.northEastY && northEastY > zoneBorders.southWestY;
	}

	public boolean insideBorder(Entity node) {
		if (node == null) {
			return false;
		}
		final Location location = node.getLocation();
		return insideBorder(location.getX(), location.getY(), location.getPlane());
	}

	public boolean insideBorder(final Location location) {
		return insideBorder(location.getX(), location.getY(), location.getPlane());
	}

	public void setAnyLevel(boolean anyLevel) {
		this.anyLevel = anyLevel;
	}

	@Override
	public int hashCode() {
		return Objects.hash(southWestX, southWestY, northEastX, northEastY, plane);
	}

}
