package com.zenyte.game.world.region.area.wilderness;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.teleportsystem.PortalTeleport;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 19/04/2019 19:29
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class WesternDragonsArea extends WildernessArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 2960, 3632 },
                        { 2960, 3578 },
                        { 3006, 3578 },
                        { 3006, 3632 }
                }, 0)
        };
    }

    @Override
    public void enter(final Player player) {
        super.enter(player);
        player.getTeleportManager().unlock(PortalTeleport.WESTERN_DRAGONS);
    }

    @Override
    public String name() {
        return "Wilderness - Western dragons";
    }
}
