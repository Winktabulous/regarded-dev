package com.zenyte.game.world.region.area.godwars;

import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin;

/**
 * @author Kris | 07/01/2019 17:17
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SaradominChamberArea extends GodwarsDungeonArea implements LootBroadcastPlugin {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{new RSPolygon(new int[][]{{2889, 5276}, {2889, 5258}, {2908, 5258}, {2908, 5276}}, 0)};
    }

    @Override
    public String name() {
        return "Godwars Dungeon: Saradomin Chamber";
    }

}
