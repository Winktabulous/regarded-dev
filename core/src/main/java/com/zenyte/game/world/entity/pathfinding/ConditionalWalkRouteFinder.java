package com.zenyte.game.world.entity.pathfinding;

import com.zenyte.game.world.World;
import com.zenyte.game.world.region.Region;
import com.zenyte.game.world.region.RegionMap;

import java.util.Arrays;

/**
 * @author Kris | 13/05/2019 16:51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ConditionalWalkRouteFinder {
    private static final int GRAPH_SIZE = 128;
    private static final int QUEUE_SIZE = (GRAPH_SIZE * GRAPH_SIZE) / 4; // we do /4 because each tile can only be accessed from single direction
    private static final int ALTERNATIVE_ROUTE_MAX_DISTANCE = 100;
    private static final int ALTERNATIVE_ROUTE_RANGE = 15;

    private static final int DIR_NORTH = 0x1;
    private static final int DIR_EAST = 0x2;
    private static final int DIR_SOUTH = 0x4;
    private static final int DIR_WEST = 0x8;

    private static final int[][] directions = new int[GRAPH_SIZE][GRAPH_SIZE];
    private static final int[][] distances = new int[GRAPH_SIZE][GRAPH_SIZE];
    private static final int[][] clip = new int[GRAPH_SIZE][GRAPH_SIZE];
    private static final int[] bufferX = new int[QUEUE_SIZE];
    private static final int[] bufferY = new int[QUEUE_SIZE];
    private static int exitX = -1;
    private static int exitY = -1;
    private static boolean isAlternative;

    /**
     * Find's route using given strategy. Returns amount of steps found. If
     * steps > 0, route exists. If steps = 0, route exists, but no need to move.
     * If steps < 0, route does not exist.
     */
    protected static synchronized RouteResult findRoute(final int srcX, final int srcY, final int srcZ,
                                                        final int srcSizeXY,
                                                        final RouteStrategy strategy, final boolean findAlternative) {
        try {
            isAlternative = false;
            for (int x = 0; x < GRAPH_SIZE; x++) {
                for (int y = 0; y < GRAPH_SIZE; y++) {
                    directions[x][y] = 0;
                    distances[x][y] = 99999999;
                }
            }
            transmitClipData(srcX, srcY, srcZ);

            // we could use performCalculationSX() for every size, but since most common size's are 1 and 2,
            // we will have optimized algorhytm's for them.
            boolean found;
            switch (srcSizeXY) {
                case 1:
                    found = performCalculationS1(srcX, srcY, strategy);
                    break;
                case 2:
                    found = performCalculationS2(srcX, srcY, strategy);
                    break;
                default:
                    found = performCalculationSX(srcX, srcY, srcSizeXY, strategy);
                    break;
            }
            if (!found && !findAlternative) {
                return RouteResult.ILLEGAL;
            }

            // when we start searching for path, we position ourselves in the middle of graph
            // so the base(minimum) position is source_pos - HALF_GRAPH_SIZE.
            final int graphBaseX = srcX - (GRAPH_SIZE / 2);
            final int graphBaseY = srcY - (GRAPH_SIZE / 2);
            int endX = exitX;
            int endY = exitY;

            if (!found) {
                isAlternative = true;
                int lowestCost = Integer.MAX_VALUE;
                int lowestDistance = Integer.MAX_VALUE;

                final int approxDestX = strategy.getApproxDestinationX();
                final int approxDestY = strategy.getApproxDestinationY();

                // what we will do here is search the coordinates range of destination +- ALTERNATIVE_ROUTE_RANGE
                // to see if at least one position in that range is reachable, and reaching it takes no longer than ALTERNATIVE_ROUTE_MAX_DISTANCE steps.
                // if we have multiple positions in our range that fits all the conditions, we will choose the one which takes fewer steps.

                for (int checkX = (approxDestX - ALTERNATIVE_ROUTE_RANGE); checkX <= (approxDestX + ALTERNATIVE_ROUTE_RANGE); checkX++) {
                    for (int checkY = (approxDestY - ALTERNATIVE_ROUTE_RANGE); checkY <= (approxDestY + ALTERNATIVE_ROUTE_RANGE); checkY++) {
                        final int graphX = checkX - graphBaseX;
                        final int graphY = checkY - graphBaseY;
                        if (graphX < 0 || graphY < 0 || graphX >= GRAPH_SIZE || graphY >= GRAPH_SIZE || distances[graphX][graphY] >= ALTERNATIVE_ROUTE_MAX_DISTANCE) {
                            continue; // we are out of graph's bounds or too much steps.
                        }
                        // calculate the delta's.
                        // when calculating, we are also taking the approximated destination size into account to increase precise.
                        int deltaX;
                        int deltaY;
                        if (approxDestX <= checkX) {
                            deltaX = 1 - approxDestX - (strategy.getApproxDestinationSizeX() + strategy.getDistance() - checkX);
                            //deltaX = (approxDestX + (strategy.getApproxDestinationSizeX() - 1)) < checkX ? (approxDestX - (checkX - (strategy.getApproxDestinationSizeX() + 1))) : 0;
                        } else {
                            deltaX = approxDestX - checkX;
                        }
                        if (approxDestY <= checkY) {
                            deltaY = 1 - approxDestY - (strategy.getApproxDestinationSizeY() + strategy.getDistance() - checkY);
                            //deltaY = (approxDestY + (strategy.getApproxDestinationSizeY() - 1)) < checkY ? (approxDestY - (checkY - (strategy.getApproxDestinationSizeY() + 1))) : 0;
                        } else {
                            deltaY = approxDestY - checkY;
                        }

                        final int cost = (deltaX * deltaX) + (deltaY * deltaY);
                        if (cost < lowestCost || (cost <= lowestCost && distances[graphX][graphY] < lowestDistance)) {
                            // if the cost is lower than the lowest one, or same as the lowest one, but less steps, we accept this position as alternate.
                            lowestCost = cost;
                            lowestDistance = distances[graphX][graphY];
                            endX = checkX;
                            endY = checkY;
                        }
                    }
                }

                if (lowestCost == Integer.MAX_VALUE || lowestDistance == Integer.MAX_VALUE) {
                    return RouteResult.ILLEGAL; // we didin't find any alternative route, sadly.
                }
            }

            if (endX == srcX && endY == srcY) {
                return new RouteResult(0, Arrays.copyOf(bufferX, 0), Arrays.copyOf(bufferY,
                        0), isAlternative); // path was found, but we didin't move
            }

            // what we will do now is trace the path from the end position
            // for faster performance, we are reusing our queue buffer for another purpose.
            int steps = 0;
            int traceX = endX;
            int traceY = endY;
            int direction = directions[traceX - graphBaseX][traceY - graphBaseY];
            int lastwritten = direction;
            // queue destination position and start tracing from it
            bufferX[steps] = traceX;
            bufferY[steps++] = traceY;
            while (traceX != srcX || traceY != srcY) {
                if (lastwritten != direction) {
                    // we changed our direction, write it
                    bufferX[steps] = traceX;
                    bufferY[steps++] = traceY;
                    lastwritten = direction;
                }

                if ((direction & DIR_EAST) != 0) {
                    traceX++;
                } else if ((direction & DIR_WEST) != 0) {
                    traceX--;
                }

                if ((direction & DIR_NORTH) != 0) {
                    traceY++;
                } else if ((direction & DIR_SOUTH) != 0) {
                    traceY--;
                }

                direction = directions[traceX - graphBaseX][traceY - graphBaseY];
            }
            return new RouteResult(steps, Arrays.copyOf(bufferX, steps), Arrays.copyOf(bufferY,
                    steps), isAlternative);
            // return steps;
        } catch (final Exception e) {
            e.printStackTrace();
            return new RouteResult(0, Arrays.copyOf(bufferX, 0), Arrays.copyOf(bufferY,
                    0), true);
        }
    }

    /**
     * Performs size 1 calculations.
     */
    private static boolean performCalculationS1(final int srcX, final int srcY, final RouteStrategy strategy) {
        // first, we will cache our static fields to local variables, this is done for performance, because
        // modern jit compiler's usually takes advantage of things like this
        final int[][] tileDirections = directions;
        final int[][] tileDistances = distances;
        final int[][] clipMasks = clip;
        final int[] writeBufferX = bufferX;
        final int[] writeBufferY = bufferY;

        // when we start searching for path, we position ourselves in the middle of graph
        // so the base(minimum) position is source_pos - HALF_GRAPH_SIZE.
        final int graphBaseX = srcX - (GRAPH_SIZE / 2);
        final int graphBaseY = srcY - (GRAPH_SIZE / 2);
        int currentX = srcX;
        int currentY = srcY;
        int currentGraphX = srcX - graphBaseX;
        int currentGraphY = srcY - graphBaseY;

        // setup information about source tile.
        tileDistances[currentGraphX][currentGraphY] = 0;
        tileDirections[currentGraphX][currentGraphY] = 99;

        // queue variables
        int read = 0, write = 0, nextDistance;
        // insert our current position as first queued position.
        writeBufferX[write] = currentX;
        writeBufferY[write++] = currentY;
        while (read != write) {
            currentX = writeBufferX[read];
            currentY = writeBufferY[read];
            read = (read + 1) & (QUEUE_SIZE - 1);

            currentGraphX = currentX - graphBaseX;
            currentGraphY = currentY - graphBaseY;

            if (strategy.canExit(currentX, currentY, 1, clipMasks, graphBaseX, graphBaseY)) {
                exitX = currentX;
                exitY = currentY;
                return true;
            }

            // if we can't exit at current tile, check where we can go from this tile
            nextDistance = tileDistances[currentGraphX][currentGraphY] + 1;
            if (currentGraphX > 0 && tileDirections[currentGraphX - 1][currentGraphY] == 0
                    && (clipMasks[currentGraphX - 1][currentGraphY] & (Flags.BLOCK_EAST | Flags.OCCUPIED_BLOCK_NPC)) == 0) {
                // we can go to west, queue it
                writeBufferX[write] = currentX - 1;
                writeBufferY[write] = currentY;
                write = (write + 1) & (QUEUE_SIZE - 1);

                tileDirections[currentGraphX - 1][currentGraphY] = DIR_EAST;
                tileDistances[currentGraphX - 1][currentGraphY] = nextDistance;
            }
            if (currentGraphX < (GRAPH_SIZE - 1) && tileDirections[currentGraphX + 1][currentGraphY] == 0
                    && (clipMasks[currentGraphX + 1][currentGraphY] & (Flags.BLOCK_WEST | Flags.OCCUPIED_BLOCK_NPC)) == 0) {
                // we can go to east, queue it
                writeBufferX[write] = currentX + 1;
                writeBufferY[write] = currentY;
                write = (write + 1) & (QUEUE_SIZE - 1);

                tileDirections[currentGraphX + 1][currentGraphY] = DIR_WEST;
                tileDistances[currentGraphX + 1][currentGraphY] = nextDistance;
            }
            if (currentGraphY > 0 && tileDirections[currentGraphX][currentGraphY - 1] == 0
                    && (clipMasks[currentGraphX][currentGraphY - 1] & (Flags.BLOCK_NORTH | Flags.OCCUPIED_BLOCK_NPC)) == 0) {
                // we can go to south, queue it
                writeBufferX[write] = currentX;
                writeBufferY[write] = currentY - 1;
                write = (write + 1) & (QUEUE_SIZE - 1);

                tileDirections[currentGraphX][currentGraphY - 1] = DIR_NORTH;
                tileDistances[currentGraphX][currentGraphY - 1] = nextDistance;
            }
            if (currentGraphY < (GRAPH_SIZE - 1) && tileDirections[currentGraphX][currentGraphY + 1] == 0
                    && (clipMasks[currentGraphX][currentGraphY + 1] & (Flags.BLOCK_SOUTH | Flags.OCCUPIED_BLOCK_NPC)) == 0) {
                // we can go to north, queue it
                writeBufferX[write] = currentX;
                writeBufferY[write] = currentY + 1;
                write = (write + 1) & (QUEUE_SIZE - 1);

                tileDirections[currentGraphX][currentGraphY + 1] = DIR_SOUTH;
                tileDistances[currentGraphX][currentGraphY + 1] = nextDistance;
            }
            // diagonal checks, comment them to disable diagonal routes.
            if (currentGraphX > 0 && currentGraphY > 0 && tileDirections[currentGraphX - 1][currentGraphY - 1] == 0
                    && (clipMasks[currentGraphX - 1][currentGraphY - 1] & (Flags.BLOCK_NORTH_EAST | Flags.OCCUPIED_BLOCK_NPC)) == 0
                    && (clipMasks[currentGraphX - 1][currentGraphY] & (Flags.BLOCK_EAST | Flags.OCCUPIED_BLOCK_NPC)) == 0
                    && (clip[currentGraphX][currentGraphY - 1] & (Flags.BLOCK_NORTH | Flags.OCCUPIED_BLOCK_NPC)) == 0) {
                // we can go to south west, queue it
                writeBufferX[write] = currentX - 1;
                writeBufferY[write] = currentY - 1;
                write = (write + 1) & (QUEUE_SIZE - 1);

                tileDirections[currentGraphX - 1][currentGraphY - 1] = DIR_NORTH | DIR_EAST;
                tileDistances[currentGraphX - 1][currentGraphY - 1] = nextDistance;
            }
            if (currentGraphX < (GRAPH_SIZE - 1) && currentGraphY > 0 && tileDirections[currentGraphX + 1][currentGraphY - 1] == 0
                    && (clipMasks[currentGraphX + 1][currentGraphY - 1] & (Flags.BLOCK_NORTH_WEST | Flags.OCCUPIED_BLOCK_NPC)) == 0
                    && (clipMasks[currentGraphX + 1][currentGraphY] & (Flags.BLOCK_WEST | Flags.OCCUPIED_BLOCK_NPC)) == 0
                    && (clipMasks[currentGraphX][currentGraphY - 1] & (Flags.BLOCK_NORTH | Flags.OCCUPIED_BLOCK_NPC)) == 0) {
                // we can go to south east, queue it
                writeBufferX[write] = currentX + 1;
                writeBufferY[write] = currentY - 1;
                write = (write + 1) & (QUEUE_SIZE - 1);

                tileDirections[currentGraphX + 1][currentGraphY - 1] = DIR_NORTH | DIR_WEST;
                tileDistances[currentGraphX + 1][currentGraphY - 1] = nextDistance;
            }
            if (currentGraphX > 0 && currentGraphY < (GRAPH_SIZE - 1) && tileDirections[currentGraphX - 1][currentGraphY + 1] == 0
                    && (clipMasks[currentGraphX - 1][currentGraphY + 1] & (Flags.BLOCK_SOUTH_EAST | Flags.OCCUPIED_BLOCK_NPC)) == 0
                    && (clipMasks[currentGraphX - 1][currentGraphY] & (Flags.BLOCK_EAST | Flags.OCCUPIED_BLOCK_NPC)) == 0
                    && (clipMasks[currentGraphX][currentGraphY + 1] & (Flags.BLOCK_SOUTH | Flags.OCCUPIED_BLOCK_NPC)) == 0) {
                // we can go to north west, queue it.
                writeBufferX[write] = currentX - 1;
                writeBufferY[write] = currentY + 1;
                write = (write + 1) & (QUEUE_SIZE - 1);

                tileDirections[currentGraphX - 1][currentGraphY + 1] = DIR_SOUTH | DIR_EAST;
                tileDistances[currentGraphX - 1][currentGraphY + 1] = nextDistance;
            }
            if (currentGraphX < (GRAPH_SIZE - 1) && currentGraphY < (GRAPH_SIZE - 1) && tileDirections[currentGraphX + 1][currentGraphY + 1] == 0
                    && (clipMasks[currentGraphX + 1][currentGraphY + 1] & (Flags.BLOCK_SOUTH_WEST | Flags.OCCUPIED_BLOCK_NPC)) == 0
                    && (clipMasks[currentGraphX + 1][currentGraphY] & (Flags.BLOCK_WEST | Flags.OCCUPIED_BLOCK_NPC)) == 0
                    && (clipMasks[currentGraphX][currentGraphY + 1] & (Flags.BLOCK_SOUTH | Flags.OCCUPIED_BLOCK_NPC)) == 0) {
                // we can go to north east, queue it.
                writeBufferX[write] = currentX + 1;
                writeBufferY[write] = currentY + 1;
                write = (write + 1) & (QUEUE_SIZE - 1);

                tileDirections[currentGraphX + 1][currentGraphY + 1] = DIR_SOUTH | DIR_WEST;
                tileDistances[currentGraphX + 1][currentGraphY + 1] = nextDistance;
            }

        }
        exitX = currentX;
        exitY = currentY;
        return false;
    }

    /**
     * Perform's size 2 calculations.
     */
    private static boolean performCalculationS2(final int srcX, final int srcY, final RouteStrategy strategy) {
        return performCalculationSX(srcX, srcY, 2, strategy); // TODO optimized algorhytm's.
    }

    /**
     * Perform's size x calculations.
     */
    private static boolean performCalculationSX(final int srcX, final int srcY, final int size, final RouteStrategy strategy) {
        // first, we will cache our static fields to local variables, this is done for performance, because
        // modern jit compiler's usually takes advantage of things like this
        final int[][] _directions = directions;
        final int[][] _distances = distances;
        final int[][] _clip = clip;
        final int[] _bufferX = bufferX;
        final int[] _bufferY = bufferY;

        // when we start searching for path, we position ourselves in the middle of graph
        // so the base(minimum) position is source_pos - HALF_GRAPH_SIZE.
        final int graphBaseX = srcX - (GRAPH_SIZE / 2);
        final int graphBaseY = srcY - (GRAPH_SIZE / 2);
        int currentX = srcX;
        int currentY = srcY;
        int currentGraphX = srcX - graphBaseX;
        int currentGraphY = srcY - graphBaseY;

        // setup information about source tile.
        _distances[currentGraphX][currentGraphY] = 0;
        _directions[currentGraphX][currentGraphY] = 99;

        // queue variables
        int read = 0, write = 0;
        // insert our current position as first queued position.
        _bufferX[write] = currentX;
        _bufferY[write++] = currentY;

        while (read != write) {
            currentX = _bufferX[read];
            currentY = _bufferY[read];
            read = (read + 1) & (QUEUE_SIZE - 1);

            currentGraphX = currentX - graphBaseX;
            currentGraphY = currentY - graphBaseY;

            if (strategy.canExit(currentX, currentY, size, _clip, graphBaseX, graphBaseY)) {
                // we found a path!
                exitX = currentX;
                exitY = currentY;
                return true;
            }

            // if we can't exit at current tile, check where we can go from this tile
            final int nextDistance = _distances[currentGraphX][currentGraphY] + 1;
            if (currentGraphX > 0 && _directions[currentGraphX - 1][currentGraphY] == 0 && (_clip[currentGraphX - 1][currentGraphY]
                    & (Flags.FLOOR | Flags.FLOOR_DECORATION | Flags.OBJECT | Flags.WALL_NORTH | Flags.WALL_EAST | Flags.CORNER_NORTH_EAST | Flags.OCCUPIED_BLOCK_NPC)) == 0 && (_clip[currentGraphX - 1][currentGraphY + (size - 1)]
                    & (Flags.FLOOR | Flags.FLOOR_DECORATION | Flags.OBJECT | Flags.WALL_EAST | Flags.WALL_SOUTH | Flags.CORNER_SOUTH_EAST | Flags.OCCUPIED_BLOCK_NPC)) == 0) {
                exit:
                {
                    for (int y = 1; y < (size - 1); y++) {
                        if ((_clip[currentGraphX - 1][currentGraphY + y]
                                & (Flags.FLOOR | Flags.FLOOR_DECORATION | Flags.OBJECT | Flags.WALL_NORTH | Flags.WALL_EAST | Flags.WALL_SOUTH | Flags.CORNER_NORTH_EAST | Flags.CORNER_SOUTH_EAST | Flags.OCCUPIED_BLOCK_NPC)) != 0) {
                            break exit;
                        }
                    }
                    // we can go to west, queue it
                    _bufferX[write] = currentX - 1;
                    _bufferY[write] = currentY;
                    write = (write + 1) & (QUEUE_SIZE - 1);

                    _directions[currentGraphX - 1][currentGraphY] = DIR_EAST;
                    _distances[currentGraphX - 1][currentGraphY] = nextDistance;
                }
            }
            if (currentGraphX < (GRAPH_SIZE - size) && _directions[currentGraphX + 1][currentGraphY] == 0 && (_clip[currentGraphX + size][currentGraphY]
                    & (Flags.FLOOR | Flags.FLOOR_DECORATION | Flags.OBJECT | Flags.WALL_NORTH | Flags.WALL_WEST | Flags.CORNER_NORTH_WEST | Flags.OCCUPIED_BLOCK_NPC)) == 0 && (_clip[currentGraphX + size][currentGraphY + (size - 1)]
                    & (Flags.FLOOR | Flags.FLOOR_DECORATION | Flags.OBJECT | Flags.WALL_SOUTH | Flags.WALL_WEST | Flags.CORNER_SOUTH_WEST | Flags.OCCUPIED_BLOCK_NPC)) == 0) {
                exit:
                {
                    for (int y = 1; y < (size - 1); y++) {
                        if ((_clip[currentGraphX + size][currentGraphY + y]
                                & (Flags.FLOOR | Flags.FLOOR_DECORATION | Flags.OBJECT | Flags.WALL_NORTH | Flags.WALL_SOUTH | Flags.WALL_WEST | Flags.CORNER_NORTH_WEST | Flags.CORNER_SOUTH_WEST | Flags.OCCUPIED_BLOCK_NPC)) != 0) {
                            break exit;
                        }
                    }
                    // we can go to east, queue it
                    _bufferX[write] = currentX + 1;
                    _bufferY[write] = currentY;
                    write = (write + 1) & (QUEUE_SIZE - 1);

                    _directions[currentGraphX + 1][currentGraphY] = DIR_WEST;
                    _distances[currentGraphX + 1][currentGraphY] = nextDistance;
                }
            }
            if (currentGraphY > 0 && _directions[currentGraphX][currentGraphY - 1] == 0 && (_clip[currentGraphX][currentGraphY - 1]
                    & (Flags.FLOOR | Flags.FLOOR_DECORATION | Flags.OBJECT | Flags.WALL_NORTH | Flags.WALL_EAST | Flags.CORNER_NORTH_EAST | Flags.OCCUPIED_BLOCK_NPC)) == 0 && (_clip[currentGraphX + (size - 1)][currentGraphY - 1]
                    & (Flags.FLOOR | Flags.FLOOR_DECORATION | Flags.OBJECT | Flags.WALL_NORTH | Flags.WALL_WEST | Flags.CORNER_NORTH_WEST | Flags.OCCUPIED_BLOCK_NPC)) == 0) {
                exit:
                {
                    for (int y = 1; y < (size - 1); y++) {
                        if ((_clip[currentGraphX + y][currentGraphY - 1]
                                & (Flags.FLOOR | Flags.FLOOR_DECORATION | Flags.OBJECT | Flags.WALL_NORTH | Flags.WALL_EAST | Flags.WALL_WEST | Flags.CORNER_NORTH_WEST | Flags.CORNER_NORTH_EAST | Flags.OCCUPIED_BLOCK_NPC)) != 0) {
                            break exit;
                        }
                    }
                    // we can go to south, queue it
                    _bufferX[write] = currentX;
                    _bufferY[write] = currentY - 1;
                    write = (write + 1) & (QUEUE_SIZE - 1);

                    _directions[currentGraphX][currentGraphY - 1] = DIR_NORTH;
                    _distances[currentGraphX][currentGraphY - 1] = nextDistance;
                }
            }
            if (currentGraphY < (GRAPH_SIZE - size) && _directions[currentGraphX][currentGraphY + 1] == 0 && (_clip[currentGraphX][currentGraphY + size]
                    & (Flags.FLOOR | Flags.FLOOR_DECORATION | Flags.OBJECT | Flags.WALL_EAST | Flags.WALL_SOUTH | Flags.CORNER_SOUTH_EAST | Flags.OCCUPIED_BLOCK_NPC)) == 0 && (_clip[currentGraphX + (size - 1)][currentGraphY + size]
                    & (Flags.FLOOR | Flags.FLOOR_DECORATION | Flags.OBJECT | Flags.WALL_SOUTH | Flags.WALL_WEST | Flags.CORNER_SOUTH_WEST | Flags.OCCUPIED_BLOCK_NPC)) == 0) {
                exit:
                {
                    for (int y = 1; y < (size - 1); y++) {
                        if ((_clip[currentGraphX + y][currentGraphY + size]
                                & (Flags.FLOOR | Flags.FLOOR_DECORATION | Flags.OBJECT | Flags.WALL_EAST | Flags.WALL_SOUTH | Flags.WALL_WEST | Flags.CORNER_SOUTH_EAST | Flags.CORNER_SOUTH_WEST | Flags.OCCUPIED_BLOCK_NPC)) != 0) {
                            break exit;
                        }
                    }
                    // we can go to north, queue it
                    _bufferX[write] = currentX;
                    _bufferY[write] = currentY + 1;
                    write = (write + 1) & (QUEUE_SIZE - 1);

                    _directions[currentGraphX][currentGraphY + 1] = DIR_SOUTH;
                    _distances[currentGraphX][currentGraphY + 1] = nextDistance;
                }
            }
            // diagonal checks, comment them to disable diagonal routes.
            if (currentGraphX > 0 && currentGraphY > 0 && _directions[currentGraphX - 1][currentGraphY - 1] == 0 && (_clip[currentGraphX - 1][currentGraphY - 1]
                    & (Flags.FLOOR | Flags.FLOOR_DECORATION | Flags.OBJECT | Flags.WALL_NORTH | Flags.WALL_EAST | Flags.CORNER_NORTH_EAST | Flags.OCCUPIED_BLOCK_NPC)) == 0) {
                exit:
                {
                    for (int y = 1; y < size; y++) {
                        if ((_clip[currentGraphX - 1][currentGraphY + (y - 1)]
                                & (Flags.FLOOR | Flags.FLOOR_DECORATION | Flags.OBJECT | Flags.WALL_NORTH | Flags.WALL_EAST | Flags.WALL_SOUTH | Flags.CORNER_NORTH_EAST | Flags.CORNER_SOUTH_EAST | Flags.OCCUPIED_BLOCK_NPC)) != 0 || (_clip[currentGraphX + (y - 1)][currentGraphY - 1] & (Flags.FLOOR | Flags.FLOOR_DECORATION | Flags.OBJECT | Flags.WALL_NORTH | Flags.WALL_EAST | Flags.WALL_WEST | Flags.CORNER_NORTH_WEST | Flags.CORNER_NORTH_EAST | Flags.OCCUPIED_BLOCK_NPC)) != 0) {
                            break exit;
                        }
                    }
                    // we can go to south west, queue it
                    _bufferX[write] = currentX - 1;
                    _bufferY[write] = currentY - 1;
                    write = (write + 1) & (QUEUE_SIZE - 1);

                    _directions[currentGraphX - 1][currentGraphY - 1] = DIR_NORTH | DIR_EAST;
                    _distances[currentGraphX - 1][currentGraphY - 1] = nextDistance;
                }
            }
            if (currentGraphX < (GRAPH_SIZE - size) && currentGraphY > 0 && _directions[currentGraphX + 1][currentGraphY - 1] == 0 && (_clip[currentGraphX + size][currentGraphY - 1]
                    & (Flags.FLOOR | Flags.FLOOR_DECORATION | Flags.OBJECT | Flags.WALL_NORTH | Flags.WALL_WEST | Flags.CORNER_NORTH_WEST | Flags.OCCUPIED_BLOCK_NPC)) == 0) {
                exit:
                {
                    for (int y = 1; y < size; y++) {
                        if ((_clip[currentGraphX + size][currentGraphY + (y - 1)]
                                & (Flags.FLOOR | Flags.FLOOR_DECORATION | Flags.OBJECT | Flags.WALL_NORTH | Flags.WALL_SOUTH | Flags.WALL_WEST | Flags.CORNER_NORTH_WEST | Flags.CORNER_SOUTH_WEST | Flags.OCCUPIED_BLOCK_NPC)) != 0 || (_clip[currentGraphX + y][currentGraphY - 1] & (Flags.FLOOR | Flags.FLOOR_DECORATION | Flags.OBJECT | Flags.WALL_NORTH | Flags.WALL_EAST | Flags.WALL_WEST | Flags.CORNER_NORTH_WEST | Flags.CORNER_NORTH_EAST | Flags.OCCUPIED_BLOCK_NPC)) != 0) {
                            break exit;
                        }
                    }
                    // we can go to south east, queue it
                    _bufferX[write] = currentX + 1;
                    _bufferY[write] = currentY - 1;
                    write = (write + 1) & (QUEUE_SIZE - 1);

                    _directions[currentGraphX + 1][currentGraphY - 1] = DIR_NORTH | DIR_WEST;
                    _distances[currentGraphX + 1][currentGraphY - 1] = nextDistance;
                }
            }
            if (currentGraphX > 0 && currentGraphY < (GRAPH_SIZE - size) && _directions[currentGraphX - 1][currentGraphY + 1] == 0 && (_clip[currentGraphX - 1][currentGraphY + size]
                    & (Flags.FLOOR | Flags.FLOOR_DECORATION | Flags.OBJECT | Flags.WALL_EAST | Flags.WALL_SOUTH | Flags.CORNER_SOUTH_EAST | Flags.OCCUPIED_BLOCK_NPC)) == 0) {
                exit:
                {
                    for (int y = 1; y < size; y++) {
                        if ((_clip[currentGraphX - 1][currentGraphY + y] & (Flags.FLOOR | Flags.FLOOR_DECORATION | Flags.OBJECT | Flags.WALL_NORTH | Flags.WALL_EAST | Flags.WALL_SOUTH | Flags.CORNER_NORTH_EAST | Flags.CORNER_SOUTH_EAST | Flags.OCCUPIED_BLOCK_NPC)) != 0 || (_clip[currentGraphX + (y - 1)][currentGraphY + size] & (Flags.FLOOR | Flags.FLOOR_DECORATION | Flags.OBJECT | Flags.WALL_EAST | Flags.WALL_SOUTH | Flags.WALL_WEST | Flags.CORNER_SOUTH_EAST | Flags.CORNER_SOUTH_WEST | Flags.OCCUPIED_BLOCK_NPC)) != 0) {
                            break exit;
                        }
                    }
                    // we can go to north west, queue it.
                    _bufferX[write] = currentX - 1;
                    _bufferY[write] = currentY + 1;
                    write = (write + 1) & (QUEUE_SIZE - 1);

                    _directions[currentGraphX - 1][currentGraphY + 1] = DIR_SOUTH | DIR_EAST;
                    _distances[currentGraphX - 1][currentGraphY + 1] = nextDistance;
                }
            }
            if (currentGraphX < (GRAPH_SIZE - size) && currentGraphY < (GRAPH_SIZE - size) && _directions[currentGraphX + 1][currentGraphY + 1] == 0 && (_clip[currentGraphX + size][currentGraphY + size] & (Flags.FLOOR | Flags.FLOOR_DECORATION | Flags.OBJECT | Flags.WALL_SOUTH | Flags.WALL_WEST | Flags.CORNER_SOUTH_WEST | Flags.OCCUPIED_BLOCK_NPC)) == 0) {
                exit:
                {
                    for (int y = 1; y < size; y++) {
                        if ((_clip[currentGraphX + y][currentGraphY + size] & (Flags.FLOOR | Flags.FLOOR_DECORATION | Flags.OBJECT | Flags.WALL_EAST | Flags.WALL_SOUTH | Flags.WALL_WEST | Flags.CORNER_SOUTH_EAST | Flags.CORNER_SOUTH_WEST | Flags.OCCUPIED_BLOCK_NPC)) != 0 || (_clip[currentGraphX + size][currentGraphY + y] & (Flags.FLOOR | Flags.FLOOR_DECORATION | Flags.OBJECT | Flags.WALL_NORTH | Flags.WALL_SOUTH | Flags.WALL_WEST | Flags.CORNER_NORTH_WEST | Flags.CORNER_SOUTH_WEST | Flags.OCCUPIED_BLOCK_NPC)) != 0) {
                            break exit;
                        }
                    }
                    // we can go to north east, queue it.
                    _bufferX[write] = currentX + 1;
                    _bufferY[write] = currentY + 1;
                    write = (write + 1) & (QUEUE_SIZE - 1);

                    _directions[currentGraphX + 1][currentGraphY + 1] = DIR_SOUTH | DIR_WEST;
                    _distances[currentGraphX + 1][currentGraphY + 1] = nextDistance;
                }
            }

        }

        exitX = currentX;
        exitY = currentY;
        return false;
    }

    /**
     * Transmit's clip map to route finder buffers.
     */
    private static void transmitClipData(final int x, final int y, final int z) {
        final int graphBaseX = x - (GRAPH_SIZE / 2);
        final int graphBaseY = y - (GRAPH_SIZE / 2);
        for (int transmitRegionX = graphBaseX >> 6; transmitRegionX <= (graphBaseX + (GRAPH_SIZE - 1)) >> 6; transmitRegionX++) {
            for (int transmitRegionY = graphBaseY >> 6; transmitRegionY <= (graphBaseY + (GRAPH_SIZE - 1)) >> 6; transmitRegionY++) {
                final int startX = Math.max(graphBaseX, transmitRegionX << 6), startY = Math.max(graphBaseY, transmitRegionY << 6);
                final int endX = Math.min(graphBaseX + GRAPH_SIZE, (transmitRegionX << 6) + 64), endY = Math.min(graphBaseY + GRAPH_SIZE, (transmitRegionY << 6) + 64);
                final Region region = World.getRegion(transmitRegionX << 8 | transmitRegionY, true);
                final RegionMap map = region.getRegionMap(true);
                if (map == null || region.getLoadStage() != 2) {
                    for (int fillX = startX; fillX < endX; fillX++) {
                        for (int fillY = startY; fillY < endY; fillY++) {
                            clip[fillX - graphBaseX][fillY - graphBaseY] = -1;
                        }
                    }
                    continue;
                }


                final int[][] masks = map.getMasks()[z];
                for (int fillX = startX; fillX < endX; fillX++) {
                    for (int fillY = startY; fillY < endY; fillY++) {
                        clip[fillX - graphBaseX][fillY - graphBaseY] = masks[fillX & 0x3F][fillY & 0x3F];
                    }
                }
            }
        }
    }
}