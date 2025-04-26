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
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Kris | 10/05/2019 17:19
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MiscellaniaSteppingStone implements Shortcut {
    private static final Location DOCK = new Location(2573, 3859, 0);
    private static final Location LAND = new Location(2575, 3861, 0);

    @Override
    public int getLevel(final WorldObject object) {
        return 55;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {11768};
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 4;
    }

    @Override
    public boolean preconditions(final Player player, final WorldObject object) {
        if (!DiaryUtil.eligibleFor(DiaryReward.FREMENNIK_SEA_BOOTS2, player)) {
            player.getDialogueManager().start(new PlainChat(player, "You need to have completed the medium Fremennik diary to use this shortcut."));
            return false;
        }
        return true;
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return player.getX() >= 2575 && player.getY() >= 3855 ? new Location(2575, 3861, 0) : new Location(2573, 3859, 0);
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        final boolean direction = player.getY() < object.getY();
        player.setFaceLocation(object);
        WorldTasksManager.schedule(new WorldTask() {
            private int ticks;
            @Override
            public void run() {
                if (ticks == 0) {
                    player.setAnimation(Animation.JUMP);
                    player.setForceMovement(new ForceMovement(player.getLocation(), 15, object, 35, direction ? ForceMovement.NORTH : ForceMovement.WEST));
                } else if (ticks == 1) {
                    player.setLocation(object);
                } else if (ticks == 2) {
                    player.setAnimation(Animation.JUMP);
                    player.setForceMovement(new ForceMovement(player.getLocation(), 15, direction ? LAND : DOCK, 35, direction ? ForceMovement.EAST : ForceMovement.SOUTH));
                } else if (ticks == 3) {
                    player.setLocation(direction ? LAND : DOCK);
                    stop();
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
