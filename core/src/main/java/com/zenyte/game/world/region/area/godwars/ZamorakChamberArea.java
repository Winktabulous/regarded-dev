package com.zenyte.game.world.region.area.godwars;

import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin;

/**
 * @author Kris | 07/01/2019 17:16
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ZamorakChamberArea extends GodwarsDungeonArea implements LootBroadcastPlugin {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{new RSPolygon(new int[][]{{2918, 5332}, {2918, 5318}, {2937, 5318}, {2937, 5332}}, 2)};
    }

    @Override
    public String name() {
        return "Godwars Dungeon: Zamorak Chamber";
    }

}
