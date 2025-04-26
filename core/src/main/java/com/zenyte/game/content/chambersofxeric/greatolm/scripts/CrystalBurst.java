package com.zenyte.game.content.chambersofxeric.greatolm.scripts;

import com.zenyte.game.content.chambersofxeric.greatolm.*;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 16. jaan 2018 : 1:10.37
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class CrystalBurst implements OlmCombatScript {
    @Override
    public void handle(final GreatOlm olm) {
        final OlmRoom room = olm.getRoom();
        final LeftClaw leftClaw = room.getLeftClaw();
        if (leftClaw != null) {
            leftClaw.displaySign(LeftClaw.CRYSTAL_SIGN);
        }
        if (room.getRaid().isDestroyed()) {
            return;
        }
        for (final Player player : olm.everyone(GreatOlm.ENTIRE_CHAMBER)) {
            final Location tile = new Location(player.getLocation());
            if (room.containsCrystalCluster(tile)) {
                continue;
            }
            final CrystalCluster cluster = new CrystalCluster(room, tile);
            cluster.process();
        }
    }
}
