package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 18/02/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MausoleumGap implements Shortcut {

    private static final Animation JUMP_ANIM = new Animation(2586);

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        if (object.getId() == ObjectId.BRIDGE_5159) {
            return new Location(object.getX(), 3558, 0);
        }
        return object;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        final Location forward = player.getLocation().transform(0, 1, 0);
        player.addWalkSteps(forward.getX(), forward.getY(), 1, true);
        WorldTasksManager.schedule(new WorldTask() {

            int ticks;

            @Override
            public void run() {
                switch(ticks++) {
                    case 0:
                        player.setAnimation(JUMP_ANIM);
                        break;
                    case 1:
                        player.setLocation(player.getLocation().transform(0, 3, 0));
                        break;
                    case 3:
                        player.setAnimation(Animation.STOP);
                        stop();
                        break;
                }
            }
        }, 0, 0);
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 0;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 5159, 5160 };
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 4;
    }
}
