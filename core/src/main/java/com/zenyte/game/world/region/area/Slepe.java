package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.teleportsystem.PortalTeleport;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 15/04/2019 17:38
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Slepe extends Morytania {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 3685, 3392 },
                        { 3685, 3264 },
                        { 3776, 3264 },
                        { 3776, 3392 }
                })
        };
    }

    @Override
    public void enter(final Player player) {
        super.enter(player);
        player.getTeleportManager().unlock(PortalTeleport.SWAMP_CRABS);
    }

    @Override
    public void leave(final Player player, final boolean logout) {
        super.leave(player, logout);
    }

    @Override
    public String name() {
        return "Slepe";
    }
}
