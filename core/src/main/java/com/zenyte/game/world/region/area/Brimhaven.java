package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.teleportsystem.PortalTeleport;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 15/04/2019 17:17
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Brimhaven extends Karamja {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 2740, 3254 },
                        { 2688, 3222 },
                        { 2688, 3200 },
                        { 2704, 3141 },
                        { 2740, 3130 },
                        { 2787, 3144 },
                        { 2813, 3142 },
                        { 2816, 3152 },
                        { 2816, 3200 },
                        { 2786, 3242 }
                })
        };
    }

    @Override
    public void enter(final Player player) {
        super.enter(player);
        player.getTeleportManager().unlock(PortalTeleport.BRIMHAVEN);
    }


    @Override
    public String name() {
        return "Brimhaven";
    }
}
