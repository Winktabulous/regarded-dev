package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;

/**
 * @author Kris | 31/01/2019 03:49
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MorUlRek extends TzHaarCity implements CannonRestrictionPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{new RSPolygon(new int[][]{{2492, 5180}, {2493, 5179}, {2493, 5176}, {2494, 5175}, {2494, 5174}, {2493, 5173}, {2493, 5172}, {2492, 5171}, {2492, 5170}, {2493, 5163}, {2494, 5162}, {2494, 5159}, {2495, 5158}, {2495, 5157}, {2494, 5156}, {2494, 5153}, {2493, 5152}, {2478, 5139}, {2476, 5139}, {2475, 5138}, {2474, 5138}, {2473, 5139}, {2472, 5139}, {2472, 5138}, {2465, 5122}, {2460, 5121}, {2459, 5121}, {2458, 5120}, {2457, 5120}, {2455, 5122}, {2453, 5122}, {2452, 5121}, {2450, 5121}, {2449, 5122}, {2448, 5122}, {2447, 5123}, {2439, 5123}, {2437, 5121}, {2436, 5121}, {2434, 5123}, {2433, 5123}, {2433, 5106}, {2455, 5079}, {2498, 5055}, {2545, 5072}, {2560, 5134}, {2523, 5179}})};
    }

    @Override
    public void enter(Player player) {

    }

    @Override
    public void leave(Player player, boolean logout) {

    }

    @Override
    public String name() {
        return "Mor Ul Rek";
    }
}
