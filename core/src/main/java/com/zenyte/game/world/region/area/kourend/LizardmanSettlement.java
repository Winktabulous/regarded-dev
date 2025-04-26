package com.zenyte.game.world.region.area.kourend;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;

/**
 * @author Kris | 31/01/2019 03:35
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LizardmanSettlement extends GreatKourend implements CannonRestrictionPlugin {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 1309, 3518 },
                        { 1302, 3526 },
                        { 1295, 3528 },
                        { 1293, 3535 },
                        { 1288, 3540 },
                        { 1296, 3551 },
                        { 1296, 3556 },
                        { 1304, 3562 },
                        { 1304, 3569 },
                        { 1298, 3575 },
                        { 1311, 3586 },
                        { 1313, 3587 },
                        { 1320, 3587 },
                        { 1322, 3593 },
                        { 1326, 3593 },
                        { 1330, 3587 },
                        { 1335, 3588 },
                        { 1339, 3584 },
                        { 1350, 3580 },
                        { 1355, 3575 },
                        { 1349, 3564 },
                        { 1360, 3552 },
                        { 1356, 3531 },
                        { 1350, 3527 },
                        { 1334, 3535 },
                        { 1330, 3533 },
                        { 1327, 3534 },
                        { 1317, 3525 },
                        { 1326, 3518 }
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
    public String restrictionMessage() {
        return "The ground is too marshy here.";
    }

    @Override
    public String name() {
        return "Lizardman Settlement";
    }
}
