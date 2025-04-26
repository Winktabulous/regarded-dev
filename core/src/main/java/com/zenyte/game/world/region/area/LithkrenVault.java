package com.zenyte.game.world.region.area;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 03/05/2019 00:46
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LithkrenVault extends PolygonRegionArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 1536, 5120 },
                        { 1536, 5056 },
                        { 1600, 5056 },
                        { 1600, 5120 }
                })
        };
    }

    @Override
    public void enter(final Player player) {
        player.getInterfaceHandler().sendInterface(InterfacePosition.OVERLAY, 98);
    }

    @Override
    public void leave(final Player player, final boolean logout) {
        player.getInterfaceHandler().closeInterface(InterfacePosition.OVERLAY);
    }

    @Override
    public String name() {
        return "Lithkren Dungeon";
    }
}
