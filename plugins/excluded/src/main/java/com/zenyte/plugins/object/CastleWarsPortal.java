package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.teleportsystem.PortalTeleport;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Christopher
 * @since 4/11/2020
 */
public class CastleWarsPortal implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        PortalTeleport.GAMBLING.teleport(player);
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{ObjectId.CASTLE_WARS_PORTAL};
    }
}
