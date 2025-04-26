package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin;
import com.zenyte.game.world.region.area.taskonlyareas.KrakenCove;

/**
 * @author Kris | 23/10/2018 14:32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class KrakenBossArea extends KrakenCove implements LootBroadcastPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{
                new RSPolygon(new int[][]{
                        {2275, 10045}, {2270, 10040}, {2270, 10033}, {2272, 10031}, {2275, 10031}, {2277, 10029},
                        {2277, 10023}, {2278, 10022}, {2283, 10022}, {2284, 10023}, {2284, 10029}, {2286, 10031},
                        {2289, 10031}, {2291, 10033}, {2291, 10040}, {2286, 10045}
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
        return "Kraken Boss Room";
    }
}
