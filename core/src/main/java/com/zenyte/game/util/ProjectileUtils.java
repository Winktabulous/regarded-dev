package com.zenyte.game.util;

import com.zenyte.game.world.Position;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.pathfinding.Flags;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 27. sept 2018 : 22:53:44
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public class ProjectileUtils {

    /**
     * Checks whether the projectile would be clipped between the two entities. Size will be called from {@code Entity#getSize()} if the
     * position is an instance of entity, or 1 by default.
     *
     * @param from           the position from which the projectile is shot.
     * @param to             the position to which the projectile is shot.
     * @param closeProximity whether to check for close proximity, meaning objects such as gates would block the path.
     * @return whether the projectile is clipped in this path or not.
     */
    public static boolean isProjectileClipped(final Entity sender, final Entity receiver, final Position from,
                                              final Position to,
                                              final boolean closeProximity) {
        final int fromSize = sender != null ? sender.getSize() : 1;
        final int toSize = receiver != null ? receiver.getSize() : 1;
        return isProjectileClipped(sender, receiver, from, to, fromSize, toSize, closeProximity, false);
    }

    public static boolean isProjectileClipped(final Entity sender, final Entity receiver) {
        final int fromSize = sender != null ? sender.getSize() : 1;
        final int toSize = receiver != null ? receiver.getSize() : 1;
        if(sender == null || receiver == null)
            return false;
        return isProjectileClipped(sender, receiver, sender.getPosition(), receiver.getPosition(), fromSize, toSize, true, false);
    }

    public static boolean isProjectileClipped(final Entity sender, final Entity receiver, final Position from,
                                              final Position to,
                                              final boolean closeProximity, final boolean ignoreUnderneath) {
        final int fromSize = sender != null ? sender.getSize() : 1;
        final int toSize = receiver != null ? receiver.getSize() : 1;
        return isProjectileClipped(sender, receiver, from, to, fromSize, toSize, closeProximity, ignoreUnderneath);
    }

    public static boolean isProjectileClipped(final Entity sender, final Entity receiver, final Position from,
                                              final Position to, final int fromSize, final int toSize, final boolean closeProximity, final boolean ignoreUnderneath) {
        return _isProjectileClipped(sender, receiver, from, to, fromSize, toSize, closeProximity, ignoreUnderneath);
    }

    /**
     * Checks whether the projectile would be clipped between the two entities.
     *
     * @param from             the position from which the projectile is shot.
     * @param to               the position to which the projectile is shot.
     * @param fromSize         size of the from position.
     * @param toSize           size of the to position.
     * @param closeProximity   whether to check for close proximity, meaning objects such as gates would block the path.
     * @param ignoreUnderneath whether or not the code should ignore the from/to tiles.
     * @return whether the projectile is clipped in this path or not.
     */
    private static boolean _isProjectileClipped(final Entity sender, final Entity receiver, final Position from,
                                                final Position to, final int fromSize, final int toSize, final boolean closeProximity, boolean ignoreUnderneath) {
        final Location sourceLp = from.getPosition();
        final Location targetLp = to.getPosition();
        final int fromPlane = sourceLp.getPlane();
        final int toPlane = targetLp.getPlane();
        if (fromPlane != toPlane) {
            return true;
        }
        int cmpThisX, cmpThisY, cmpOtherX, cmpOtherY;
        {
            int otherX = targetLp.getX();
            int otherY = targetLp.getY();
            int thisX = sourceLp.getX();
            int thisY = sourceLp.getY();
            int width = fromSize;
            int height = fromSize;
            int otherWidth = toSize;
            int otherHeight = toSize;


            // Determine which position to compare with for this NPC
            if (otherX <= thisX) {
                cmpThisX = thisX;
            } else if (otherX >= thisX + width - 1) {
                cmpThisX = thisX + width - 1;
            } else {
                cmpThisX = otherX;
            }
            if (otherY <= thisY) {
                cmpThisY = thisY;
            } else if (otherY >= thisY + height - 1) {
                cmpThisY = thisY + height - 1;
            } else {
                cmpThisY = otherY;
            }

            // Determine which position to compare for the other actor
            if (thisX <= otherX) {
                cmpOtherX = otherX;
            } else if (thisX >= otherX + otherWidth - 1) {
                cmpOtherX = otherX + otherWidth - 1;
            } else {
                cmpOtherX = thisX;
            }
            if (thisY <= otherY) {
                cmpOtherY = otherY;
            } else if (thisY >= otherY + otherHeight - 1) {
                cmpOtherY = otherY + otherHeight - 1;
            } else {
                cmpOtherY = thisY;
            }
        }

        int targetX = cmpOtherX;
        int targetY = cmpOtherY;
        int projX = cmpThisX;
        int projY = cmpThisY;
        int distanceX = targetX - projX;
        int distanceY = targetY - projY;

        if (CollisionUtil.collides(sourceLp.getX(), sourceLp.getY(), fromSize, targetLp.getX(), targetLp.getY(), toSize))
            return false;

        int absDistanceX = Math.abs(distanceX);
        int absDistanceY = Math.abs(distanceY);
        if (absDistanceX == 0 && absDistanceY == 0) {
            return false;
        }

        int xTileMask = closeProximity ? (Flags.FLOOR | Flags.FLOOR_DECORATION | Flags.OBJECT) : Flags.OBJECT_PROJECTILE;
        int yTileMask = closeProximity ? (Flags.FLOOR | Flags.FLOOR_DECORATION | Flags.OBJECT) : Flags.OBJECT_PROJECTILE;
        if (distanceX < 0) {
            xTileMask |= closeProximity ? Flags.WALL_EAST : Flags.WALL_EAST_PROJECTILE;
        } else {
            xTileMask |= closeProximity ? Flags.WALL_WEST : Flags.WALL_WEST_PROJECTILE;
        }

        if (distanceY < 0) {
            yTileMask |= closeProximity ? Flags.WALL_NORTH : Flags.WALL_NORTH_PROJECTILE;
        } else {
            yTileMask |= closeProximity ? Flags.WALL_SOUTH : Flags.WALL_SOUTH_PROJECTILE;
        }

        int entityFlag = 0;
        if (sender != null) {
            if (sender instanceof Player) {
                xTileMask |= Flags.OCCUPIED_PROJECTILE_BLOCK_PLAYER;
                yTileMask |= Flags.OCCUPIED_PROJECTILE_BLOCK_PLAYER;
                entityFlag = Flags.OCCUPIED_PROJECTILE_BLOCK_PLAYER;
            } else {
                xTileMask |= Flags.OCCUPIED_PROJECTILE_BLOCK_NPC;
                yTileMask |= Flags.OCCUPIED_PROJECTILE_BLOCK_NPC;
                entityFlag = Flags.OCCUPIED_PROJECTILE_BLOCK_NPC;
            }
        }

        if (absDistanceX > absDistanceY) {
            int currentX = projX;
            int currentY = projY << 16;
            int ratio = (distanceY << 16) / absDistanceX;
            currentY += 32768;
            if (distanceY < 0) {
                --currentY;
            }
            int err = distanceX < 0 ? -1 : 1;
            int actualCurrentY;
            int postRatioCurrentY;
            while (currentX != targetX) {
                currentX += err;
                actualCurrentY = currentY >>> 16;
                if ((getMask(currentX, actualCurrentY, fromPlane) & (currentX == targetX ? (xTileMask & ~entityFlag) :
                        xTileMask)) != 0) {
                    if (!ignoreUnderneath || !((currentX == targetX && actualCurrentY == targetY) || (currentX == projX && actualCurrentY == projY))) {
                        return true;
                    }
                }
                currentY += ratio;
                postRatioCurrentY = currentY >>> 16;
                if (postRatioCurrentY != actualCurrentY
                        && (getMask(currentX, postRatioCurrentY, fromPlane) & (currentX == targetX ? (yTileMask & ~entityFlag)
                        : yTileMask)) != 0) {
                    if (!ignoreUnderneath || !((currentX == targetX && postRatioCurrentY == targetY) || (currentX == projX && postRatioCurrentY == projY))) {
                        return true;
                    }
                }

            }
        } else {
            int currentY = projY;
            int currentX = projX << 16;
            int ratio = (distanceX << 16) / absDistanceY;
            currentX += 32768;
            if (distanceX < 0) {
                --currentX;
            }
            int actualCurrentX;
            int postRatioCurrentX;
            int err = distanceY < 0 ? -1 : 1;
            while (currentY != targetY) {
                currentY += err;
                actualCurrentX = currentX >>> 16;
                if ((getMask(actualCurrentX, currentY, fromPlane) & (currentY == targetY ? (yTileMask & ~entityFlag) :
                        yTileMask)) != 0) {
                    if (!ignoreUnderneath || !((actualCurrentX == targetX && currentY == targetY) || (actualCurrentX == projX && currentY == projY))) {
                        return true;
                    }
                }
                currentX += ratio;
                postRatioCurrentX = currentX >>> 16;
                if (postRatioCurrentX != actualCurrentX && (getMask(postRatioCurrentX, currentY, fromPlane) & (currentY == targetY ? (xTileMask & ~entityFlag) : xTileMask)) != 0) {
                    if (!ignoreUnderneath || !((postRatioCurrentX == targetX && currentY == targetY) || (postRatioCurrentX == projX && currentY == projY))) {
                        return true;
                    }
                }

            }
        }
        return false;
    }

    private static int getMask(final int x, final int y, final int plane) {
        return World.getMask(plane, x, y);
    }

}
