package com.zenyte.game.content.skills.agility.seersrooftop;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.content.skills.agility.MarkOfGrace;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

public final class SecondJumpGap extends AgilityCourseObstacle {

    private static final Animation GRAB = new Animation(2585);

    public SecondJumpGap() {
        super(SeersRooftopCourse.class, 4);
    }

    @Override
    public void startSuccess(Player player, WorldObject object) {
        final Location edge = new Location(player.getX(), player.getY()-3, 3);
        final Location finish = new Location(player.getX(), player.getY()-4, 3);
        player.setFaceLocation(edge);
        WorldTasksManager.schedule(new WorldTask() {
            private int ticks;

            @Override
            public void run() {
                if(ticks == 0)
                    player.setAnimation(Animation.LEAP);
                else if(ticks == 1) {
                    player.setLocation(edge);
                    player.setAnimation(GRAB);
                } else if(ticks == 3) {
                    player.setLocation(finish);
                    MarkOfGrace.spawn(player, SeersRooftopCourse.MARK_LOCATIONS, 60, 20);
                    stop();
                }

                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public int getLevel(WorldObject object) {
        return 60;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {ObjectId.GAP_14929 };
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 3;
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 20;
    }
}
