package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 26/04/2019 17:52
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TrollStrongholdRockClimb implements Shortcut {

    private static final Location BOTTOM_OBSTACLE = new Location(2843, 3693, 0);

    private static final Animation CRAWL = new Animation(1148);
    private static final RenderAnimation RENDER = new RenderAnimation(738, 737, 737, 737, 737, 737, -1);

    private static final Location CRAWL_FACE = new Location(2837, 3693, 0);
    private static final Location TOP_ROCK = new Location(2839, 3693, 0);

    @Override
    public boolean preconditions(final Player player, final WorldObject object) {
        if (object.matches(BOTTOM_OBSTACLE)) {
            if (!DiaryUtil.eligibleFor(DiaryReward.FREMENNIK_SEA_BOOTS3, player)) {
                player.sendMessage("You must have completed the hard fremennik diary to use this obstacle.");
                return false;
            }
        }
        return true;
    }

    private static final Location TOP = new Location(2838, 3693, 0);
    private static final Location BOTTOM = new Location(2844, 3693, 0);

    private static final ForceMovement CRAWL_DOWN = new ForceMovement(BOTTOM, 120, ForceMovement.WEST);

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return object.matches(BOTTOM_OBSTACLE) ? new Location(2844, 3693, 0) : object;
    }

    @Override
    public void startSuccess(Player player, WorldObject object) {
        player.setFaceLocation(CRAWL_FACE);
        WorldTasksManager.schedule(new WorldTask() {

            private int ticks;

            @Override
            public void run() {
                if(object.matches(BOTTOM_OBSTACLE)) {
                    if(ticks == 0) {
                        player.getAppearance().setRenderAnimation(RENDER);
                        player.addWalkSteps(object.getX(), object.getY(), -1, false);
                    } else if(ticks == 1)
                        player.addWalkSteps(TOP_ROCK.getX(), TOP_ROCK.getY(), -1, false);
                    else if(ticks == 2)
                        player.addWalkSteps(TOP.getX(), TOP.getY(), -1, false);
                    else if(ticks == 4) {
                        player.getAppearance().resetRenderAnimation();
                        stop();
                    }
                } else {
                    if(ticks == 1) {
                        player.setAnimation(CRAWL);
                        player.setForceMovement(CRAWL_DOWN);
                    } else if(ticks == 5) {
                        player.setLocation(BOTTOM);
                        stop();
                    }
                }
                ticks++;
            }

        }, 0, 0);
    }

    @Override
    public int getLevel(WorldObject object) {
        return 73;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 16464 }; // top, bottom
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return object.matches(BOTTOM_OBSTACLE) ? 5 : 7;
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 0;
    }
}
