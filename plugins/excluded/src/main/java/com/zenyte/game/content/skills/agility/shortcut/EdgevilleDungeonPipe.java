package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.achievementdiary.diaries.VarrockDiary;
import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.AttachedObject;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 28/11/2018 13:26
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EdgevilleDungeonPipe implements Shortcut {
    private static final int WEST_HASH = new Location(3150, 9906, 0).getPositionHash();
    private static final Animation CRAWL = new Animation(749);
    private static final Location MIDDLE = new Location(3152, 9906, 0);
    private static final Location WEST = new Location(3149, 9906, 0);
    private static final Location EAST = new Location(3155, 9906, 0);
    private static final AttachedObject PIPE_START_OBJ = new AttachedObject(new WorldObject(16511, 10, 0, 3150, 9906, 0), 0, 96, -1, 2, 0, 0);
    private static final AttachedObject PIPE_END_OBJ = new AttachedObject(new WorldObject(16511, 10, 2, 3153, 9906, 0), 0, 96, -1, 2, 0, 0);

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        player.faceObject(object);
        final boolean west = object.getPositionHash() == WEST_HASH;
        WorldTasksManager.schedule(new WorldTask() {
            private int ticks;
            @Override
            public void run() {
                if (ticks == 0) player.setAnimation(CRAWL);
                 else if (ticks == 1) {
                    World.sendAttachedObject(player, west ? PIPE_START_OBJ : PIPE_END_OBJ);
                    player.setForceMovement(new ForceMovement(MIDDLE, 90, west ? ForceMovement.EAST : ForceMovement.WEST));
                } else if (ticks == 3) player.setLocation(MIDDLE);
                 else if (ticks == 5) {
                    player.setAnimation(CRAWL);
                    World.sendAttachedObject(player, west ? PIPE_END_OBJ : PIPE_START_OBJ);
                    player.setForceMovement(new ForceMovement(west ? EAST : WEST, 90, west ? ForceMovement.EAST : ForceMovement.WEST));
                } else if (ticks == 7) {
                    player.getAchievementDiaries().update(VarrockDiary.SQUEEZE_THROUGH_OBSTACLE_PIPE);
                    player.setLocation(west ? EAST : WEST);
                    stop();
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return object.getPositionHash() == WEST_HASH ? WEST : EAST;
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 51;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {16511};
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 5;
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
