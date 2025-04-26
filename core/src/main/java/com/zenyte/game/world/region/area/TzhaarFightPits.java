package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;

/**
 * @author Kris | 31/01/2019 03:50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TzhaarFightPits extends TzHaarCity implements CannonRestrictionPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 2405, 5169 },
                        { 2400, 5169 },
                        { 2400, 5168 },
                        { 2399, 5168 },
                        { 2399, 5169 },
                        { 2372, 5169 },
                        { 2372, 5123 },
                        { 2423, 5124 },
                        { 2423, 5169 }
                })
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
        return "TzHaar Fight Pits";
    }
}
