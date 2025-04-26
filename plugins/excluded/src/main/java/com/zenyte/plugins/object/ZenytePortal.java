package com.zenyte.plugins.object;

import com.zenyte.game.GameInterface;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.teleportsystem.PortalTeleport;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 5-3-2019 | 19:02
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ZenytePortal implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Teleport")) {
            GameInterface.TELEPORT_MENU.open(player);
        } else if (option.equals("Teleport-previous")) {
            final PortalTeleport teleport = player.getTeleportManager().getLastTeleport();
            if (teleport == null) {
                player.sendMessage("You haven\'t teleported anywhere yet.");
                return;
            }
            player.getTeleportManager().teleportTo(teleport);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {35000};
    }
}
