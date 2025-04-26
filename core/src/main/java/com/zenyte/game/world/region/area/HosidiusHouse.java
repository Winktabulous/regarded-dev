package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.teleportsystem.PortalTeleport;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.kourend.GreatKourend;

/**
 * @author Kris | 15/04/2019 17:35
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HosidiusHouse extends GreatKourend {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 1749, 3645 },
                        { 1735, 3640 },
                        { 1712, 3637 },
                        { 1707, 3626 },
                        { 1717, 3614 },
                        { 1714, 3599 },
                        { 1714, 3589 },
                        { 1704, 3587 },
                        { 1691, 3593 },
                        { 1683, 3591 },
                        { 1688, 3585 },
                        { 1688, 3582 },
                        { 1691, 3562 },
                        { 1699, 3561 },
                        { 1712, 3563 },
                        { 1720, 3556 },
                        { 1721, 3540 },
                        { 1730, 3519 },
                        { 1730, 3514 },
                        { 1716, 3506 },
                        { 1707, 3495 },
                        { 1671, 3495 },
                        { 1661, 3465 },
                        { 1664, 3392 },
                        { 1920, 3392 },
                        { 1920, 3648 },
                        { 1749, 3648 }
                })
        };
    }

    @Override
    public void enter(final Player player) {
        super.enter(player);
        player.getTeleportManager().unlock(PortalTeleport.SAND_CRABS);
    }

    @Override
    public String name() {
        return "Great Kourend: Hosidius House";
    }
}
