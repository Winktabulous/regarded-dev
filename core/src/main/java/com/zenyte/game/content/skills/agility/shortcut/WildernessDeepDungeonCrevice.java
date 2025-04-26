package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 10/05/2019 21:53
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class WildernessDeepDungeonCrevice implements Shortcut {
    private static final Animation CRAWL_IN = new Animation(2594);
    private static final Animation CRAWL_OUT = new Animation(2595);

    private static final Animation INVISIBLE = new Animation(2590);

    private static final Location NORTH_CREVICE = new Location(3046, 10327, 0);
    private static final Location SOUTH_CREVICE = new Location(3048, 10335, 0);

    private static final Location NORTH_EXIT = new Location(3046, 10326, 0);
    private static final Location SOUTH_EXIT = new Location(3048, 10336, 0);

    @Override
    public boolean preconditions(final Player player, final WorldObject object) {
        if (!DiaryUtil.eligibleFor(DiaryReward.WILDERNESS_SWORD2, player)) {
            player.sendMessage("You need to have completed the medium Wilderness diaries to use this shortcut.");
            return false;
        }
        return true;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        final boolean NORTH = object.getPositionHash() == NORTH_CREVICE.getPositionHash(); // NORTH going SOUTH
        WorldTasksManager.schedule(new WorldTask() {

            private int ticks;

            @Override
            public void run() {
                if(ticks == 0) {
                    player.setAnimation(CRAWL_IN);
                    player.setForceMovement(new ForceMovement(NORTH ? NORTH_CREVICE : SOUTH_EXIT, 150, NORTH ? ForceMovement.SOUTH : ForceMovement.NORTH));
                } else if(ticks == 1)
                    player.setLocation(NORTH ? NORTH_CREVICE : SOUTH_CREVICE);
                else if(ticks == 2) {
                    player.setAnimation(INVISIBLE);
                    player.setForceMovement(new ForceMovement(NORTH ? SOUTH_CREVICE : NORTH_CREVICE, 150, NORTH ? ForceMovement.SOUTH : ForceMovement.NORTH));
                } else if(ticks == 7) {
                    player.setAnimation(CRAWL_OUT);
                    player.setForceMovement(new ForceMovement(NORTH ? SOUTH_EXIT : NORTH_EXIT, 60, NORTH ? ForceMovement.SOUTH : ForceMovement.NORTH));
                } else if(ticks == 8) {
                    final long currentTick = WorldThread.getCurrentCycle();
                    player.setAttackedByDelay(currentTick);
                    player.setLocation(NORTH ? SOUTH_EXIT : NORTH_EXIT);
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
        return 46;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 19043 };
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
