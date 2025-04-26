package com.zenyte.game.world.region.area.forthos;

import com.zenyte.game.world.Position;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;

/**
 * @author Andys1814
 */
public final class SarachnisArea extends PolygonRegionArea implements CannonRestrictionPlugin {

    @Override
    protected RSPolygon[] polygons() {
        return new RSPolygon[] {
                // 1809 9964
                new RSPolygon(new int[][]{
                        { 1833, 9910 }, //tl
                        { 1833, 9893 }, //bl
                        { 1850, 9893 }, //br
                        { 1850, 9910 }, //tr
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
    public boolean isMultiwayArea(Position position) {
        return true;
    }

    @Override
    public String name() {
        return "Sarachnis";
    }
}
