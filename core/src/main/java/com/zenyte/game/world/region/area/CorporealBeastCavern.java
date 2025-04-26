package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.teleportsystem.PortalTeleport;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin;
import com.zenyte.game.world.region.area.plugins.RandomEventRestrictionPlugin;

/**
 * @author Kris | 15/04/2019 17:43
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CorporealBeastCavern extends PolygonRegionArea implements RandomEventRestrictionPlugin, LootBroadcastPlugin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 2944, 4416 },
                        { 2944, 4352 },
                        { 3008, 4352 },
                        { 3008, 4416 }
                }, 2)
        };
    }

    @Override
    public void enter(final Player player) {
        player.getTeleportManager().unlock(PortalTeleport.CORPOREAL_BEAST);
    }

    @Override
    public void leave(final Player player, final boolean logout) {

    }

    @Override
    public String name() {
        return "Corporeal Beast Cavern";
    }
}
