package com.zenyte.plugins.object;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.object.*;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 9. juuni 2018 : 07:29:29
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class LumbridgeShedDoor implements ObjectAction {

    private static final Location OUTSIDE = new Location(3201, 3169, 0);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (object.getId() == ObjectId.DOOR_2406) {
            player.lock();
            final boolean isOutside = player.getLocation().getPositionHash() != OUTSIDE.getPositionHash();
            player.addWalkSteps(object.getX(), object.getY());
            WorldTasksManager.schedule(new WorldTask() {

                private int ticks;

                private WorldObject door;

                @Override
                public void run() {
                    switch(ticks++) {
                        case 0:
                            door = Door.handleGraphicalDoor(object, null);
                            return;
                        case 1:
                            if (isOutside) {
                                player.addWalkSteps(door.getX(), door.getY(), 1, false);
                            } else {
                                player.addWalkSteps(object.getX(), object.getY(), 1, false);
                            }
                            if (canUse(player) && !isOutside) {
                                FairyRing.handle(player, object, FairyRing.ZANARIS_ENTRANCE);
                            }
                            return;
                        case 3:
                            if (!(canUse(player) && !isOutside)) {
                                player.unlock();
                            }
                            Door.handleGraphicalDoor(door, object);
                            stop();
                            return;
                    }
                }
            }, 0, 0);
            return;
        } else if (object.getId() == ObjectId.FAIRY_RING_12094) {
            if (canUse(player)) {
                FairyRing.handle(player, object, FairyRing.LUMBRIDGE_SHED);
            } else {
                player.sendMessage("The fairy ring doesn\'t seem to respond.");
            }
        }
    }

    private boolean canUse(@NotNull final Player player) {
        return player.getEquipment().getId(EquipmentSlot.WEAPON) == 772 || DiaryUtil.eligibleFor(DiaryReward.EXPLORERS_RING4, player);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.DOOR_2406, ObjectId.FAIRY_RING_12094 };
    }
}
