package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.teleportsystem.PortalTeleport;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Tommeh | 21-11-2018 | 17:27
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class BrimhavenDungeon extends PolygonRegionArea {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] { new RSPolygon(new int[][]{
                { 2624, 9600 },
                { 2624, 9408 },
                { 2752, 9408 },
                { 2752, 9505 },
                { 2729, 9536 },
                { 2729, 9600 }
        })};
    }

    @Override
    public void enter(Player player) {
        player.getTeleportManager().unlock(PortalTeleport.BRIMHAVEN_DUNGEON);
    }

    @Override
    public void leave(Player player, boolean logout) {

    }

    @Override
    public String name() {
        return "Brimhaven Dungeon";
    }
}
