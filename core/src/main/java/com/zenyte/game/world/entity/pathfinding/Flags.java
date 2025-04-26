package com.zenyte.game.world.entity.pathfinding;

/**
 * @author Kris | 28. sept 2018 : 19:53:16
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public final class Flags {

	public static final int OCCUPIED_BLOCK_NPC = 0x1000000;
	public static final int OCCUPIED_PROJECTILE_BLOCK_NPC = 0x2000000;
	public static final int OCCUPIED_BLOCK_PLAYER = 0x4000000;
	public static final int OCCUPIED_PROJECTILE_BLOCK_PLAYER = 0x8000000;

	
	public static final int FLOOR = 0x200000;
	public static final int FLOOR_DECORATION = 0x40000;

	public static final int OBJECT = 0x100;
	public static final int OBJECT_PROJECTILE = 0x20000;

	public static final int WALL_NORTH = 0x2;
	public static final int WALL_EAST = 0x8;
	public static final int WALL_SOUTH = 0x20;
	public static final int WALL_WEST = 0x80;

	public static final int CORNER_NORTH_WEST = 0x1;
	public static final int CORNER_NORTH_EAST = 0x4;
	public static final int CORNER_SOUTH_EAST = 0x10;
	public static final int CORNER_SOUTH_WEST = 0x40;

	public static final int WALL_NORTH_PROJECTILE = 0x400;
	public static final int WALL_EAST_PROJECTILE = 0x1000;
	public static final int WALL_SOUTH_PROJECTILE = 0x4000;
	public static final int WALL_WEST_PROJECTILE = 0x10000;

    public static final int CORNER_NORTH_WEST_PROJECTILE = 0x200;
    public static final int CORNER_NORTH_EAST_PROJECTILE = 0x800;
    public static final int CORNER_SOUTH_EAST_PROJECTILE = 0x2000;
    public static final int CORNER_SOUTH_WEST_PROJECTILE = 0x8000;


    /**
     * Combination flags
     */
    public static final int WALL_NORTH_WEST = WALL_NORTH | WALL_WEST;
    public static final int WALL_NORTH_EAST = WALL_NORTH | WALL_EAST;
    public static final int WALL_SOUTH_EAST = WALL_SOUTH | WALL_EAST;
    public static final int WALL_SOUTH_WEST = WALL_SOUTH | WALL_WEST;

    public static final int WALL_NORTH_WEST_PROJECTILE = WALL_NORTH_PROJECTILE | WALL_WEST_PROJECTILE;
    public static final int WALL_NORTH_EAST_PROJECTILE = WALL_NORTH_PROJECTILE | WALL_EAST_PROJECTILE;
    public static final int WALL_SOUTH_EAST_PROJECTILE = WALL_SOUTH_PROJECTILE | WALL_EAST_PROJECTILE;
    public static final int WALL_SOUTH_WEST_PROJECTILE = WALL_SOUTH_PROJECTILE | WALL_WEST_PROJECTILE;

    public static final int BLOCK_EAST = FLOOR | FLOOR_DECORATION | OBJECT | WALL_EAST;
    public static final int BLOCK_WEST = FLOOR | FLOOR_DECORATION | OBJECT | WALL_WEST;
    public static final int BLOCK_NORTH = FLOOR | FLOOR_DECORATION | OBJECT | WALL_NORTH;
    public static final int BLOCK_SOUTH = FLOOR | FLOOR_DECORATION | OBJECT | WALL_SOUTH;

    public static final int BLOCK_NORTH_EAST = BLOCK_EAST | BLOCK_NORTH | CORNER_NORTH_EAST;
    public static final int BLOCK_NORTH_WEST = BLOCK_NORTH | BLOCK_WEST | CORNER_NORTH_WEST;
    public static final int BLOCK_SOUTH_EAST = BLOCK_SOUTH | BLOCK_EAST | CORNER_SOUTH_EAST;
    public static final int BLOCK_SOUTH_WEST = BLOCK_SOUTH | BLOCK_WEST | CORNER_SOUTH_WEST;

}
