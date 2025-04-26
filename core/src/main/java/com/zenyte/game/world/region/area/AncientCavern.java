package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;

/**
 * @author Kris | 09/01/2019 18:26
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AncientCavern extends PolygonRegionArea implements CannonRestrictionPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{{1590, 5390}, {1590, 5294}, {1807, 5294}, {1807, 5390}})
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
        return "Ancient cavern";
    }
}
