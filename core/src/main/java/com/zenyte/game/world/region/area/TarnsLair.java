package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;

/**
 * @author Kris | 31/01/2019 03:34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TarnsLair extends PolygonRegionArea implements CannonRestrictionPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 3121, 4673 },
                        { 3121, 4537 },
                        { 3204, 4537 },
                        { 3204, 4673 }
                })
        };
    }

    @Override
    public void enter(Player player) {

    }

    @Override
    public String restrictionMessage() {
        return "This temple is ancient and would probably collapse if you started firing a cannon.";
    }

    @Override
    public void leave(Player player, boolean logout) {

    }

    @Override
    public String name() {
        return "Tarn's Lair";
    }
}
