package com.zenyte.game.content.boss.wildernessbosses.spiders.venenatis;

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
public class SilkChasm extends WildernessArea implements LootBroadcastPlugin {

    private boolean first = true;

    @Override
    protected RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {
                { 3404, 10183 },
                { 3442, 10183 },
                { 3442, 10226 },
                { 3404, 10226 }
        })};
    }

    @Override
    public void enter(Player player) {
        if (first) {
            World.spawnNPC(NpcId.VENENATIS_6610, new Location(3423, 10203, 2));
            first = false;
        }
        player.getHpHud().open(NpcId.VENENATIS_6610, 850);
        super.enter(player);
    }

    @Override
    public void leave(Player player, boolean logout) {
        super.leave(player, logout);
        if(player.getHpHud() != null)
            player.getHpHud().close();
    }

    @Override
    public String name() {
        return "Silk Chasm";
    }

    @Override
    public boolean isMultiwayArea(Position position) {
        return true;
    }
}
