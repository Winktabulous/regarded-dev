package com.zenyte.game.content.skills.agility.priffdinasrooftop;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author R-Y-M-R
 * @date 2/11/2022
 * @see <a href="https://www.rune-server.ee/members/necrotic/">RuneServer</a>
 */
public final class ForestFailLadder extends AgilityCourseObstacle {
    private static final int CLIMB = 828;
    private static final Location START_LOC = new Location(3267, 6144, 0);
    private static final Location END_LOC = new Location(3267, 6146, 2);

    public ForestFailLadder() {
        super(PriffdinasRooftopCourse.class, 10);
    }

    @Override
    public void startSuccess(Player player, WorldObject object) {
        player.useStairs(CLIMB, new Location(END_LOC), 1, 2);
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return START_LOC;
    }

    @Override
    public int getLevel(WorldObject object) {
        return 75;
    }

    @Override
    public int[] getObjectIds() {
        return new int[]{36232};
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 2;
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 0;
    }
}