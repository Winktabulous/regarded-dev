package com.zenyte.game.content.skills.construction.objects.questhall;

import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 24. veebr 2018 : 23:03.22
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 * TODO: Restrictions.
 */
public final class MythicalCape implements ObjectInteraction {

    private static final Animation START = new Animation(714);

    private static final Graphics GFX = new Graphics(308, 48, 100);

    private static final Location TILE = new Location(2457, 2856, 0);

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.MYTHICAL_CAPE };
    }

    @Override
    public void handleObjectAction(Player player, Construction construction, RoomReference reference, WorldObject object, int optionId, String option) {
        player.lock();
        player.setAnimation(START);
        player.setGraphics(GFX);
        WorldTasksManager.schedule(new WorldTask() {

            @Override
            public void run() {
                player.unlock();
                player.setLocation(TILE);
                player.setAnimation(Animation.STOP);
            }
        }, 2);
    }
}
