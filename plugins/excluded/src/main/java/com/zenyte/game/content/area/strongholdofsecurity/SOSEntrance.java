package com.zenyte.game.content.area.strongholdofsecurity;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Kris | 4. sept 2018 : 21:34:30
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class SOSEntrance implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Climb-down")) {
            player.setLocation(new Location(1859, 5243, 0));
            player.getDialogueManager().start(new PlainChat(player, "You squeeze through the hole and find a ladder a few feet down leading into the Stronghold of Security."));
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.ENTRANCE_20790 };
    }

    @Override
    public int getDelay() {
        return 1;
    }
}
