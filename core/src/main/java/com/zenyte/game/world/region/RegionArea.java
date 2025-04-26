package com.zenyte.game.world.region;

import com.zenyte.game.world.Position;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.AreaManager;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.areatype.AreaType;
import com.zenyte.game.world.region.areatype.AreaTypes;
import com.zenyte.plugins.Plugin;

import java.util.List;
import java.util.Set;

/**
 * @author Jire
 */
public interface RegionArea extends Plugin {

    default boolean isRaidArea() {
        return false;
    }

    default boolean isDynamicArea() {
        return false;
    }

    default boolean hasPolygons() {
        return false;
    }

    default boolean isWildernessArea(final Position position) {
        return false;
    }

    default boolean isMultiwayArea(final Position position) {
        return AreaTypes.MULTIWAY.matches(position);
    }

    default boolean isSinglesPlusArea(final Position position) {
        return AreaTypes.SINGLES_PLUS.matches(position);
    }

    default AreaType checkAreaType(final Position position) {
        if (isMultiwayArea(position)) return AreaTypes.MULTIWAY;
        if (isSinglesPlusArea(position)) return AreaTypes.SINGLES_PLUS;
        return AreaTypes.SINGLE_WAY;
    }

    default void addSub(final RegionArea subArea) {
        final List<RegionArea> subAreas = getSubAreas();
        if (!subAreas.contains(subArea)) {
            subAreas.add(subArea);
        }
    }

    List<RegionArea> getSubAreas();

    default void addSuper(final RegionArea superArea) {
        if (getSuperArea() != null) {
            throw new IllegalStateException("Super area of " + getClass() + " is already set!");
        }
        setSuperArea(superArea);
    }

    RegionArea getSuperArea();

    void setSuperArea(final RegionArea superArea);

    default RegionArea getSubmost(final Position tile, final boolean checkInside) {
        final Location location = tile.getPosition();
        if (checkInside && !inside(location)) return null;

        final List<RegionArea> subAreas = getSubAreas();
        for (final RegionArea subArea : subAreas) {
            if (subArea.inside(location)) {
                return subArea.getSubmost(location, false);
            }
        }

        return this;
    }

    default void add(final Player player) {
        final Set<Player> players = getPlayers();
        players.add(player);

        final AreaManager areaManager = player.getAreaManager();
        areaManager.setArea(this);

        enter(player);
    }

    default void remove(final Player player, final boolean logout) {
        final Set<Player> players = getPlayers();
        players.remove(player);

        leave(player, logout);
        player.getAreaManager().setArea(null);
    }

    void enter(final Player player);

    void leave(final Player player, boolean logout);

    String name();

    boolean inside(final Location location);

    Set<Player> getPlayers();

    int getAreaTimer();

    void setAreaTimer(final int areaTimer);

    Location getRandomPosition();

    default boolean safeHardcoreIronmanDeath() {
        return false;
    }

    default void move(Player player) {

    }

}
