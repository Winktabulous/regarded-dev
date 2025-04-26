package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.teleportsystem.PortalTeleport;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 15/04/2019 17:18
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ShiloVillage extends Karamja {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 2822, 3007 },
                        { 2817, 3002 },
                        { 2817, 2950 },
                        { 2841, 2945 },
                        { 2865, 2945 },
                        { 2866, 2946 },
                        { 2867, 2946 },
                        { 2868, 2945 },
                        { 2869, 2945 },
                        { 2870, 2944 },
                        { 2873, 2944 },
                        { 2874, 2945 },
                        { 2876, 2945 },
                        { 2875, 2946 },
                        { 2875, 2961 },
                        { 2880, 2966 },
                        { 2879, 3004 },
                        { 2861, 3007 }
                })
        };
    }

    @Override
    public void enter(final Player player) {
        super.enter(player);
        player.getTeleportManager().unlock(PortalTeleport.SHILO_VILLAGE);
    }

    @Override
    public void leave(final Player player, final boolean logout) {

    }

    @Override
    public String name() {
        return "Shilo Village";
    }
}
