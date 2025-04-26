package com.zenyte.game.content.minigame.pestcontrol.plugins;

import com.zenyte.game.content.minigame.pestcontrol.PestControlGameType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 26. juuni 2018 : 18:12:47
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class GangPlankPlugin implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        switch(object.getId()) {
            case 14315:
                player.setLocation(PestControlGameType.NOVICE.getEnterPoint());
                break;
            case 25631:
                player.setLocation(PestControlGameType.INTERMEDIATE.getEnterPoint());
                break;
            case 25632:
                player.setLocation(PestControlGameType.VETERAN.getEnterPoint());
                break;
        }
        player.sendMessage("You board the lander.");
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.GANGPLANK_14315, ObjectId.GANGPLANK_25631, ObjectId.GANGPLANK_25632 };
    }
}
