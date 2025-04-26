package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;

/**
 * @author Kris | 31/01/2019 03:14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FremennikSlayerDungeon extends PolygonRegionArea implements CannonRestrictionPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 2686, 10048 },
                        { 2686, 9945 },
                        { 2728, 9945 },
                        { 2728, 9984 },
                        { 2816, 9984 },
                        { 2816, 10048 }
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
        return "Fremennik Slayer Dungeon";
    }

    @Override
    public String restrictionMessage() {
        return "The humid air in these tunnels won't do your cannon any good!";
    }
}
