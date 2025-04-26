package com.zenyte.game.content.skills.agility.faladorrooftop;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.content.skills.agility.MarkOfGrace;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Noele | Apr 30, 2018 : 8:37:42 AM
 * @see <a href="https://noeles.life">|| noele@zenyte.com</a>
 */
public final class JumpLedge extends AgilityCourseObstacle {
    private static final Animation JUMP = new Animation(1603);

    public JumpLedge() {
        super(FaladorRooftopCourse.class, 5);
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        final boolean axis = object.getId() == ObjectId.LEDGE_14920 || object.getId() == ObjectId.LEDGE_14924;
        final int direction = axis ? (object.getId() == ObjectId.LEDGE_14920 ? ForceMovement.WEST : ForceMovement.EAST) : ForceMovement.SOUTH;
        final int offset = axis ? (object.getId() == ObjectId.LEDGE_14920 ? player.getX() - 2 : player.getX() + 2) : (object.getName().equals("Gap") ? player.getY() - 4 : player.getY() - 2);
        final Location finish = axis ? new Location(offset, player.getY(), 3) : new Location(player.getX(), offset, 3);
        player.setFaceLocation(finish);
        WorldTasksManager.schedule(new WorldTask() {
            private int ticks;
            @Override
            public void run() {
                if (ticks == 0) {
                    player.setAnimation(JUMP);
                    player.setForceMovement(new ForceMovement(finish, 45, direction));
                } else if (ticks == 2) {
                    player.setLocation(finish);
                    MarkOfGrace.spawn(player, FaladorRooftopCourse.MARK_LOCATIONS, 50, 50);
                    stop();
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 50;
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 3;
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return object.getId() == ObjectId.GAP_14919 ? 25 : 10;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {ObjectId.GAP_14919, ObjectId.LEDGE_14920, ObjectId.LEDGE_14921, ObjectId.LEDGE_14922, ObjectId.LEDGE_14923, ObjectId.LEDGE_14924};
    }
}
