package com.zenyte.game.world.region.area.memberzones;

import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

public class LegendaryZone extends PolygonRegionArea {


    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{new RSPolygon(13430)};
    }

    @Override
    public void enter(Player player) {
        if(!player.getMemberRank().equalToOrGreaterThan(MemberRank.LEGENDARY)) {
            player.sendMessage(Colour.RS_RED.wrap("This is a legendary members only zone."));
            //tp them home
        }
    }

    @Override
    public void leave(Player player, boolean logout) {
    }

    @Override
    public String name() {
        return "Legendary Zone";
    }

}
