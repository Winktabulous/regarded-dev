package com.zenyte.game.world.region;

import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.pathfinding.Flags;

public class RegionMap {

    private final int[][][] masks;
    private final int hash;

    RegionMap(final int regionId) {
        hash = ((regionId & 255) * 64) | ((regionId >> 8) * 64) << 14;
        masks = new int[4][64][64];
    }

    public static final boolean checkWalkStep(final int plane, final int x, final int y, final int xOffset, final int yOffset, final int size, final boolean checkCollidingNPCs, final boolean checkCollidingPlayers) {
        int collisionFlag = 0;
        if (checkCollidingNPCs) {
            collisionFlag |= Flags.OCCUPIED_BLOCK_NPC;
        }
        if (checkCollidingPlayers) {
            collisionFlag |= Flags.OCCUPIED_BLOCK_PLAYER;
        }
        if (size == 1) {
            final int mask = World.getMask(plane, x + xOffset, y + yOffset);
            if ((mask & collisionFlag) != 0) return false;
            if (xOffset == -1 && yOffset == 0) {
                return (mask & (Flags.BLOCK_EAST)) == 0;
            }
            if (xOffset == 1 && yOffset == 0) {
                return (mask & (Flags.BLOCK_WEST)) == 0;
            }
            if (xOffset == 0 && yOffset == -1) {
                return (mask & (Flags.BLOCK_NORTH)) == 0;
            }
            if (xOffset == 0 && yOffset == 1) {
                return (mask & (Flags.BLOCK_SOUTH)) == 0;
            }
            if (xOffset == -1 && yOffset == -1) {
                return (mask & (Flags.BLOCK_NORTH_EAST)) == 0 && (World.getMask(plane, x - 1, y) & (Flags.BLOCK_EAST)) == 0 && (World.getMask(plane, x, y - 1) & (Flags.BLOCK_NORTH)) == 0;
            }
            if (xOffset == 1 && yOffset == -1) {
                return (mask & (Flags.BLOCK_NORTH_WEST)) == 0 && (World.getMask(plane, x + 1, y) & (Flags.BLOCK_WEST)) == 0 && (World.getMask(plane, x, y - 1) & (Flags.BLOCK_NORTH)) == 0;
            }
            if (xOffset == -1 && yOffset == 1) {
                return (mask & (Flags.BLOCK_SOUTH_EAST)) == 0 && (World.getMask(plane, x - 1, y) & (Flags.BLOCK_EAST)) == 0 && (World.getMask(plane, x, y + 1) & (Flags.BLOCK_SOUTH)) == 0;
            }
            if (xOffset == 1 && yOffset == 1) {
                return (mask & (Flags.BLOCK_SOUTH_WEST)) == 0 && (World.getMask(plane, x + 1, y) & (Flags.BLOCK_WEST)) == 0 && (World.getMask(plane, x, y + 1) & (Flags.BLOCK_SOUTH)) == 0;
            }
        } else if (size == 2) {
            if (xOffset == -1 && yOffset == 0) {
                return (World.getMask(plane, x - 1, y) & (Flags.BLOCK_NORTH_EAST | collisionFlag)) == 0 && (World.getMask(plane, x - 1, y + 1) & (Flags.BLOCK_SOUTH_EAST | collisionFlag)) == 0;
            }
            if (xOffset == 1 && yOffset == 0) {
                return (World.getMask(plane, x + 2, y) & (Flags.BLOCK_NORTH_WEST | collisionFlag)) == 0 && (World.getMask(plane, x + 2, y + 1) & (Flags.BLOCK_SOUTH_WEST | collisionFlag)) == 0;
            }
            if (xOffset == 0 && yOffset == -1) {
                return (World.getMask(plane, x, y - 1) & (Flags.BLOCK_NORTH_EAST | collisionFlag)) == 0 && (World.getMask(plane, x + 1, y - 1) & (Flags.BLOCK_NORTH_WEST | collisionFlag)) == 0;
            }
            if (xOffset == 0 && yOffset == 1) {
                return (World.getMask(plane, x, y + 2) & (Flags.BLOCK_SOUTH_EAST | collisionFlag)) == 0 && (World.getMask(plane, x + 1, y + 2) & (Flags.BLOCK_SOUTH_WEST | collisionFlag)) == 0;
            }
            if (xOffset == -1 && yOffset == -1) {
                return (World.getMask(plane, x - 1, y) & (Flags.BLOCK_NORTH_EAST | Flags.BLOCK_SOUTH_EAST | collisionFlag)) == 0 && (World.getMask(plane, x - 1, y - 1) & (Flags.BLOCK_NORTH_EAST | collisionFlag)) == 0 && (World.getMask(plane, x, y - 1) & (Flags.BLOCK_NORTH_WEST | Flags.BLOCK_NORTH_EAST | collisionFlag)) == 0;
            }
            if (xOffset == 1 && yOffset == -1) {
                return (World.getMask(plane, x + 1, y - 1) & (Flags.BLOCK_NORTH_WEST | Flags.BLOCK_NORTH_EAST | collisionFlag)) == 0 && (World.getMask(plane, x + 2, y - 1) & (Flags.BLOCK_NORTH_WEST | collisionFlag)) == 0 && (World.getMask(plane, x + 2, y) & (Flags.BLOCK_NORTH_WEST | Flags.BLOCK_SOUTH_WEST | collisionFlag)) == 0;
            }
            if (xOffset == -1 && yOffset == 1) {
                return (World.getMask(plane, x - 1, y + 1) & (Flags.BLOCK_NORTH_EAST | Flags.BLOCK_SOUTH_EAST | collisionFlag)) == 0 && (World.getMask(plane, x - 1, y + 2) & (Flags.BLOCK_SOUTH_EAST | collisionFlag)) == 0 && (World.getMask(plane, x, y + 2) & (Flags.BLOCK_SOUTH_EAST | Flags.BLOCK_SOUTH_WEST | collisionFlag)) == 0;
            }
            if (xOffset == 1 && yOffset == 1) {
                return (World.getMask(plane, x + 1, y + 2) & (Flags.BLOCK_SOUTH_EAST | Flags.BLOCK_SOUTH_WEST | collisionFlag)) == 0 && (World.getMask(plane, x + 2, y + 2) & (Flags.BLOCK_SOUTH_WEST | collisionFlag)) == 0 && (World.getMask(plane, x + 1, y + 1) & (Flags.BLOCK_NORTH_WEST | Flags.BLOCK_SOUTH_WEST | collisionFlag)) == 0;
            }
        } else {
            if (xOffset == -1 && yOffset == 0) {
                if ((World.getMask(plane, x - 1, y) & (Flags.BLOCK_NORTH_EAST | collisionFlag)) != 0 || (World.getMask(plane, x - 1, -1 + (y + size)) & (Flags.BLOCK_SOUTH_EAST | collisionFlag)) != 0) {
                    return false;
                }
                for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
                    if ((World.getMask(plane, x - 1, y + sizeOffset) & (Flags.BLOCK_NORTH_EAST | Flags.BLOCK_SOUTH_EAST | collisionFlag)) != 0) {
                        return false;
                    }
                }
            } else if (xOffset == 1 && yOffset == 0) {
                if ((World.getMask(plane, x + size, y) & (Flags.BLOCK_NORTH_WEST | collisionFlag)) != 0 || (World.getMask(plane, x + size, y - (-size + 1)) & (Flags.BLOCK_SOUTH_WEST | collisionFlag)) != 0) {
                    return false;
                }
                for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
                    if ((World.getMask(plane, x + size, y + sizeOffset) & (Flags.BLOCK_NORTH_WEST | Flags.BLOCK_SOUTH_WEST | collisionFlag)) != 0) {
                        return false;
                    }
                }
            } else if (xOffset == 0 && yOffset == -1) {
                if ((World.getMask(plane, x, y - 1) & (Flags.BLOCK_NORTH_EAST | collisionFlag)) != 0 || (World.getMask(plane, x + size - 1, y - 1) & (Flags.BLOCK_NORTH_WEST | collisionFlag)) != 0) {
                    return false;
                }
                for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
                    if ((World.getMask(plane, x + sizeOffset, y - 1) & (Flags.BLOCK_NORTH_WEST | Flags.BLOCK_NORTH_EAST | collisionFlag)) != 0) {
                        return false;
                    }
                }
            } else if (xOffset == 0 && yOffset == 1) {
                if ((World.getMask(plane, x, y + size) & (Flags.BLOCK_SOUTH_EAST | collisionFlag)) != 0 || (World.getMask(plane, x + (size - 1), y + size) & (Flags.BLOCK_SOUTH_WEST | collisionFlag)) != 0) {
                    return false;
                }
                for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
                    if ((World.getMask(plane, x + sizeOffset, y + size) & (Flags.BLOCK_SOUTH_EAST | Flags.BLOCK_SOUTH_WEST | collisionFlag)) != 0) {
                        return false;
                    }
                }
            } else if (xOffset == -1 && yOffset == -1) {
                if ((World.getMask(plane, x - 1, y - 1) & (Flags.BLOCK_NORTH_EAST | collisionFlag)) != 0) {
                    return false;
                }
                for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
                    if ((World.getMask(plane, x - 1, y + (-1 + sizeOffset)) & (Flags.BLOCK_NORTH_EAST | Flags.BLOCK_SOUTH_EAST | collisionFlag)) != 0 || (World.getMask(plane, sizeOffset - 1 + x, y - 1) & (Flags.BLOCK_NORTH_WEST | Flags.BLOCK_NORTH_EAST | collisionFlag)) != 0) {
                        return false;
                    }
                }
            } else if (xOffset == 1 && yOffset == -1) {
                if ((World.getMask(plane, x + size, y - 1) & (Flags.BLOCK_NORTH_WEST | collisionFlag)) != 0) {
                    return false;
                }
                for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
                    if ((World.getMask(plane, x + size, sizeOffset + (-1 + y)) & (Flags.BLOCK_NORTH_WEST | Flags.BLOCK_SOUTH_WEST | collisionFlag)) != 0 || (World.getMask(plane, x + sizeOffset, y - 1) & (Flags.BLOCK_NORTH_WEST | Flags.BLOCK_NORTH_EAST | collisionFlag)) != 0) {
                        return false;
                    }
                }
            } else if (xOffset == -1 && yOffset == 1) {
                if ((World.getMask(plane, x - 1, y + size) & (Flags.BLOCK_SOUTH_EAST | collisionFlag)) != 0) {
                    return false;
                }
                for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
                    if ((World.getMask(plane, x - 1, y + sizeOffset) & (Flags.BLOCK_NORTH_EAST | Flags.BLOCK_SOUTH_EAST | collisionFlag)) != 0 || (World.getMask(plane, -1 + (x + sizeOffset), y + size) & (Flags.BLOCK_SOUTH_EAST | Flags.BLOCK_SOUTH_WEST | collisionFlag)) != 0) {
                        return false;
                    }
                }
            } else if (xOffset == 1 && yOffset == 1) {
                if ((World.getMask(plane, x + size, y + size) & (Flags.BLOCK_SOUTH_WEST | collisionFlag)) != 0) {
                    return false;
                }
                for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
                    if ((World.getMask(plane, x + sizeOffset, y + size) & (Flags.BLOCK_SOUTH_EAST | Flags.BLOCK_SOUTH_WEST | collisionFlag)) != 0 || (World.getMask(plane, x + size, y + sizeOffset) & (Flags.BLOCK_NORTH_WEST | Flags.BLOCK_SOUTH_WEST | collisionFlag)) != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public int getRegionX() {
        return (hash >> 14) & 16383;
    }

    public int getRegionY() {
        return hash & 16383;
    }

    synchronized int getLocalMask(final int plane, final int x, final int y) {
        return masks[plane][x][y];
    }

    public void setFloor(int plane, int localX, int localY, boolean add) {
        this.setFlag(plane, localX, localY, Flags.FLOOR_DECORATION, add);
    }

    void setObject(int plane, int x, int y, int width, int height, boolean blocksFly, boolean add) {
        int flag = Flags.OBJECT;
        if (blocksFly) {
            flag += Flags.OBJECT_PROJECTILE;
        }
        for (int tileX = x; tileX < width + x; ++tileX) {
            for (int tileY = y; tileY < y + height; ++tileY) {
                this.setFlag(plane, tileX, tileY, flag, add);
            }
        }
    }

    void setWall(int plane, int x, int y, int type, int rotation, boolean blocksFly, boolean add) {
        if (type == 0) {
            if (rotation == 0) {
                this.setFlag(plane, x, y, Flags.WALL_WEST, add);
                this.setFlag(plane, x - 1, y, Flags.WALL_EAST, add);
            }
            if (rotation == 1) {
                this.setFlag(plane, x, y, Flags.WALL_NORTH, add);
                this.setFlag(plane, x, y + 1, Flags.WALL_SOUTH, add);
            }
            if (rotation == 2) {
                this.setFlag(plane, x, y, Flags.WALL_EAST, add);
                this.setFlag(plane, x + 1, y, Flags.WALL_WEST, add);
            }
            if (rotation == 3) {
                this.setFlag(plane, x, y, Flags.WALL_SOUTH, add);
                this.setFlag(plane, x, y - 1, Flags.WALL_NORTH, add);
            }
        }
        if (type == 1 || type == 3) {
            if (rotation == 0) {
                this.setFlag(plane, x, y, Flags.CORNER_NORTH_WEST, add);
                this.setFlag(plane, x - 1, y + 1, Flags.CORNER_SOUTH_EAST, add);
            }
            if (rotation == 1) {
                this.setFlag(plane, x, y, Flags.CORNER_NORTH_EAST, add);
                this.setFlag(plane, x + 1, y + 1, Flags.CORNER_SOUTH_WEST, add);
            }
            if (rotation == 2) {
                this.setFlag(plane, x, y, Flags.CORNER_SOUTH_EAST, add);
                this.setFlag(plane, x + 1, y - 1, Flags.CORNER_NORTH_WEST, add);
            }
            if (rotation == 3) {
                this.setFlag(plane, x, y, Flags.CORNER_SOUTH_WEST, add);
                this.setFlag(plane, x - 1, y - 1, Flags.CORNER_NORTH_EAST, add);
            }
        }
        if (type == 2) {
            if (rotation == 0) {
                this.setFlag(plane, x, y, Flags.WALL_NORTH_WEST, add);
                this.setFlag(plane, x - 1, y, Flags.WALL_EAST, add);
                this.setFlag(plane, x, y + 1, Flags.WALL_SOUTH, add);
            }
            if (rotation == 1) {
                this.setFlag(plane, x, y, Flags.WALL_NORTH_EAST, add);
                this.setFlag(plane, x, y + 1, Flags.WALL_SOUTH, add);
                this.setFlag(plane, x + 1, y, Flags.WALL_WEST, add);
            }
            if (rotation == 2) {
                this.setFlag(plane, x, y, Flags.WALL_SOUTH_EAST, add);
                this.setFlag(plane, x + 1, y, Flags.WALL_WEST, add);
                this.setFlag(plane, x, y - 1, Flags.WALL_NORTH, add);
            }
            if (rotation == 3) {
                this.setFlag(plane, x, y, Flags.WALL_SOUTH_WEST, add);
                this.setFlag(plane, x, y - 1, Flags.WALL_NORTH, add);
                this.setFlag(plane, x - 1, y, Flags.WALL_EAST, add);
            }
        }
        if (blocksFly) {
            if (type == 0) {
                if (rotation == 0) {
                    this.setFlag(plane, x, y, Flags.WALL_WEST_PROJECTILE, add);
                    this.setFlag(plane, x - 1, y, Flags.WALL_EAST_PROJECTILE, add);
                }
                if (rotation == 1) {
                    this.setFlag(plane, x, y, Flags.WALL_NORTH_PROJECTILE, add);
                    this.setFlag(plane, x, y + 1, Flags.WALL_SOUTH_PROJECTILE, add);
                }
                if (rotation == 2) {
                    this.setFlag(plane, x, y, Flags.WALL_EAST_PROJECTILE, add);
                    this.setFlag(plane, x + 1, y, Flags.WALL_WEST_PROJECTILE, add);
                }
                if (rotation == 3) {
                    this.setFlag(plane, x, y, Flags.WALL_SOUTH_PROJECTILE, add);
                    this.setFlag(plane, x, y - 1, Flags.WALL_NORTH_PROJECTILE, add);
                }
            }
            if (type == 1 || type == 3) {
                if (rotation == 0) {
                    this.setFlag(plane, x, y, Flags.CORNER_NORTH_WEST_PROJECTILE, add);
                    this.setFlag(plane, x - 1, y + 1, Flags.CORNER_SOUTH_EAST_PROJECTILE, add);
                }
                if (rotation == 1) {
                    this.setFlag(plane, x, y, Flags.CORNER_NORTH_EAST_PROJECTILE, add);
                    this.setFlag(plane, x + 1, y + 1, Flags.CORNER_SOUTH_WEST_PROJECTILE, add);
                }
                if (rotation == 2) {
                    this.setFlag(plane, x, y, Flags.CORNER_SOUTH_EAST_PROJECTILE, add);
                    this.setFlag(plane, x + 1, y - 1, Flags.CORNER_NORTH_WEST_PROJECTILE, add);
                }
                if (rotation == 3) {
                    this.setFlag(plane, x, y, Flags.CORNER_SOUTH_WEST_PROJECTILE, add);
                    this.setFlag(plane, x - 1, y - 1, Flags.CORNER_NORTH_EAST_PROJECTILE, add);
                }
            }
            if (type == 2) {
                if (rotation == 0) {
                    this.setFlag(plane, x, y, Flags.WALL_NORTH_WEST_PROJECTILE, add);
                    this.setFlag(plane, x - 1, y, Flags.WALL_EAST_PROJECTILE, add);
                    this.setFlag(plane, x, y + 1, Flags.WALL_SOUTH_PROJECTILE, add);
                }
                if (rotation == 1) {
                    this.setFlag(plane, x, y, Flags.WALL_NORTH_EAST_PROJECTILE, add);
                    this.setFlag(plane, x, y + 1, Flags.WALL_SOUTH_PROJECTILE, add);
                    this.setFlag(plane, x + 1, y, Flags.WALL_WEST_PROJECTILE, add);
                }
                if (rotation == 2) {
                    this.setFlag(plane, x, y, Flags.WALL_SOUTH_EAST_PROJECTILE, add);
                    this.setFlag(plane, x + 1, y, Flags.WALL_WEST_PROJECTILE, add);
                    this.setFlag(plane, x, y - 1, Flags.WALL_NORTH_PROJECTILE, add);
                }
                if (rotation == 3) {
                    this.setFlag(plane, x, y, Flags.WALL_SOUTH_WEST_PROJECTILE, add);
                    this.setFlag(plane, x, y - 1, Flags.WALL_NORTH_PROJECTILE, add);
                    this.setFlag(plane, x - 1, y, Flags.WALL_EAST_PROJECTILE, add);
                }
            }
        }
    }

    synchronized void setFlag(int plane, int localX, int localY, int mask, boolean add) {
        if (localX >= 64 || localY >= 64 || localX < 0 || localY < 0) {
            final int x = getRegionX() + localX;
            final int y = getRegionY() + localY;
            final int regionId = ((x >> 6) << 8) + (y >> 6);
            final int regionX = (regionId >> 8) << 6;
            final int regionY = (regionId & 255) << 6;
            World.getRegion(regionId).getRegionMap(true).setFlag(plane, x - regionX, y - regionY, mask, add);
            return;
        }
        if (add) {
            masks[plane][localX][localY] |= mask;
        } else {
            masks[plane][localX][localY] &= ~mask;
        }
    }

    synchronized void setFlag(final int plane, final int x, final int y, final int mask) {
        masks[plane][x][y] = mask;
    }

    public int[][][] getMasks() {
        return masks;
    }
}
