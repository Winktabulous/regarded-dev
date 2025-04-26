package com.zenyte.plugins.renewednpc;

import com.zenyte.game.GameInterface;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.teleportsystem.PortalTeleport;

/**
 * @author Kris | 25/11/2018 16:11
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ZenyteTeleporter extends NPCPlugin {
    @Override
    public void handle() {
        bind("Teleport", (player, npc) -> GameInterface.TELEPORT_MENU.open(player));
        bind("Teleport-previous", (player, npc) -> {
            final PortalTeleport teleport = player.getTeleportManager().getLastTeleport();
            if (teleport == null) {
                player.sendMessage("The wizard hasn't teleported you anywhere yet.");
                return;
            }
            player.getTeleportManager().teleportTo(teleport);
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] {10000};
    }
}
