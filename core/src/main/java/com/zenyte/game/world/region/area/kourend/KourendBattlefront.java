package com.zenyte.game.world.region.area.kourend;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Tommeh | 13/10/2019 | 19:01
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class KourendBattlefront extends GreatKourend {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] { new RSPolygon(new int[][] {
                { 1321, 3696 },
                { 1329, 3718 },
                { 1366, 3729 },
                { 1366, 3758 },
               // { 1367, 3762 },
                { 1377, 3772 },
                { 1388, 3772 },
                { 1393, 3763 },
                { 1391, 3758 },
                { 1393, 3753 },
                { 1397, 3748 },
                { 1407, 3748 },
                { 1407, 3743 },
                { 1398, 3742 },
                { 1398, 3736 },
                { 1397, 3734 },
                { 1397, 3729 },
                { 1399, 3727 },
                { 1402, 3729 },
                { 1403, 3729 },
                { 1408, 3726 },
                { 1410, 3724 },
                { 1413, 3724 },
                { 1417, 3726 },
                { 1422, 3726 },
                { 1429, 3728 },
                { 1438, 3730 },
                { 1455, 3726 },
                { 1454, 3723 },
                { 1452, 3718 },
                { 1456, 3710 },
                { 1456, 3702 },
                { 1456, 3695 },
                { 1460, 3691 },
                { 1461, 3688 },
                { 1453, 3688 },
                { 1445, 3693 },
                { 1431, 3689 },
                { 1422, 3692 },
                { 1406, 3685 },
                { 1398, 3689 },
                { 1395, 3687 },
                { 1400, 3679 },
                { 1395, 3673 },
                { 1379, 3675 },
                { 1379, 3679 },
                { 1371, 3685 },
                { 1362, 3693 },
                { 1355, 3689 },
                { 1348, 3691 },
                { 1342, 3684 },
                { 1348, 3681 },
                { 1341, 3678 }
        })};
    }

    @Override
    public void enter(Player player) {

    }

    @Override
    public void leave(Player player, boolean logout) {

    }

    @Override
    public String name() {
        return "Kourend Battlefront";
    }
}
