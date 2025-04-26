package com.zenyte.game.world.region.area.godwars;

import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin;

/**
 * @author Kris | 07/01/2019 17:14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BandosChamberArea extends GodwarsDungeonArea implements LootBroadcastPlugin {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] { new RSPolygon(new int[][] { { 2864, 5370 }, { 2864, 5351 }, { 2877, 5351 }, { 2877, 5370 } }, 2) };
    }

    @Override
    public String name() {
        return "Godwars Dungeon: Bandos Chamber";
    }

}
