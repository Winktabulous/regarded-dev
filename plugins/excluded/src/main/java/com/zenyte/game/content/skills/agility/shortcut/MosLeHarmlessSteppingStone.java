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
 * @author Kris | 10/05/2019 17:49
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MosLeHarmlessSteppingStone implements Shortcut {
    private static final Location WEST = new Location(3708, 2969, 0);
    private static final Location EAST = new Location(3715, 2969, 0);

    @Override
    public boolean preconditions(final Player player, final WorldObject object) {
        if (!DiaryUtil.eligibleFor(DiaryReward.MORYTANIA_LEGS3, player)) {
            player.sendMessage("You need to have completed the hard Morytania diaries to use this shortcut.");
            return false;
        }
        return true;
    }

    @Override
    public void startSuccess(Player player, WorldObject object) {
        final boolean west = player.getX() < object.getX();
        final Location dest = west ? EAST : WEST;
        WorldTasksManager.schedule(new WorldTask() {
            private int ticks;
            @Override
            public void run() {
                if (ticks == 0) {
                    player.setAnimation(Animation.JUMP);
                    player.setForceMovement(new ForceMovement(player.getLocation(), 15, object, 35, west ? ForceMovement.EAST : ForceMovement.WEST));
                } else if (ticks == 1) player.setLocation(object);
                 else if (ticks == 2) {
                    player.setAnimation(Animation.JUMP);
                    player.setForceMovement(new ForceMovement(player.getLocation(), 15, dest, 35, west ? ForceMovement.EAST : ForceMovement.WEST));
                } else if (ticks == 3) {
                    player.setLocation(dest);
                    stop();
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public int getLevel(WorldObject object) {
        return 40;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {19042};
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 3;
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 0;
    }

    @Override
    public Location getRouteEvent(Player player, WorldObject object) {
        return player.getX() < object.getX() ? WEST : EAST;
    }
}
