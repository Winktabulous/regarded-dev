package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.teleportsystem.PortalTeleport;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 15/04/2019 17:25
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MosLeHarmless extends PolygonRegionArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 3648, 3072 },
                        { 3648, 2880 },
                        { 3904, 2880 },
                        { 3904, 3072 }
                })
        };
    }

    @Override
    public void enter(final Player player) {
        player.getTeleportManager().unlock(PortalTeleport.MOS_LE_HARMLESS);
    }

    @Override
    public void leave(final Player player, final boolean logout) {

    }

    @Override
    public String name() {
        return "Mos Le'Harmless";
    }
}
