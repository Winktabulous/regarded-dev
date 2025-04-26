package com.zenyte.game.content.boss.dagannothkings;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.WaterbirthDungeon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin;
import com.zenyte.game.world.region.area.plugins.RandomEventRestrictionPlugin;

/**
 * @author Kris | 21/11/2018 08:31
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DagannothKingsLair extends WaterbirthDungeon implements RandomEventRestrictionPlugin, LootBroadcastPlugin, CannonRestrictionPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 2880, 4480 },
                        { 2880, 4416 },
                        { 2944, 4416 },
                        { 2944, 4480 }
                }, 0)
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
        return "Dagannoth Kings Lair";
    }
}
