package com.zenyte.game.world.region.area;

import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.kourend.GreatKourend;
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin;

/**
 * @author Kris | 31/07/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MountKaruulm extends GreatKourend implements LootBroadcastPlugin {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 1216, 3904 },
                        { 1216, 3788 },
                        { 1287, 3770 },
                        { 1316, 3764 },
                        { 1318, 3767 },
                        { 1323, 3767 },
                        { 1328, 3756 },
                        { 1354, 3752 },
                        { 1366, 3758 },
                        { 1375, 3771 },
                        { 1351, 3840 },
                        { 1344, 3840 },
                        { 1344, 3904 }
                })
        };
    }

    @Override
    public String name() {
        return "Mount Karuulm";
    }

}
