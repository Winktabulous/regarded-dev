package com.zenyte.game.content.area.prifddinas.zalcano;

import com.zenyte.game.world.Position;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;
import com.zenyte.game.world.region.area.plugins.DeathPlugin;
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin;

public class ZalcanoLair extends PolygonRegionArea implements DeathPlugin, CannonRestrictionPlugin, LootBroadcastPlugin {

    public static final String NAME = "Zalcano Lair";

    @Override
    public boolean isMultiwayArea(Position position) {
        return true;
    }

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{
                new RSPolygon(new int[][]{
                        {3046, 6036},
                        {3022, 6036},
                        {3022, 6062},
                        {3046, 6062},
                })
        };
    }

    @Override
    public void enter(Player player) {
        player.getCombatAchievements().setCurrentTaskValue(CAType.PERFECT_ZALCANO, 0);
    }

    @Override
    public void leave(Player player, boolean logout) {
        ZalcanoInstance.deleteTephra(player);
        player.getCombatAchievements().removeCurrentTask(CAType.PERFECT_ZALCANO);
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public boolean isSafe() {
        return false;
    }

    @Override
    public String getDeathInformation() {
        return "Zalcano";
    }

    @Override
    public Location getRespawnLocation() {
        return null;
    }

    @Override
    public boolean sendDeath(Player player, Entity source) {
        ZalcanoInstance.deleteTephra(player);
        return DeathPlugin.super.sendDeath(player, source);
    }
}
