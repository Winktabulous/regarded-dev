package com.zenyte.game.content.skills.agility.gnomecourse;

import com.zenyte.game.content.achievementdiary.diaries.WesternProvincesDiary;
import com.zenyte.game.content.skills.agility.AgilityCourse;
import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.AttachedObject;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 21. dets 2017 : 18:33.42
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ObstaclePipe extends AgilityCourseObstacle {

    private static final Animation CLIMB_ANIM = new Animation(749);

    private static final AttachedObject PIPE_START_OBJ_EAST = new AttachedObject(new WorldObject(23138, 10, 1, 2484, 3431, 0), 30, 126, 0, 0, -1, 2);

    private static final AttachedObject PIPE_START_OBJ_WEST = new AttachedObject(new WorldObject(23138, 10, 1, 2487, 3431, 0), 30, 126, 0, 0, -1, 2);

    private static final AttachedObject PIPE_END_OBJ_EAST = new AttachedObject(new WorldObject(23138, 10, 3, 2484, 3435, 0), 30, 126, 0, 0, -1, 2);

    private static final AttachedObject PIPE_END_OBJ_WEST = new AttachedObject(new WorldObject(23138, 10, 3, 2487, 3435, 0), 30, 126, 0, 0, -1, 2);

    public ObstaclePipe() {
        super(GnomeCourse.class, 7);
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        player.resetWalkSteps();
        final Location obj = new Location(player.getLocation());
        player.addWalkSteps(obj.getX(), obj.getY(), -1, false);
        final boolean east = object.getX() == 2484;
        WorldTasksManager.schedule(new WorldTask() {

            private int ticks;

            @Override
            public void run() {
                switch(ticks++) {
                    case 0:
                        World.sendAttachedObject(player, east ? PIPE_START_OBJ_EAST : PIPE_START_OBJ_WEST);
                        player.setAnimation(CLIMB_ANIM);
                        break;
                    case 1:
                        player.setForceMovement(new ForceMovement(new Location(obj.getX(), obj.getY() + 3, 0), 90, ForceMovement.NORTH));
                        break;
                    case 4:
                        player.setLocation(new Location(obj.getX(), obj.getY() + 3, 0));
                        break;
                    case 5:
                        player.setForceMovement(new ForceMovement(new Location(obj.getX(), obj.getY() + 6, 0), 150, ForceMovement.NORTH));
                        break;
                    case 6:
                        World.sendAttachedObject(player, east ? PIPE_END_OBJ_EAST : PIPE_END_OBJ_WEST);
                        break;
                    case 8:
                        player.setAnimation(CLIMB_ANIM);
                        break;
                    case 10:
                        player.setLocation(new Location(obj.getX(), obj.getY() + 7, 0));
                        player.getAchievementDiaries().update(WesternProvincesDiary.COMPLETE_GNOME_COURSE_LAP);
                        stop();
                        break;
                }
            }
        }, 0, 0);
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        if (object.getId() == ObjectId.OBSTACLE_PIPE_23138)
            return new Location(2484, 3430, 0);
        return new Location(2487, 3430, 0);
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 1;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 23138, 23139 };
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 7.5;
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 11;
    }

    @Override
    public Class<? extends AgilityCourse> getCourse() {
        return GnomeCourse.class;
    }

}
