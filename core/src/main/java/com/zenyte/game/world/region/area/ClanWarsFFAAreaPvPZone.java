package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 07/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ClanWarsFFAAreaPvPZone extends ClanWarsFFAArea {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 3272, 4760 },
                        { 3272, 4856 },
                        { 3384, 4856 },
                        { 3384, 4760 }
                })
        };
    }

    @Override
    public void enter(final Player player) {
        player.setCanPvp(true);
    }

    @Override
    public void leave(final Player player, final boolean logout) {
        player.setCanPvp(false);
        if (!(GlobalAreaManager.getArea(player.getLocation()) instanceof ClanWarsFFAArea)) {
            super.leave(player, logout);
        }
    }

    @Override
    public String name() {
        return "Clan Wars: FFA (PvP)";
    }

}
