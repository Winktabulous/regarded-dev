package com.zenyte.game.content.skills.agility.wildernesscourse;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 25 feb. 2018 : 14:47:02
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 * profile</a>}
 */
public final class ObstaclePipe extends AgilityCourseObstacle {
    private static final Animation CLIMB_ANIM = new Animation(749);
    private static final Location START = new Location(3004, 3937, 0);

    public ObstaclePipe() {
        super(WildernessCourse.class, 1);
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        player.resetWalkSteps();
        final Location obj = new Location(player.getLocation());
        player.addWalkSteps(obj.getX(), obj.getY(), -1, false);
        player.lock();
        WorldTasksManager.schedule(new WorldTask() {
            private int ticks;
            @Override
            public void run() {
                switch (ticks++) {
                case 0: 
                    player.setAnimation(CLIMB_ANIM);
                    break;
                case 1: 
                    player.setForceMovement(new ForceMovement(new Location(obj.getX(), obj.getY() + 5, 0), 90, ForceMovement.NORTH));
                    break;
                case 4: 
                    player.setLocation(new Location(obj.getX(), obj.getY() + 5, 0));
                    break;
                case 5: 
                    player.setForceMovement(new ForceMovement(new Location(obj.getX(), obj.getY() + 12, 0), 150, ForceMovement.NORTH));
                    break;
                case 8: 
                    player.setAnimation(CLIMB_ANIM);
                    break;
                case 10: 
                    player.setLocation(new Location(obj.getX(), obj.getY() + 13, 0));
                    break;
                case 11: 
                    player.unlock();
                    stop();
                    break;
                }
            }
        }, 0, 0);
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return START;
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 52;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {23137};
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 12.5;
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 11;
    }
}
