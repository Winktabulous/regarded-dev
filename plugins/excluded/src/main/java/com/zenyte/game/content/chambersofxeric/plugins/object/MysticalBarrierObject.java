package com.zenyte.game.content.chambersofxeric.plugins.object;

import com.zenyte.game.content.chambersofxeric.dialogue.MysticalBarrierD;
import com.zenyte.game.content.chambersofxeric.greatolm.OlmRoom;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Kris | 06/07/2019 04:32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MysticalBarrierObject implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.getRaid().ifPresent(raid -> raid.ifInRoom(player, OlmRoom.class, room -> {
            if (room.getRaid().isCompleted()) {
                MysticalBarrierD.passBarrier(player, room, object);
                return;
            }
            if (room.inChamber(player.getLocation())) {
                player.getDialogueManager().start(new PlainChat(player, "A strange magic prevents you from passing through.<br><br>" + "There's no way back now."));
                return;
            }
            if (option.equalsIgnoreCase("Pass")) {
                player.getDialogueManager().start(new MysticalBarrierD(player, room, object));
            } else {
                MysticalBarrierD.passBarrier(player, room, object);
            }
        }));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.MYSTICAL_BARRIER };
    }
}
