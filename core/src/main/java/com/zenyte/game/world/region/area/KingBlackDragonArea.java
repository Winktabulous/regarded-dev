package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin;

/**
 * @author Kris | 21/11/2018 08:33
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class KingBlackDragonArea extends PolygonRegionArea implements LootBroadcastPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 2240, 4736 },
                        { 2240, 4672 },
                        { 2304, 4672 },
                        { 2304, 4736 }
                }, 0)
        };
    }

    @Override
    public void enter(Player player) {

    }

    @Override
    public void leave(Player player, boolean logout) {

    }

    @Override
    public String name() {
        return "King Black Dragon Lair";
    }
}
