package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.teleportsystem.PortalTeleport;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 15/04/2019 17:13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Yanille extends KingdomOfKandarin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 2542, 3111 },
                        { 2540, 3109 },
                        { 2537, 3109 },
                        { 2536, 3108 },
                        { 2536, 3103 },
                        { 2533, 3100 },
                        { 2533, 3084 },
                        { 2536, 3081 },
                        { 2536, 3076 },
                        { 2537, 3075 },
                        { 2540, 3075 },
                        { 2542, 3073 },
                        { 2592, 3073 },
                        { 2620, 3073 },
                        { 2622, 3075 },
                        { 2622, 3099 },
                        { 2610, 3111 }
                })
        };
    }

    @Override
    public void enter(final Player player) {
        super.enter(player);
        player.getTeleportManager().unlock(PortalTeleport.YANILLE);
    }

    @Override
    public String name() {
        return "Yanille";
    }
}
