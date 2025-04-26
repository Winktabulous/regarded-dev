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
 * @author Kris | 24. veebr 2018 : 22:37.33
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 * TODO: Destroy house if it's the owner.
 */
public final class AmuletOfGlory implements ObjectInteraction {

    private static final Animation START = new Animation(714);

    private static final Graphics GFX = new Graphics(308, 48, 100);

    private static final Location[] LOCATIONS = new Location[] { new Location(3087, 3496, 0), new Location(2918, 3176, 0), new Location(3105, 3251, 0), new Location(3293, 3162, 0) };

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.AMULET_OF_GLORY };
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
                player.setLocation(LOCATIONS[optionId - 1]);
                player.setAnimation(Animation.STOP);
            }
        }, 2);
    }
}
