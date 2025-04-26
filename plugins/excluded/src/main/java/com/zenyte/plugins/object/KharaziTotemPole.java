package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Kris | 27/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class KharaziTotemPole implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.getDialogueManager().start(new PlainChat(player, "This totem pole is truly awe inspiring. It depicts powerful Karamja " +
                "jungle animals. It is very well carved and brings a sense of power and spiritual fulfillment to anyone who looks at it."));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {
                ObjectId.TOTEM_POLE_2936
        };
    }
}
