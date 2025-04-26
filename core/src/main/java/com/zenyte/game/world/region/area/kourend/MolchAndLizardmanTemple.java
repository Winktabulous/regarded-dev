package com.zenyte.game.world.region.area.kourend;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;

/**
 * @author Kris | 31/01/2019 03:36
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MolchAndLizardmanTemple extends GreatKourend implements CannonRestrictionPlugin {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 1280, 10112 },
                        { 1280, 10048 },
                        { 1343, 10048 },
                        { 1343, 10112 }
                }),
                new RSPolygon(new int[][] {
                        { 1261, 3656 },
                        { 1266, 3661 },
                        { 1268, 3661 },
                        { 1275, 3666 },
                        { 1273, 3675 },
                        { 1278, 3690 },
                        { 1301, 3690 },
                        { 1312, 3695 },
                        { 1323, 3692 },
                        { 1323, 3687 },
                        { 1332, 3678 },
                        { 1342, 3674 },
                        { 1338, 3663 },
                        { 1331, 3657 }
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
        return "This ancient structure is too unstable to handle the idea of a cannon firing at its walls.";
    }

    @Override
    public String name() {
        return "Molch & Lizardman Temple";
    }
}
