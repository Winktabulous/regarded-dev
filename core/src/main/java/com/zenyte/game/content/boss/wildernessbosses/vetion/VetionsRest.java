package com.zenyte.game.content.boss.wildernessbosses.vetion;

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
public final class VetionsRest extends WildernessArea implements LootBroadcastPlugin {

    private Vetion vetion;

    @Override
    protected RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {
                { 3285, 10189 },
                { 3305, 10189 },
                { 3305, 10216 },
                { 3285, 10216 }
        })};
    }

    @Override
    public void enter(Player player) {
        if (vetion == null) {
            vetion = new Vetion(NpcId.VETION, new Location(3295, 10202, 1), Direction.SOUTH, 3);
            vetion.spawn();
        }
        if (!vetion.isFinished()) {
            player.getHpHud().open(NpcId.VETION, vetion.getHitpoints());
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
        return "Vet'ion's Rest";
    }

    @Override
    public boolean isMultiwayArea(Position position) {
        return true;
    }
}
