package com.zenyte.game.world.entity.pathfinding;

/**
 * @author Kris | 26/02/2019 22:04
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class RouteResult {

    private final int steps;
    private final int[] xBuffer;
    private final int[] yBuffer;
    private final boolean alternative;

    public static final RouteResult ILLEGAL = new RouteResult(-1, null, null, true);

    public RouteResult(int steps, int[] xBuffer, int[] yBuffer, boolean alternative) {
        this.steps = steps;
        this.xBuffer = xBuffer;
        this.yBuffer = yBuffer;
        this.alternative = alternative;
    }

    public int getSteps() {
        return steps;
    }

    public int[] getXBuffer() {
        return xBuffer;
    }

    public int[] getYBuffer() {
        return yBuffer;
    }

    public boolean isAlternative() {
        return alternative;
    }

}
