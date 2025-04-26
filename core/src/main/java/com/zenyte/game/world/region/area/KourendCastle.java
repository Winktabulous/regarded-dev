package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.teleportsystem.PortalTeleport;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.kourend.GreatKourend;

/**
 * @author Kris | 15/04/2019 17:05
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class KourendCastle extends GreatKourend {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{
                new RSPolygon(new int[][]{
                        {1595, 3686},
                        {1593, 3684},
                        {1593, 3681},
                        {1595, 3679},
                        {1595, 3668},
                        {1593, 3666},
                        {1593, 3663},
                        {1595, 3661},
                        {1598, 3661},
                        {1600, 3663},
                        {1611, 3663},
                        {1613, 3661},
                        {1613, 3658},
                        {1615, 3656},
                        {1618, 3656},
                        {1620, 3658},
                        {1652, 3658},
                        {1654, 3656},
                        {1657, 3656},
                        {1659, 3658},
                        {1659, 3661},
                        {1657, 3663},
                        {1657, 3684},
                        {1659, 3686},
                        {1659, 3689},
                        {1657, 3691},
                        {1654, 3691},
                        {1652, 3689},
                        {1620, 3689},
                        {1618, 3691},
                        {1615, 3691},
                        {1613, 3689},
                        {1613, 3686},
                        {1611, 3684},
                        {1600, 3684},
                        {1598, 3686}
                })
        };
    }

    @Override
    public void enter(final Player player) {
        super.enter(player);
        player.getTeleportManager().unlock(PortalTeleport.KOUREND);
    }

    @Override
    public String name() {
        return "Kourend Castle";
    }
}
