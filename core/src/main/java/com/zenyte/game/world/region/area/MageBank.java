package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.teleportsystem.PortalTeleport;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 15/04/2019 18:00
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MageBank extends PolygonRegionArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 2496, 4736 },
                        { 2496, 4672 },
                        { 2560, 4672 },
                        { 2560, 4736 }
                })
        };
    }

    @Override
    public void enter(final Player player) {
        player.getTeleportManager().unlock(PortalTeleport.MAGE_BANK);
    }

    @Override
    public void leave(final Player player, final boolean logout) {

    }

    @Override
    public String name() {
        return "Mage Bank";
    }
}
