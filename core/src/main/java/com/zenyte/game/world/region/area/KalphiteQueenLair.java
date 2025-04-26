package com.zenyte.game.world.region.area;

import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin;

/**
 * @author Kris | 21/11/2018 08:44
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class KalphiteQueenLair extends KalphiteLair implements LootBroadcastPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 3456, 9536 },
                        { 3456, 9472 },
                        { 3520, 9472 },
                        { 3520, 9536 }
                }, 0)
        };
    }

    @Override
    public String name() {
        return "Kalphite Queen Lair";
    }
}
