package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;

/**
 * @author Kris | 09/01/2019 18:27
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AlKharidPalace extends DesertArea implements CannonRestrictionPlugin {


    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] { new RSPolygon(new int[][]{{3280, 3180}, {3280, 3157}, {3306, 3157}, {3306, 3180}}) };
    }

    @Override
    public void enter(Player player) {
    }

    @Override
    public void leave(Player player, boolean logout) {
    }

    @Override
    public String name() {
        return "Al-Kharid Palace";
    }

}
