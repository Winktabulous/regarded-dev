package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.teleportsystem.PortalTeleport;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 15/04/2019 17:15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Rimmington extends KingdomOfAsgarnia {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 2911, 3230 },
                        { 2911, 3197 },
                        { 2944, 3195 },
                        { 2950, 3191 },
                        { 2971, 3185 },
                        { 2986, 3196 },
                        { 2985, 3224 },
                        { 2965, 3230 }
                })
        };
    }

    @Override
    public void enter(final Player player) {
        super.enter(player);
        player.getTeleportManager().unlock(PortalTeleport.RIMMINGTON);
    }

    @Override
    public String name() {
        return "Rimmington";
    }
}
