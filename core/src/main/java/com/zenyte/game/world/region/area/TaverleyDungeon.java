package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.teleportsystem.PortalTeleport;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 15/04/2019 17:49
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TaverleyDungeon extends PolygonRegionArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 2816, 9856 },
                        { 2816, 9833 },
                        { 2814, 9832 },
                        { 2814, 9825 },
                        { 2816, 9823 },
                        { 2816, 9728 },
                        { 2880, 9728 },
                        { 2880, 9664 },
                        { 2944, 9664 },
                        { 2944, 9728 },
                        { 2944, 9765 },
                        { 2973, 9765 },
                        { 2973, 9800 },
                        { 2952, 9800 },
                        { 2944, 9805 },
                        { 2944, 9856 },
                        { 2752, 9856 },
                        { 2752, 9792 },
                        { 2816, 9792 }
                }), new RSPolygon(new int[][]{
                { 2752, 9856 },
                { 2752, 9664 },
                { 3008, 9664 },
                { 3008, 9856 }
        }, 1)
        };
    }

    @Override
    public void enter(final Player player) {
        player.getTeleportManager().unlock(PortalTeleport.TAVERLEY_DUNGEON);
    }

    @Override
    public void leave(final Player player, final boolean logout) {

    }

    @Override
    public String name() {
        return "Taverley Underground";
    }
}
