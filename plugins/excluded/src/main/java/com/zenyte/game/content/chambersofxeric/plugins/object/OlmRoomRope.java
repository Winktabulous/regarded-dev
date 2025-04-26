package com.zenyte.game.content.chambersofxeric.plugins.object;

import com.zenyte.game.content.chambersofxeric.greatolm.OlmRoom;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

import static com.zenyte.plugins.object.LadderOA.CLIMB_UP;

/**
 * @author Kris | 06/07/2019 04:31
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class OlmRoomRope implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.getRaid().ifPresent(raid -> raid.ifInRoom(player, OlmRoom.class, room -> {
            player.lock(2);
            player.setAnimation(CLIMB_UP);
            player.getPacketDispatcher().sendClientScript(1512);
            WorldTasksManager.schedule(new WorldTask() {

                @Override
                public void run() {
                    if (raid.isDestroyed()) {
                        return;
                    }
                    player.getPacketDispatcher().sendClientScript(1513);
                    player.setLocation(room.getBoundTile());
                }
            });
        }));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.ROPE_29996 };
    }
}
