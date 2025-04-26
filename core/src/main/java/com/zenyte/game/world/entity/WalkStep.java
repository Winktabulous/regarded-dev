package com.zenyte.game.world.entity;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.entity.pathfinding.RouteFinder;
import com.zenyte.game.world.entity.pathfinding.RouteResult;
import com.zenyte.game.world.entity.pathfinding.strategy.EntityStrategy;
import com.zenyte.game.world.entity.pathfinding.strategy.ObjectStrategy;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 11. apr 2018 : 22:24.03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class WalkStep {
	public static final int getHash(final int dir, final int nextX, final int nextY, final boolean check) {
		return ((dir & 7) << 28) | (nextX & 16383) | ((nextY & 16383) << 14) | (check ? 1 << 31 : 0);
	}

	public static final int getDirection(final int hash) {
		return (hash >> 28) & 7;
	}

	public static final int getNextX(final int hash) {
		return hash & 16383;
	}

	public static final int getNextY(final int hash) {
		return (hash >> 14) & 16383;
	}

	public static final boolean check(final int hash) {
		return ((hash >> 31) & 1) == 1;
	}

	public static final boolean addWalkStepsInteract(final Entity entity, final int destX, final int destY, final int maxStepsCount, final int sizeX, final int sizeY, final boolean calculate) {
		final int[] lastTile = entity.getLastWalkTile();
		int myX = lastTile[0];
		int myY = lastTile[1];
		int stepCount = 0;
		while (true) {
			stepCount++;
			final int myRealX = myX;
			final int myRealY = myY;
			if (myX < destX) {
				myX++;
			} else if (myX > destX) {
				myX--;
			}
			if (myY < destY) {
				myY++;
			} else if (myY > destY) {
				myY--;
			}
			if (!entity.addWalkStep(myX, myY, lastTile[0], lastTile[1], true)) {
				if (!calculate) {
					return false;
				}
				myX = myRealX;
				myY = myRealY;
				final int[] myT = calculatedStep(entity, myRealX, myRealY, destX, destY, lastTile[0], lastTile[1], sizeX, sizeY);
				if (myT == null) {
					return false;
				}
				myX = myT[0];
				myY = myT[1];
			}
			final int distanceX = myX - destX;
			final int distanceY = myY - destY;
			if (!(distanceX > sizeX || distanceX < -1 || distanceY > sizeY || distanceY < -1)) {
				return true;
			}
			if (stepCount == maxStepsCount) {
				return true;
			}
			lastTile[0] = myX;
			lastTile[1] = myY;
			if (lastTile[0] == destX && lastTile[1] == destY) {
				return true;
			}
		}
	}

	public static final int[] calculatedStep(final Entity entity, int myX, int myY, final int destX, final int destY, final int lastX, final int lastY, final int sizeX, final int sizeY) {
		if (myX < destX) {
			myX++;
			if (!entity.addWalkStep(myX, myY, lastX, lastY, true)) {
				myX--;
			} else if (!(myX - destX > sizeX || myX - destX < -1 || myY - destY > sizeY || myY - destY < -1)) {
				if (myX == lastX || myY == lastY) {
					return null;
				}
				return new int[] {myX, myY};
			}
		} else if (myX > destX) {
			myX--;
			if (!entity.addWalkStep(myX, myY, lastX, lastY, true)) {
				myX++;
			} else if (!(myX - destX > sizeX || myX - destX < -1 || myY - destY > sizeY || myY - destY < -1)) {
				if (myX == lastX || myY == lastY) {
					return null;
				}
				return new int[] {myX, myY};
			}
		}
		if (myY < destY) {
			myY++;
			if (!entity.addWalkStep(myX, myY, lastX, lastY, true)) {
				myY--;
			} else if (!(myX - destX > sizeX || myX - destX < -1 || myY - destY > sizeY || myY - destY < -1)) {
				if (myX == lastX || myY == lastY) {
					return null;
				}
				return new int[] {myX, myY};
			}
		} else if (myY > destY) {
			myY--;
			if (!entity.addWalkStep(myX, myY, lastX, lastY, true)) {
				myY++;
			} else if (!(myX - destX > sizeX || myX - destX < -1 || myY - destY > sizeY || myY - destY < -1)) {
				if (myX == lastX || myY == lastY) {
					return null;
				}
				return new int[] {myX, myY};
			}
		}
		if (myX == lastX || myY == lastY) {
			return null;
		}
		return new int[] {myX, myY};
	}

	private static final int withinRange(final int npcX, final int npcY, final int npcSize, final int targetX, final int targetY, final int targetSize) {
		final int distanceX = npcX - targetX;
		final int distanceY = npcY - targetY;
		if (distanceX == -npcSize && distanceY == -npcSize || distanceX == targetSize && distanceY == targetSize || distanceX == -npcSize && distanceY == targetSize || distanceX == targetSize && distanceY == -npcSize) {
			return 1;
		}
		if (!(distanceX > targetSize || distanceY > targetSize || distanceX < -npcSize || distanceY < -npcSize)) {
			return 0;
		}
		return -1;
	}

	public static boolean find(final Entity src, final Entity dest, int maxStepsCount, final boolean calculate) {
		final int[] srcPos = src.getLastWalkTile();
		final Location nextPos = dest.getLocation();
		final int x = nextPos.getX();
		final int y = nextPos.getY();
		final int srcSize = src.getSize();
		final int destSize = dest.getSize();
		final int destSceneX = x + srcSize - 1;
		final int destSceneY = y + srcSize - 1;
		while (maxStepsCount-- != 0) {
			final int[] srcScenePos = {srcPos[0] + srcSize - 1, srcPos[1] + srcSize - 1};
			int o;
			final int absDistX = srcPos[0] - x;
			final int absDistY = srcPos[1] - y;
			final boolean diagonal = (absDistX == -srcSize && absDistY == -srcSize) || (absDistX == -srcSize && absDistY == 1) || (absDistX == 1 && absDistY == -srcSize) || (absDistX == 1 && absDistY == 1);
			if (diagonal) {
				if (srcScenePos[0] < destSceneX && src.addWalkStep(srcPos[0] + 1, srcPos[1], srcPos[0], srcPos[1], true)) {
					srcPos[0]++;
					continue;
				}
				if (srcScenePos[0] > destSceneX && src.addWalkStep(srcPos[0] - 1, srcPos[1], srcPos[0], srcPos[1], true)) {
					srcPos[0]--;
					continue;
				}
				return false;
			}
			if ((o = withinRange(srcPos[0], srcPos[1], srcSize, x, y, destSize)) != 0) {
				if (o != 1) {
					if (srcScenePos[0] < destSceneX && srcScenePos[1] < destSceneY && src.addWalkStep(srcPos[0] + 1, srcPos[1] + 1, srcPos[0], srcPos[1], true)) {
						srcPos[0]++;
						srcPos[1]++;
						continue;
					}
					if (srcScenePos[0] > destSceneX && srcScenePos[1] > destSceneY && src.addWalkStep(srcPos[0] - 1, srcPos[1] - 1, srcPos[0], srcPos[1], true)) {
						srcPos[0]--;
						srcPos[1]--;
						continue;
					}
					if (srcScenePos[0] < destSceneX && srcScenePos[1] > destSceneY && src.addWalkStep(srcPos[0] + 1, srcPos[1] - 1, srcPos[0], srcPos[1], true)) {
						srcPos[0]++;
						srcPos[1]--;
						continue;
					}
					if (srcScenePos[0] > destSceneX && srcScenePos[1] < destSceneY && src.addWalkStep(srcPos[0] - 1, srcPos[1] + 1, srcPos[0], srcPos[1], true)) {
						srcPos[0]--;
						srcPos[1]++;
						continue;
					}
				}
				if (srcScenePos[0] < destSceneX && src.addWalkStep(srcPos[0] + 1, srcPos[1], srcPos[0], srcPos[1], true)) {
					srcPos[0]++;
					continue;
				}
				if (srcScenePos[0] > destSceneX && src.addWalkStep(srcPos[0] - 1, srcPos[1], srcPos[0], srcPos[1], true)) {
					srcPos[0]--;
					continue;
				}
				if (srcScenePos[1] < destSceneY && src.addWalkStep(srcPos[0], srcPos[1] + 1, srcPos[0], srcPos[1], true)) {
					srcPos[1]++;
					continue;
				}
				if (srcScenePos[1] > destSceneY && src.addWalkStep(srcPos[0], srcPos[1] - 1, srcPos[0], srcPos[1], true)) {
					srcPos[1]--;
					continue;
				}
				return false;
			}
			break; // for now nothing between break and return
		}
		return true;
	}

	public static boolean findBasicRoute(final Entity src, final Object dest, int maxStepsCount, final boolean calculate) {
		if (dest instanceof Entity) {
			return find(src, (Entity) dest, maxStepsCount, calculate);
		}
		final int[] srcPos = src.getLastWalkTile();
		final int x = (dest instanceof Entity ? ((Entity) dest).getX() : ((Location) dest).getX());
		final int y = (dest instanceof Entity ? ((Entity) dest).getY() : ((Location) dest).getY());
		//final int[] destPos = { x, y };
		final int srcSize = src.getSize();
		// set destSize to 0 to walk under it else follows
		final int destSize = dest instanceof Entity ? ((Entity) dest).getSize() : 1;
		//final int[] destScenePos = { destPos[0] + destSize - 1, destPos[1] + destSize - 1 };
		final int destSceneX = x + destSize - 1;
		final int destSceneY = y + destSize - 1;
		while (maxStepsCount-- != 0) {
			final int[] srcScenePos = {srcPos[0] + srcSize - 1, srcPos[1] + srcSize - 1};
			if (!Utils.isOnRangeExcludingDiagonal(srcPos[0], srcPos[1], srcSize, x, y, destSize, 0)) {
				if (srcScenePos[0] < destSceneX && srcScenePos[1] < destSceneY && src.addWalkStep(srcPos[0] + 1, srcPos[1] + 1, srcPos[0], srcPos[1], true)) {
					srcPos[0]++;
					srcPos[1]++;
					continue;
				}
				if (srcScenePos[0] > destSceneX && srcScenePos[1] > destSceneY && src.addWalkStep(srcPos[0] - 1, srcPos[1] - 1, srcPos[0], srcPos[1], true)) {
					srcPos[0]--;
					srcPos[1]--;
					continue;
				}
				if (srcScenePos[0] < destSceneX && srcScenePos[1] > destSceneY && src.addWalkStep(srcPos[0] + 1, srcPos[1] - 1, srcPos[0], srcPos[1], true)) {
					srcPos[0]++;
					srcPos[1]--;
					continue;
				}
				if (srcScenePos[0] > destSceneX && srcScenePos[1] < destSceneY && src.addWalkStep(srcPos[0] - 1, srcPos[1] + 1, srcPos[0], srcPos[1], true)) {
					srcPos[0]--;
					srcPos[1]++;
					continue;
				}
				if (srcScenePos[0] < destSceneX && src.addWalkStep(srcPos[0] + 1, srcPos[1], srcPos[0], srcPos[1], true)) {
					srcPos[0]++;
					continue;
				}
				if (srcScenePos[0] > destSceneX && src.addWalkStep(srcPos[0] - 1, srcPos[1], srcPos[0], srcPos[1], true)) {
					srcPos[0]--;
					continue;
				}
				if (srcScenePos[1] < destSceneY && src.addWalkStep(srcPos[0], srcPos[1] + 1, srcPos[0], srcPos[1], true)) {
					srcPos[1]++;
					continue;
				}
				if (srcScenePos[1] > destSceneY && src.addWalkStep(srcPos[0], srcPos[1] - 1, srcPos[0], srcPos[1], true)) {
					srcPos[1]--;
					continue;
				}
				return false;
			}
			break; // for now nothing between break and return
		}
		return true;
	}

	public static final boolean calcFollow(final Entity entity, final Position target, final int maxStepsCount, final boolean calculate, final boolean intelligent, final boolean checkEntities) {
		if (intelligent) {
			final RouteResult steps = RouteFinder.findRoute(entity.getX(), entity.getY(), entity.getPlane(), entity.getSize(), target instanceof WorldObject ? new ObjectStrategy((WorldObject) target) : target instanceof Location ? new TileStrategy((Location) target) : new EntityStrategy((Entity) target), true);
			if (steps == RouteResult.ILLEGAL) {
				return false;
			}
			if (steps.getSteps() == 0) {
				return true;
			}
			final int[] bufferX = steps.getXBuffer();
			final int[] bufferY = steps.getYBuffer();
			int stepCount = 0;
			for (int step = steps.getSteps() - 1; step >= 0; step--) {
				if (!checkEntities) {
					if (!entity.addWalkSteps(bufferX[step], bufferY[step], maxStepsCount, true)) {
						break;
					}
				} else {
					if (!entity.addWalkStepsInteract(bufferX[step], bufferY[step], maxStepsCount, entity.getSize(), true)) {
						break;
					}
				}
				if (maxStepsCount != -1 && ++stepCount >= maxStepsCount) {
					break;
				}
			}
			return true;
		}
		return WalkStep.findBasicRoute(entity, target, maxStepsCount, true);
	}

	public static final boolean addWalkSteps(final Entity entity, final int destX, final int destY, final int maxStepsCount, final boolean check) {
		final int[] lastTile = entity.getLastWalkTile();
		int myX = lastTile[0];
		int myY = lastTile[1];
		int stepCount = 0;
		while (true) {
			stepCount++;
			if (myX < destX) {
				myX++;
			} else if (myX > destX) {
				myX--;
			}
			if (myY < destY) {
				myY++;
			} else if (myY > destY) {
				myY--;
			}
			if (!entity.addWalkStep(myX, myY, lastTile[0], lastTile[1], check)) {
				return false;
			}
			if (stepCount == maxStepsCount) {
				return true;
			}
			lastTile[0] = myX;
			lastTile[1] = myY;
			if (lastTile[0] == destX && lastTile[1] == destY) {
				return true;
			}
		}
	}
}
