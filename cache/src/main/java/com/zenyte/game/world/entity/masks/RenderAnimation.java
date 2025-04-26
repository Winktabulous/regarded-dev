package com.zenyte.game.world.entity.masks;

/**
 * @author Kris | 6. nov 2017 : 14:36.04
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class RenderAnimation implements RenderType {

	public static final int STAND = 808, STAND_TURN = 823, WALK = 819, ROTATE180 = 820, ROTATE90 = 821, ROTATE270 =
            822, RUN = 824;
	public static final RenderAnimation DEFAULT_RENDER = new RenderAnimation(STAND, STAND_TURN, WALK, ROTATE180, ROTATE90, ROTATE270, RUN);
	
	private final int stand, standTurn, walk, rotate180, rotate90, rotate270, run;
	
	public RenderAnimation(final int stand, final int walk, final int run) {
		this(stand, STAND_TURN, walk, ROTATE180, ROTATE90, ROTATE270, run);
	}
	
	public RenderAnimation(int stand, int standTurn, int walk, int rotate180, int rotate90, int rotate270, int run) {
	    this.stand = stand;
	    this.standTurn = standTurn;
	    this.walk = walk;
	    this.rotate180 = rotate180;
	    this.rotate90 = rotate90;
	    this.rotate270 = rotate270;
	    this.run = run;
	}

	public RenderAnimation(int all) {
		this(all, all, all, all, all, all, all);
	}
	
	public int getStand() {
	    return stand;
	}
	
	public int getStandTurn() {
	    return standTurn;
	}
	
	public int getWalk() {
	    return walk;
	}
	
	public int getRotate180() {
	    return rotate180;
	}
	
	public int getRotate90() {
	    return rotate90;
	}
	
	public int getRotate270() {
	    return rotate270;
	}
	
	public int getRun() {
	    return run;
	}

}
