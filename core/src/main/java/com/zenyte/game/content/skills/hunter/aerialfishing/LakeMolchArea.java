package com.zenyte.game.content.skills.hunter.aerialfishing;

import com.zenyte.game.content.skills.hunter.aerialfishing.npc.FishingSpotNpc;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.kourend.GreatKourend;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;
import kotlin.collections.CollectionsKt;
import kotlin.random.Random;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * @author Cresinkel (certified clown)
 */
public class LakeMolchArea extends GreatKourend implements CycleProcessPlugin {

    public static int fishingSpots = 0;
    private static final Set<Location> SPAWN_LOCATIONS = new HashSet<>();
    private static final Set<Location> USED_SPAWN_LOCATIONS = new HashSet<>();
    private static final Logger LOGGER = Logger.getLogger(LakeMolchArea.class.getName());
    private static final int MAX_SPOT_COUNT = 30;

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{
                new RSPolygon(new int[][]{
                        {1354, 3620},
                        {1354, 3646},
                        {1381, 3646},
                        {1381, 3620}
                })
        };
    }

    @Override
    public void leave(Player player, boolean logout) {
        if (player.getEquipment().containsAnyOf(ItemId.CORMORANTS_GLOVE_22817, ItemId.CORMORANTS_GLOVE)) {
            player.sendMessage("Alry's cormorant flies back to him.");
            player.getEquipment().deleteItem(EquipmentSlot.WEAPON.getSlot(), new Item(ItemId.CORMORANTS_GLOVE_22817));
            player.getEquipment().deleteItem(EquipmentSlot.WEAPON.getSlot(), new Item(ItemId.CORMORANTS_GLOVE));
        }
    }

    @Override
    public String name() {
        return "Lake Molch";
    }

    @Override
    public void process() {
        if (fishingSpots == 0) {
            if (!USED_SPAWN_LOCATIONS.isEmpty())
                LOGGER.warning("Fishing spots are empty but there are still " + USED_SPAWN_LOCATIONS.size() + " used spawn locations");
            SPAWN_LOCATIONS.clear();
            SPAWN_LOCATIONS.addAll(generatePossibleSpawnLocations(1353, 1382, 3619, 3640));
            LOGGER.info("Generated " + SPAWN_LOCATIONS.size() + " possible spawn locations");
            for (int i = 0; i < MAX_SPOT_COUNT; i++)
                spawnSpot();
        }
    }

    public static void spawnSpot() {
        generateRandomSpotLocation()
                .ifPresentOrElse(fishSpawnTask(), () -> LOGGER.warning("Failed to spawn fishing spot"));
    }

    public static void handleCaughtSpot(NPC npc) {
        final Location npcPos = npc.getLocation();
        removeUsedSpawnLocation(npcPos);
        npc.remove();
        spawnCaughtSpot(npcPos);
    }

    private static void spawnCaughtSpot(Location spotPos) {
        findRandomLocationAround(spotPos)
                .ifPresentOrElse(fishSpawnTask(), () -> LOGGER.warning("Failed to spawn fishing spot"));
    }

    private static @NotNull Consumer<Location> fishSpawnTask() {
        return location -> {
            final FishingSpotNpc spot = new FishingSpotNpc(8523, location, Direction.NORTH, 0);
            spot.spawn();
//            LOGGER.info("Spawned fishing spot at " + location.toString());
            USED_SPAWN_LOCATIONS.add(location);
            fishingSpots++;
        };
    }

    public static void removeUsedSpawnLocation(Location location) {
//        LOGGER.info("Removing fishing spot at " + location.toString());
        USED_SPAWN_LOCATIONS.remove(location);
        fishingSpots--;
    }

    private static Set<Location> generatePossibleSpawnLocations(int minX, int maxX, int minY, int maxY) {
        final Set<Location> locations = new HashSet<>();
        int maxIterations = 1_000;
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                if (--maxIterations <= 0) // fail safe in case provided dimensions are way too big
                    return locations;
                if (isValidLocation(x, y))
                    locations.add(new Location(x, y, 0));
            }
        }
        return locations;
    }

    private static Optional<Location> findRandomLocationAround(Location spot) {
        final Set<Location> possibleLocations = CollectionsKt.intersect(
                generatePossibleSpawnLocations(spot.getX() - 3, spot.getX() + 3, spot.getY() - 3, spot.getY() + 3),
                SPAWN_LOCATIONS
        );
        return findRandomLocation(possibleLocations);
    }


    private static @NotNull Optional<Location> generateRandomSpotLocation() {
        return findRandomLocation(SPAWN_LOCATIONS);
    }

    private static @NotNull Optional<Location> findRandomLocation(Set<Location> possibleLocations) {
        final Set<Location> freeLocations = CollectionsKt.subtract(possibleLocations, USED_SPAWN_LOCATIONS);
        if (freeLocations.isEmpty())
            return Optional.empty();
        final Location randomLocation = CollectionsKt.random(freeLocations, Random.Default);
        if (World.findNPCAt(8523, randomLocation).isPresent()) {
            LOGGER.warning("Supposedly valid location is already occupied by a fishing spot: " + randomLocation.toString());
            return Optional.empty();
        } else
            return Optional.of(randomLocation);
    }

    private static boolean isValidLocation(int x, int y) {
        if (y < 3625) {
            return true;
        } else if (x < 1359) {
            return true;
        } else if (x > 1376) {
            return true;
        } else if (y == 3625 && (x >= 1368 && x <= 1371)) {
            return false;
        } else if (y == 3626 && (x >= 1365 && x <= 1374)) {
            return false;
        } else if (y == 3627 && (x >= 1362 && x <= 1375)) {
            return false;
        } else if (y == 3628 && x >= 1361) {
            return false;
        } else if (y == 3629 && x >= 1360) {
            return false;
        } else if (y == 3630 && x >= 1360) {
            return false;
        } else if (y == 3631 && x >= 1360) {
            return false;
        } else if (y == 3632 && (x >= 1360 && x <= 1375)) {
            return false;
        } else if (y == 3633 && x <= 1375) {
            return false;
        } else if (y == 3634) {
            return false;
        } else if (y == 3635) {
            return false;
        } else if (y == 3636 && x <= 1375) {
            return false;
        } else return y < 3637;
    }
}
