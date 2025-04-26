package com.zenyte.game.content.minigame.warriorsguild.plugins;

import com.zenyte.game.content.minigame.warriorsguild.dummyroom.DummyRoom;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.GlobalAreaManager;

/**
 * @author Kris | 23/03/2019 17:01
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DummyObject implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equalsIgnoreCase("Hit") || option.equalsIgnoreCase("View")) {
            final DummyRoom dummyRoom = GlobalAreaManager.getArea(DummyRoom.class);
            dummyRoom.handleObject(player, object);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.DUMMY_23958, ObjectId.DUMMY_23959, ObjectId.DUMMY_23960, ObjectId.DUMMY_23961, ObjectId.DUMMY_23962, ObjectId.DUMMY_23963, ObjectId.DUMMY_23964, ObjectId.INFORMATION_SCROLL_24908 };
    }
}
