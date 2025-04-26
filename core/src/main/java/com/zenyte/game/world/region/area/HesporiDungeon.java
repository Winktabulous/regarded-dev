package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;

/**
 * @author Kris | 31/01/2019 03:19
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HesporiDungeon extends PolygonRegionArea implements CannonRestrictionPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon( new int[][]{
                        { 1235, 10099 },
                        { 1235, 10074 },
                        { 1259, 10074 },
                        { 1259, 10099 }
                }, 0)
        };
    }

    @Override
    public String restrictionMessage() {
        return "It's far too damp down here to set up a cannon.";
    }

    @Override
    public void enter(Player player) {

    }

    @Override
    public void leave(Player player, boolean logout) {

    }

    @Override
    public String name() {
        return "Hespori";
    }
}
