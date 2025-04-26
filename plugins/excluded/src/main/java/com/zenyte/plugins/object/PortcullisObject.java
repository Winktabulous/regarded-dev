package com.zenyte.plugins.object;

import com.zenyte.game.content.boss.cerberus.CerberusRoom;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 3 mei 2018 | 19:02:29
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class PortcullisObject implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final CerberusRoom position = object.getX() == 1239 ? CerberusRoom.WEST : object.getX() == 1367 ? CerberusRoom.EAST : CerberusRoom.NORTH;
        new FadeScreen(player, () -> player.setLocation(position.getEntrance())).fade(2);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {"Portcullis"};
    }
}
