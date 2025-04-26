package com.zenyte.game.world.entity.npc.spawns;

import com.zenyte.game.util.Direction;

public final class NPCSpawn {
	private int id;
	private int x;
	private int y;
	private int z;
	private Direction direction = Direction.SOUTH;
	private Integer radius = 0;

	public NPCSpawn(int id, int x, int y, int z, Direction direction, Integer radius) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
		this.direction = direction;
		this.radius = radius;
	}

	public NPCSpawn() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public Integer getRadius() {
		return radius;
	}

	public void setRadius(Integer radius) {
		this.radius = radius;
	}

	@Override
	public String toString() {
		return "NPCSpawn(id=" + this.getId() + ", x=" + this.getX() + ", y=" + this.getY() + ", z=" + this.getZ() + ", direction=" + this.getDirection() + ", radius=" + this.getRadius() + ")";
	}
	//@Getter @Setter private int knownIndex;
}
