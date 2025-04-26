package com.zenyte.game.world.region;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.plugins.Plugin;

/**
 * @author Kris | 16. mai 2018 : 14:30:00
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public abstract class PolygonRegionArea extends AbstractRegionArea implements Plugin {

    private RSPolygon[] polygons;

    @Override
    public boolean hasPolygons() {
        return true;
    }

    protected abstract RSPolygon[] polygons();

    @Override
    public boolean inside(final Location location) {
        final RSPolygon[] polygons = getPolygons();
        if (polygons == null || polygons.length == 0) return false;
        for (int i = polygons.length - 1; i >= 0; i--) {
            final RSPolygon polygon = polygons[i];
            if (polygon.contains(location)) {
                return true;
            }
        }
        return false;
    }

    public RSPolygon[] getPolygons() {
        if (polygons == null) {
            polygons = polygons();
        }
        return polygons;
    }

    public RSPolygon getPolygon(int index) {
        return getPolygons()[index];
    }

    /**
     * Gets a random position within the area, this will work for square areas, not as nice for random shapes
     *
     * @return - a random location within
     */
    @Override
    public Location getRandomPosition() {
        RSPolygon random = Utils.getRandomElement(getPolygons());
        int bottomLeftX = Integer.MAX_VALUE;
        int bottomLeftY = Integer.MAX_VALUE;
        int topRightX = Integer.MIN_VALUE;
        int topRightY = Integer.MIN_VALUE;

        for (int[] values : random.getPoints()) {
            if (values[0] <= bottomLeftX) {
                bottomLeftX = values[0];
            }
            if (values[0] >= topRightX) {
                topRightX = values[0];
            }
            if (values[1] <= bottomLeftY) {
                bottomLeftY = values[1];
            }
            if (values[1] >= topRightY) {
                topRightY = values[1];
            }
        }
        Location randomLoc = null;

        while (randomLoc == null) {
            int x = Utils.random(bottomLeftX, topRightX);
            int y = Utils.random(bottomLeftY, topRightY);
            Location newLoc = new Location(x, y);
            if (inside(newLoc)) {
                randomLoc = newLoc;
            }
        }

        return randomLoc;
    }

    public static boolean isPolygonRegionArea(RegionArea regionArea) {
        return regionArea != null && regionArea.hasPolygons();
    }

}
