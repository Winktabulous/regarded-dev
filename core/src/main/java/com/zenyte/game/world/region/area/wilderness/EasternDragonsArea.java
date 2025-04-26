package com.zenyte.game.world.region.area.wilderness;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.teleportsystem.PortalTeleport;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 19/04/2019 19:30
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EasternDragonsArea extends WildernessArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 3332, 3708 },
                        { 3332, 3635 },
                        { 3391, 3635 },
                        { 3391, 3708 }
                }, 0)
        };
    }

    @Override
    public void enter(final Player player) {
        super.enter(player);
        player.getTeleportManager().unlock(PortalTeleport.EASTERN_DRAGONS);
    }

    @Override
    public String name() {
        return "Wilderness - Eastern Dragons";
    }
}
