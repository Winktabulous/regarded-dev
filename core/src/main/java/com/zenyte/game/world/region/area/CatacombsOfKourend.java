package com.zenyte.game.world.region.area;

import com.zenyte.game.world.Position;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.teleportsystem.PortalTeleport;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;

/**
 * @author Kris | 30/01/2019 20:03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CatacombsOfKourend extends PolygonRegionArea implements CannonRestrictionPlugin {

    public static final RSPolygon polygon = new RSPolygon(new int[][]{
            { 1585, 10950 },
            { 1585, 9975 },
            { 1751, 9975 },
            { 1751, 10950 }
    }

    , 0);

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                polygon
        };
    }

    @Override
    public void enter(Player player) {
        player.getTeleportManager().unlock(PortalTeleport.KOUREND_CATACOMBS);
    }

    @Override
    public void leave(Player player, boolean logout) {

    }

    @Override
    public boolean isMultiwayArea(Position position) {
        return true;
    }

    @Override
    public String name() {
        return "Catacombs of Kourend";
    }
}
