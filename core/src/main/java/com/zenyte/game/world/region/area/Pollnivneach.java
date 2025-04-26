package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.teleportsystem.PortalTeleport;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 15/04/2019 17:21
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Pollnivneach extends SouthernDesertArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 3370, 3007 },
                        { 3376, 2996 },
                        { 3380, 2991 },
                        { 3382, 2986 },
                        { 3384, 2977 },
                        { 3380, 2961 },
                        { 3375, 2960 },
                        { 3359, 2947 },
                        { 3359, 2938 },
                        { 3322, 2938 },
                        { 3322, 3007 }
                })
        };
    }

    @Override
    public void enter(final Player player) {
        super.enter(player);
        player.getTeleportManager().unlock(PortalTeleport.POLLNIVNEACH);
    }

    @Override
    public void process() {

    }

    @Override
    public String name() {
        return "Pollnivneach";
    }
}
