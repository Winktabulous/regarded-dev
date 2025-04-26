package com.zenyte.game.world.region.dynamicregion;

/**
 * @author Kris | 28. juuli 2018 : 21:54:13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class CoordinateUtilities {
	/**
	 * Translates coordinates within a chunk based on the requested rotation.
	 * 
	 * @param x
	 *            X in chunk
	 * @param y
	 *            Y in chunk
	 * @param rotation
	 *            the rotation requested
	 * @return an array of the new x & y coordinates in the chunk.
	 */
	public static final int[] translate(final int x, final int y, final int rotation) {
		final int[] coords = new int[2];
		if (rotation == 0) {
			coords[0] = x;
			coords[1] = y;
		} else if (rotation == 1) {
			coords[0] = y;
			coords[1] = 7 - x;
		} else if (rotation == 2) {
			coords[0] = 7 - x;
			coords[1] = 7 - y;
		} else {
			coords[0] = 7 - y;
			coords[1] = x;
		}
		return coords;
	}

	/**
	 * Translates an objects coordinates in a chunk based on the map rotation.
	 * 
	 * @param x
	 *            the original x in chunk
	 * @param y
	 *            the original y in chunk
	 * @param mapRotation
	 *            the rotation of the map
	 * @param sizeX
	 *            the width of the object
	 * @param sizeY
	 *            the height of the object
	 * @param objectRotation
	 *            the rotation of the object
	 * @return an array of the new x & y coordinates for the object.
	 */
	public static int[] translate(final int x, final int y, final int mapRotation, int sizeX, int sizeY, final int objectRotation) {
		final int[] coords = new int[2];
		if ((objectRotation & 1) == 1) {
			final int prevSizeX = sizeX;
			sizeX = sizeY;
			sizeY = prevSizeX;
		}
		if (mapRotation == 0) {
			coords[0] = x;
			coords[1] = y;
		} else if (mapRotation == 1) {
			coords[0] = y;
			coords[1] = 7 - x - (sizeX - 1);
		} else if (mapRotation == 2) {
			coords[0] = 7 - x - (sizeX - 1);
			coords[1] = 7 - y - (sizeY - 1);
		} else if (mapRotation == 3) {
			coords[0] = 7 - y - (sizeY - 1);
			coords[1] = x;
		}
		return coords;
	}
}
