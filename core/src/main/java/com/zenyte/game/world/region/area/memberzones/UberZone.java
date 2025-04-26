package com.zenyte.game.world.region.area.memberzones;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;


public class UberZone extends PolygonRegionArea {


    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{
                new RSPolygon(13434),
                new RSPolygon(13433),
                new RSPolygon(13690),
                new RSPolygon(13689),
        };
    }

    @Override
    public void enter(Player player) {
    }

    @Override
    public void leave(Player player, boolean logout) {
    }

    @Override
    public String name() {
        return "Uber Zone";
    }

}
