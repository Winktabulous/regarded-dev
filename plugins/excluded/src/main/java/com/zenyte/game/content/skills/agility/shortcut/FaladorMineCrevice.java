package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.achievementdiary.diaries.FaladorDiary;
import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 10/05/2019 17:55
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FaladorMineCrevice implements Shortcut {
    private static final Animation CRAWL_IN = new Animation(2594);
    private static final Animation CRAWL_OUT = new Animation(2595);
    private static final Animation INVISIBLE = new Animation(2590);

    private static final Location WEST_CREVICE = new Location(3029, 9806, 0);
    private static final Location EAST_CREVICE = new Location(3034, 9806, 0);

    private static final Location WEST_EXIT = new Location(3028, 9806, 0);
    private static final Location EAST_EXIT = new Location(3035, 9806, 0);

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
                    player.setForceMovement(new ForceMovement(west ? EAST_CREVICE : WEST_CREVICE, 150, west ? ForceMovement.EAST : ForceMovement.WEST));
                } else if(ticks == 7) {
                    player.setAnimation(CRAWL_OUT);
                    player.setForceMovement(new ForceMovement(west ? EAST_EXIT : WEST_EXIT, 60, west ? ForceMovement.EAST : ForceMovement.WEST));
                } else if(ticks == 8) {
                    player.getAchievementDiaries().update(FaladorDiary.SQUEEZE_THROUGH_CREVICE);
                    player.setLocation(west ? EAST_EXIT : WEST_EXIT);
                    stop();
                }
                ticks++;
            }

        }, 0, 0);
    }

    @Override
    public String getEndMessage(final boolean success) {
        return success ? "You climb your way through the narrow crevice." : null;
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 42;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 16543 };
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 9;
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
