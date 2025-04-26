package com.zenyte.game.content.skills.magic.spells.teleports.structures;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 19/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class InstantStructure implements TeleportStructure {
    @Override
    public SoundEffect getStartSound() {
        return null;
    }

    @Override
    public void teleport(final Player player, final Teleport teleport) {
        player.stopAll();
        start(player, teleport);
    }

    @Override
    public void start(final Player player, final Teleport teleport) {
        player.blockIncomingHits(1);
        player.lock();
        teleport.onUsage(player);
        end(player, teleport);
    }

    @Override
    public void end(final Player player, final Teleport teleport) {
        final Location location = getRandomizedLocation(player, teleport);
        player.getInterfaceHandler().closeInterfaces();
        teleport.onArrival(player);
        player.setLocation(location);
        stop(player, teleport);
    }

    @Override
    public void stop(final Player player, final Teleport teleport) {
        player.resetFreeze();
        //player.setProtectionDelay(Utils.currentTimeMillis() + 100);
        player.lock(1);
    }
}
