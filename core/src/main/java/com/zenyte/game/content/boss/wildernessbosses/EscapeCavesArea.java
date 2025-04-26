package com.zenyte.game.content.boss.wildernessbosses;

import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;

/**
 * @author Cresinkel
 */
public final class EscapeCavesArea extends WildernessArea {

    @Override
    protected RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {
                { 3358, 10241 },
                { 3375, 10245 },
                { 3392, 10264 },
                { 3392, 10279 },
                { 3377, 10303 },
                { 3344, 10302 },
                { 3327, 10282 },
                { 3327, 10259 },
                { 3342, 10243 }
        })};
    }

    @Override
    public void enter(Player player) {
        WorldTasksManager.schedule(new TickTask() {
            @Override
            public void run() {
                if (player.isNulled() || player.isFinished() || player.isDead() || player.isLocked()) {
                    stop();
                    return;
                }
                final RegionArea area = player.getArea();
                if (!(area instanceof EscapeCavesArea)) {
                    stop();
                    return;
                }
                if (ticks == 1) {
                    player.sendMessage(Colour.RED.wrap("You feel a chill down your spine as the air in the cave weakens you, the air seems to be getting thicker over time"));
                }
                if (ticks % 5 == 0) {
                    player.getPrayerManager().drainPrayerPoints(Math.max(1, ticks / 20));
                }
                ticks++;
            }
        }, 10 , 1);
        super.enter(player);
    }

    @Override
    public void leave(Player player, boolean logout) {
        super.leave(player, logout);
    }

    @Override
    public String name() {
        return "Escape Caves";
    }

    @Override
    public boolean isMultiwayArea(Position position) {
        return true;
    }
}
