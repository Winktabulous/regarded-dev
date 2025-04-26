package com.zenyte.game.world.region.area.godwars;

import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin;

/**
 * @author Kris | 07/01/2019 17:17
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ArmadylChamberArea extends GodwarsDungeonArea implements LootBroadcastPlugin {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{new RSPolygon(new int[][]{{2824, 5309}, {2824, 5296}, {2843, 5296}, {2843, 5309}}, 2)};
    }

    @Override
    public String name() {
        return "Godwars Dungeon: Armadyl Chamber";
    }

}
