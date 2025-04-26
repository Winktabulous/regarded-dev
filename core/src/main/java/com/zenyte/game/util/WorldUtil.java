package com.zenyte.game.util;

import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class WorldUtil {
    public static Optional<Location> findEmptySquare(
        @NotNull final Location center,
        final int seekRadius,
        final int emptyRadius,
        final Optional<Predicate<Location>> optionalPredicate
    ) {
        assert emptyRadius > 0;
        assert seekRadius >= emptyRadius;
        final Predicate<Location> predicate = optionalPredicate.orElse(null);
        final int px = center.getX();
        final int py = center.getY();
        final int plane = center.getPlane();
        int radius = 0;
        if (World.isSquareFree(px, py, plane, emptyRadius)) {
            final Location tile = new Location(px, py, plane);
            if (predicate == null || predicate.test(tile)) {
                return Optional.of(tile);
            }
        }
        while (++radius <= seekRadius) {
            for (int y = py - radius + 1; y < py + radius; y++) {
                if (!World.isSquareFree(px - radius, y, plane, emptyRadius)) {
                    continue;
                }
                final Location tile = new Location(px - radius, y, plane);
                if (predicate != null && !predicate.test(tile)) continue;
                return Optional.of(tile);
            }
            for (int x = px - radius; x < px + radius; x++) {
                if (!World.isSquareFree(x, py + radius, plane, emptyRadius)) {
                    continue;
                }
                final Location tile = new Location(x, py + radius, plane);
                if (predicate != null && !predicate.test(tile)) continue;
                return Optional.of(tile);
            }
            for (int y = py + radius; y >= py - radius; y--) {
                if (!World.isSquareFree(px + radius, y, plane, emptyRadius)) {
                    continue;
                }
                final Location tile = new Location(px + radius, y, plane);
                if (predicate != null && !predicate.test(tile)) continue;
                return Optional.of(tile);
            }
            for (int x = px + radius - 1; x >= px - radius; x--) {
                if (!World.isSquareFree(x, py - radius, plane, emptyRadius)) {
                    continue;
                }
                final Location tile = new Location(x, py - radius, plane);
                if (predicate != null && !predicate.test(tile)) continue;
                return Optional.of(tile);
            }
        }
        return Optional.empty();
    }

    public static Optional<Location> findEmptySquareRandom(
            @NotNull final Location center,
            final int seekRadius,
            final int emptyRadius
    ) {
        assert emptyRadius > 0;
        assert seekRadius >= emptyRadius;
        final int px = center.getX();
        final int py = center.getY();
        final int plane = center.getPlane();
        int radius = 0;
        if (World.isSquareFree(px, py, plane, emptyRadius)) {
            final Location tile = new Location(px, py, plane);
            return Optional.of(tile);
        }

        List<Location> locations = new ArrayList<>();
        while (++radius <= seekRadius) {
            for (int y = py - radius + 1; y < py + radius; y++) {
                if (!World.isSquareFree(px - radius, y, plane, emptyRadius)) {
                    continue;
                }
                final Location tile = new Location(px - radius, y, plane);
                locations.add(tile);
            }
            for (int x = px - radius; x < px + radius; x++) {
                if (!World.isSquareFree(x, py + radius, plane, emptyRadius)) {
                    continue;
                }
                final Location tile = new Location(x, py + radius, plane);
                locations.add(tile);
            }
            for (int y = py + radius; y >= py - radius; y--) {
                if (!World.isSquareFree(px + radius, y, plane, emptyRadius)) {
                    continue;
                }
                final Location tile = new Location(px + radius, y, plane);
                locations.add(tile);
            }
            for (int x = px + radius - 1; x >= px - radius; x--) {
                if (!World.isSquareFree(x, py - radius, plane, emptyRadius)) {
                    continue;
                }
                final Location tile = new Location(x, py - radius, plane);
                locations.add(tile);
            }
        }

        if (locations.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(Utils.random(locations));
    }

}
