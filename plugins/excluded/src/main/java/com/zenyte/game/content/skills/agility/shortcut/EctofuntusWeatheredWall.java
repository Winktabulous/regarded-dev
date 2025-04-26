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
 * @author Kris | 24/06/2019 12:04
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EctofuntusWeatheredWall implements Shortcut {

    private static final Animation FIRST_ANIM = new Animation(2586, 15);

    private static final Animation SECOND_ANIM = new Animation(2588);

    private static final Animation UP_ANIM = new Animation(737);

    @Override
    public int getLevel(final WorldObject object) {
        return 57;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 16525, 16526 };
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 2;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        if (object.getId() == ObjectId.WEATHERED_WALL_16526) {
            player.setAnimation(FIRST_ANIM);
            final Location dest = new Location(3671, 9888, 2);
            player.setFaceLocation(dest);
            WorldTasksManager.schedule(new WorldTask() {

                int ticks;

                @Override
                public void run() {
                    switch(ticks++) {
                        case 0:
                            player.setLocation(dest);
                            player.setAnimation(SECOND_ANIM);
                            break;
                        case 1:
                            player.setAnimation(Animation.STOP);
                            break;
                    }
                }
            }, 0, 0);
        } else {
            player.setAnimation(UP_ANIM);
            WorldTasksManager.schedule(() -> {
                player.setLocation(new Location(3670, 9888, 3));
                player.setAnimation(Animation.STOP);
            });
        }
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 2;
    }
}
