package com.zenyte.game.world.region;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.area.plugins.LoginPlugin;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

/**
 * @author Jire
 */
public abstract class AbstractRegionArea implements RegionArea {

    private static final Logger log = LoggerFactory.getLogger(AbstractRegionArea.class);

    private int areaTimer;

    protected final Set<Player> players = new ObjectOpenHashSet<>();

    protected RegionArea superArea;
    protected final List<RegionArea> subAreas = new ObjectArrayList<>();

    @Override
    public List<RegionArea> getSubAreas() {
        return subAreas;
    }

    @Override
    public RegionArea getSuperArea() {
        return superArea;
    }

    @Override
    public void setSuperArea(final RegionArea superArea) {
        this.superArea = superArea;
    }

    @Override
    public Set<Player> getPlayers() {
        return players;
    }

    @Override
    public int getAreaTimer() {
        return areaTimer;
    }

    @Override
    public void setAreaTimer(final int areaTimer) {
        this.areaTimer = areaTimer;
    }

    @Override
    public String toString() {
        return name();
    }

    void removePlayer(final Player player, final boolean logout) {
        if (!logout) {
            player.getAreaManager().setOnEnterLocation(0);
        }
        remove(player, logout);
    }

    void addPlayer(final Player player, final boolean login) {
        final Location location = player.getLocation();
        if (!inside(location)) return;

        if (login && this instanceof LoginPlugin) {
            try {
                ((LoginPlugin) this).login(player);
            } catch (Exception e) {
                log.error("", e);
            }
        }
        add(player);
    }

}
