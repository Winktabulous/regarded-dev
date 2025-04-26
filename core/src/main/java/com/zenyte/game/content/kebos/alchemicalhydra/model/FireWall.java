package com.zenyte.game.content.kebos.alchemicalhydra.model;

import com.zenyte.game.content.kebos.alchemicalhydra.instance.AlchemicalHydraInstance;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.utils.efficientarea.Polygon;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.jetbrains.annotations.NotNull;

import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * @author Tommeh | 10/11/2019 | 14:13
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public enum FireWall {
    NORTH_WEST(new Location(1363, 10271, 0), FireBlockDirection.NORTH, FireBlockDirection.WEST), SOUTH_WEST(new Location(1363, 10264, 0), FireBlockDirection.SOUTH, FireBlockDirection.WEST), NORTH_EAST(new Location(1370, 10271, 0), FireBlockDirection.NORTH, FireBlockDirection.EAST), SOUTH_EAST(new Location(1370, 10264, 0), FireBlockDirection.SOUTH, FireBlockDirection.EAST), NORTH(new Location(1367, 10272, 0), FireBlockDirection.NORTH_EAST, FireBlockDirection.NORTH_WEST), SOUTH(new Location(1366, 10263, 0), FireBlockDirection.SOUTH_EAST, FireBlockDirection.SOUTH_WEST), EAST(new Location(1371, 10267, 0), FireBlockDirection.SOUTH_EAST, FireBlockDirection.NORTH_EAST), WEST(new Location(1362, 10267, 0), FireBlockDirection.SOUTH_WEST, FireBlockDirection.NORTH_WEST);
    private static final FireWall[] frequentWalls = new FireWall[] {NORTH_WEST, SOUTH_WEST, NORTH_EAST, SOUTH_EAST};

    public static final FireWall select(@NotNull final Position staticTargetPosition) {
        final Location tile = staticTargetPosition.getPosition();
        final int tileX = tile.getX();
        final int tileY = tile.getY();
        for (final FireWall frequentWall : frequentWalls) {
            for (final FireWall.FireBlockDirection polygonBlock : frequentWall.blocks) {
                if (polygonBlock.polygon.contains(tileX, tileY)) {
                    return FireWall.valueOf(polygonBlock.name());
                }
            }
        }
        final int centerX = 1367;
        final int centerY = 10268;
        if (tileX < centerX && tileY < centerY) {
            return FireWall.SOUTH_WEST;
        } else if (tileX < centerX) {
            return FireWall.NORTH_WEST;
        } else if (tileX > centerX && tileY < centerY) {
            return FireWall.SOUTH_EAST;
        }
        return FireWall.NORTH_EAST;
    }

    private final FireBlockDirection[] blocks;
    private final Location movingFireLocation;

    FireWall(final Location movingFireLocation, final FireBlockDirection... polygons) {
        this.movingFireLocation = movingFireLocation;
        this.blocks = polygons;
    }

    public RSPolygon[] transform(final AlchemicalHydraInstance instance) {
        final RSPolygon[] transformedWalls = new RSPolygon[blocks.length];
        for (int index = 0; index < blocks.length; index++) {
            final RSPolygon polygon = blocks[index].polygon;
            final int[][] transformedPoints = new int[polygon.getPoints().length][2];
            for (int i = 0; i < polygon.getPoints().length; i++) {
                final int[] point = polygon.getPoints()[i];
                transformedPoints[i] = new int[] {instance.getX(point[0]), instance.getY(point[1])};
            }
            transformedWalls[index] = new RSPolygon(transformedPoints);
        }
        return transformedWalls;
    }

    public final List<FireWallBlock> getInnerPoints(@NotNull final AlchemicalHydraInstance instance) {
        final RSPolygon[] transformedPolygons = transform(instance);
        final ObjectArrayList<FireWallBlock> list = new ObjectArrayList<FireWallBlock>(2);
        for (int i = 0; i < 2; i++) {
            final RSPolygon polygon = transformedPolygons[i];
            final ObjectOpenHashSet<Location> tileSet = new ObjectOpenHashSet<Location>(25);
            final Direction dir = Direction.valueOf(blocks[i].name());
            final FireWallBlock block = new FireWallBlock(tileSet, dir, movingFireLocation);
            list.add(block);
            final Polygon poly = polygon.getPolygon();
            final Rectangle2D rectangle = poly.getBounds2D();
            final int minX = (int) rectangle.getMinX();
            final int maxX = (int) rectangle.getMaxX();
            final int minY = (int) rectangle.getMinY();
            final int maxY = (int) rectangle.getMaxY();
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    if (!poly.contains(x, y)) {
                        continue;
                    }
                    tileSet.add(new Location(x, y, 0));
                }
            }
        }
        return list;
    }


    private enum FireBlockDirection {
        NORTH(new RSPolygon(new int[][] {{1365, 10279}, {1365, 10271}, {1369, 10271}, {1369, 10279}})), SOUTH(new RSPolygon(new int[][] {{1369, 10265}, {1365, 10265}, {1365, 10257}, {1369, 10257}})), WEST(new RSPolygon(new int[][] {{1364, 10270}, {1356, 10270}, {1356, 10266}, {1364, 10266}})), EAST(new RSPolygon(new int[][] {{1378, 10270}, {1370, 10270}, {1370, 10266}, {1378, 10266}})), SOUTH_WEST(new RSPolygon(new int[][] {{1356, 10257}, {1359, 10257}, {1366, 10264}, {1366, 10265}, {1364, 10265}, {1364, 10267}, {1356, 10259}})), NORTH_WEST(new RSPolygon(new int[][] {{1356, 10279}, {1356, 10276}, {1363, 10269}, {1364, 10269}, {1364, 10271}, {1366, 10271}, {1358, 10279}})), NORTH_EAST(new RSPolygon(new int[][] {{1378, 10279}, {1376, 10279}, {1368, 10271}, {1370, 10271}, {1370, 10269}, {1371, 10269}, {1378, 10276}})), SOUTH_EAST(new RSPolygon(new int[][] {{1378, 10257}, {1375, 10257}, {1368, 10264}, {1368, 10265}, {1370, 10265}, {1370, 10267}, {1378, 10259}}));
        private final RSPolygon polygon;

        FireBlockDirection(RSPolygon polygon) {
            this.polygon = polygon;
        }
    }

    public Location getMovingFireLocation() {
        return movingFireLocation;
    }
}
