package com.zenyte.game.world.entity.player;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.plugins.DeathPlugin;

/**
 * @author Kris | 28. juuni 2018 : 20:10:05
 * @author Jire
 */
public final class AreaManager {

    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private final transient Player player;

    private transient RegionArea area;

    private String lastDynamicAreaName;
    private int onEnterLocation;

    public AreaManager(final Player player) {
        this.player = player;
    }

    public boolean sendDeath(final Player player, final Entity source) {
        if (!(area instanceof DeathPlugin)) {
            return false;
        }
        return ((DeathPlugin) area).sendDeath(player, source);
    }

    public Location getGravestoneLocation() {
        if (!(area instanceof DeathPlugin)) {
            return null;
        }
        return ((DeathPlugin) area).gravestoneLocation();
    }

    public RegionArea getArea() {
        return area;
    }

    public void setArea(RegionArea area) {
        this.area = area;
    }

    public String getLastDynamicAreaName() {
        return lastDynamicAreaName;
    }

    public void setLastDynamicAreaName(String lastDynamicAreaName) {
        this.lastDynamicAreaName = lastDynamicAreaName;
    }

    public int getOnEnterLocation() {
        return onEnterLocation;
    }

    public void setOnEnterLocation(int onEnterLocation) {
        this.onEnterLocation = onEnterLocation;
    }

}
