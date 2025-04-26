package com.zenyte.game.content.boss.wildernessbosses.callisto;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;

/**
 * @author Andys1814
 */
public final class HuntersEnd extends WildernessArea implements LootBroadcastPlugin {

    private Callisto artio;

    @Override
    protected RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {
                { 1739, 11525 },
                { 1777, 11525 },
                { 1777, 11562 },
                { 1739, 11562 }
        })};
    }

    @Override
    public void enter(Player player) {
        player.getVarManager().sendBit(4605, false);
        player.getVarManager().sendBit(5961, true);
        if (artio == null) {
            artio = new Callisto(NpcId.ARTIO, new Location(1757, 11543), Direction.SOUTH, 3);
            artio.spawn();
        }
        if (!artio.isFinished()) {
            player.getHpHud().open(NpcId.ARTIO, artio.getHitpoints());
        }
        super.enter(player);
    }

    @Override
    public void leave(Player player, boolean logout) {
        player.getHpHud().close();
        super.leave(player, logout);
    }

    @Override
    public String name() {
        return "Hunter's End";
    }

    @Override
    public boolean isSinglesPlusArea(Position position) {

        return true;
    }
}
