package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 12/05/2019 14:24
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HerosGuildCrevice implements Shortcut {
    private static final Animation CRAWL_IN = new Animation(2594);
    private static final Animation CRAWL_OUT = new Animation(2595);
    private static final Animation INVISIBLE = new Animation(2590);

    private static final Location WEST_CREVICE = new Location(2899, 9901, 0);
    private static final Location EAST_CREVICE = new Location(2914, 9895, 0);

    private static final Location WEST_EXIT = new Location(2898, 9902, 0);
    private static final Location EAST_EXIT = new Location(2915, 9894, 0);

    @Override
    public boolean preconditions(final Player player, final WorldObject object) {
        if (!DiaryUtil.eligibleFor(DiaryReward.FALADOR_SHIELD3, player)) {
            player.sendMessage("You need to have completed the hard Falador diaries to use this shortcut.");
            return false;
        }
        return true;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        final boolean west = object.getPositionHash() == WEST_CREVICE.getPositionHash(); // west going east
        WorldTasksManager.schedule(new WorldTask() {

            private int ticks;

            @Override
            public void run() {
                if(ticks == 0) {
                    player.setAnimation(CRAWL_IN);
                    player.setForceMovement(new ForceMovement(west ? WEST_CREVICE : EAST_EXIT, 150, west ? ForceMovement.EAST : ForceMovement.WEST));
                } else if(ticks == 1)
                    player.setLocation(west ? WEST_CREVICE : EAST_CREVICE);
                else if(ticks == 2) {
                    player.setAnimation(INVISIBLE);
                    player.setForceMovement(new ForceMovement(west ? EAST_CREVICE : WEST_CREVICE, 150 + (6 * 30), west ? ForceMovement.EAST : ForceMovement.WEST));
                } else if(ticks == 13) {
                    player.setAnimation(CRAWL_OUT);
                    player.setForceMovement(new ForceMovement(west ? EAST_EXIT : WEST_EXIT, 60, west ? ForceMovement.EAST : ForceMovement.WEST));
                } else if(ticks == 14) {
                    player.setLocation(west ? EAST_EXIT : WEST_EXIT);
                    stop();
                }
                ticks++;
            }

        }, 0, 0);
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return object.getId() == 9740 ? new Location(2915, 9894, 0) : new Location(2898, 9902, 0);
    }

    @Override
    public String getEndMessage(final boolean success) {
        return success ? "You climb your way through the narrow crevice." : null;
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 67;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 9739, 9740 };
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 15;
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
