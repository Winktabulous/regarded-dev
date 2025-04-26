package com.zenyte.game.model;

import com.zenyte.game.world.entity.Entity;

/**
 * @author Kris | 11. dets 2017 : 23:55.41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class HintArrow {

	/**
     * The entity (NPC or Player) on-top of whom the arrow should be placed.
     */
    private final Entity target;

    /**
     * The position of the arrow within the specified tile.
     */
    private final HintArrowPosition position;

    /**
     * The x coordinate of the hint arrow.
     */
    private final int x;

    /**
     * The y coordinate of the hint arrow.
     */
    private final int y;

    /**
     * The height of the hint arrow, values go from 0 to 255,
     * zero being on the ground and 255 being about 3 player heights.
     */
    private final byte height;
	
	public HintArrow(final Entity target) {
		this.target = target;
		this.position = HintArrowPosition.ENTITY;
		this.x = 0;
		this.y = 0;
		this.height = 0;
	}

	public HintArrow(final int x, final int y, final byte height) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.target = null;
		this.position = HintArrowPosition.CENTER;
    }

    public HintArrow(final int x, final int y, final byte height, final HintArrowPosition position) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.position = position;
        this.target = null;
    }

    public Entity getTarget() {
        return target;
    }

    public HintArrowPosition getPosition() {
        return position;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public byte getHeight() {
        return height;
    }

}
