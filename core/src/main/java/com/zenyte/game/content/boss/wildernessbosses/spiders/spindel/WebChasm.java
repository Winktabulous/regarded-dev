package com.zenyte.game.content.boss.wildernessbosses.spiders.spindel;

import com.zenyte.game.world.Position;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;

/**
 * @author Cresinkel
 */
public class WebChasm extends WildernessArea implements LootBroadcastPlugin {

    private boolean first = true;

    @Override
    protected RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {
                { 1610, 11524 },
                { 1651, 11524 },
                { 1651, 11571 },
                { 1610, 11571 }
        })};
    }

    @Override
    public void enter(Player player) {
        if (first) {
            World.spawnNPC(NpcId.SPINDEL, new Location(1630, 11547, 2));
            first = false;
        }
        player.getHpHud().open(NpcId.SPINDEL, 515);
        super.enter(player);
    }

    @Override
    public void leave(Player player, boolean logout) {
        player.getHpHud().close();
        super.leave(player, logout);
    }

    @Override
    public String name() {
        return "Web Chasm";
    }

    @Override
    public boolean isSinglesPlusArea(Position position) {
        return true;
    }
}
