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
 * @author Cresinkel
 */
public class SkeletalTomb extends WildernessArea implements LootBroadcastPlugin {

    private Vetion calvarion;

    @Override
    protected RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {
                { 1877, 11533 },
                { 1898, 11533 },
                { 1898, 11560 },
                { 1877, 11560 }
        })};
    }

    @Override
    public void enter(Player player) {
        if (calvarion == null) {
            calvarion = new Vetion(NpcId.CALVARION, new Location(1887, 11546, 1), Direction.SOUTH, 3);
            calvarion.spawn();
        }
        if (!calvarion.isFinished()) {
            player.getHpHud().open(NpcId.CALVARION, calvarion.getHitpoints());
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
        return "Skeletal Tomb";
    }

    @Override
    public boolean isSinglesPlusArea(Position position) {
        return true;
    }
}
