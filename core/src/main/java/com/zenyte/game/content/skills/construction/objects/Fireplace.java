package com.zenyte.game.content.skills.construction.objects;

import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 24. veebr 2018 : 3:17.05
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class Fireplace implements ObjectInteraction {

    private static final Animation ANIMATION = new Animation(3658);

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CLAY_FIREPLACE, 6782, 6784 };
    }

    @Override
    public void handleObjectAction(final Player player, final Construction construction, final RoomReference reference, final WorldObject object, final int optionId, final String option) {
        if (construction.isBuildingMode()) {
            player.sendMessage("You can't do this in building mode!");
            return;
        }
        if (!player.getInventory().containsItem(590, 1)) {
            player.sendMessage("You need a tinderbox to light the fireplace.");
            return;
        }
        player.lock();
        object.setLocked(true);
        player.setAnimation(ANIMATION);
        WorldTasksManager.schedule(new WorldTask() {

            private boolean first;

            private final WorldObject obj = new WorldObject(object);

            @Override
            public void run() {
                if (!first) {
                    obj.setId(obj.getId() + 1);
                    World.spawnObject(obj);
                    player.unlock();
                    first = true;
                } else {
                    if (World.containsSpawnedObject(obj)) {
                        object.setLocked(false);
                        World.spawnObject(object);
                    }
                    stop();
                }
            }
        }, 2, 250);
    }
}
