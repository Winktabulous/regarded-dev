package com.near_reality.game.content.gauntlet.map;

import com.near_reality.game.content.gauntlet.*;
import com.near_reality.game.content.gauntlet.hunllef.Hunllef;
import com.near_reality.game.content.gauntlet.hunllef.HunllefType;
import com.near_reality.game.content.gauntlet.item.actions.GauntletCrystalTeleport;
import com.near_reality.game.content.gauntlet.npc.GauntletMonsterType;
import com.near_reality.game.content.gauntlet.npc.GauntletNPC;
import com.near_reality.game.content.gauntlet.rewards.GauntletRewardType;
import com.near_reality.game.world.entity.player.TempIntefaceHandlerKt;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity._Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.pathfinding.Flags;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.DynamicArea;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.Region;
import com.zenyte.game.world.region.area.plugins.*;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfBoundaryException;
import com.zenyte.plugins.dialogue.PlainChat;
import it.unimi.dsi.fastutil.Pair;
import mgi.types.config.npcs.NPCDefinitions;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.zenyte.game.item.ItemId.*;
import static com.zenyte.game.world.entity.player.Player.DEATH_ANIMATION;

/**
 * @author Andys1814.
 * @since 1/20/2022.
 */
public final class GauntletMap extends DynamicArea implements TempPlayerStatePlugin, LogoutPlugin, TeleportPlugin, DeathPlugin, LootBroadcastPlugin, PlayerCombatPlugin {

    private static final Logger log = LoggerFactory.getLogger(GauntletMap.class);

    public static final Location OUTSIDE_LOCATION = new Location(3032, 6127, 1);

    public static final int ROOM_WIDTH = 7;

    public static final int ROOM_HEIGHT = 7;

    private final GauntletType type;

    private final int startingRoomX;

    private final int startingRoomY;

    private final GauntletRoom[][] grid = new GauntletRoom[ROOM_WIDTH][ROOM_HEIGHT];

    private final Map<Pair<Integer, Integer>, GauntletMonsterType> demiBossRooms = new HashMap<>();

    public GauntletMap(AllocatedArea allocatedArea, GauntletType type) {
        super(allocatedArea, 0, 0);
        this.type = type;

        // Starting room spawns randomly on one of the four sides of the Hunllef room.
        Direction direction = Utils.random(Direction.cardinalDirections);

        Pair<Integer, Integer> translation = getTranslation(direction);
        startingRoomX = 3 + translation.first();
        startingRoomY = 3 + translation.second();

        List<Pair<Integer, Integer>> demiBossRoomOptions = new ArrayList<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (GauntletRoom.isDemiBossRoom(i, j)) {
                    demiBossRoomOptions.add(Pair.of(i, j));
                }
            }
        }

        Pair<Integer, Integer> randomDemiBossRoom = Utils.random(demiBossRoomOptions);
        demiBossRoomOptions.remove(randomDemiBossRoom);
        demiBossRooms.put(randomDemiBossRoom, GauntletMonsterType.BEAR);

        randomDemiBossRoom = Utils.random(demiBossRoomOptions);
        demiBossRoomOptions.remove(randomDemiBossRoom);
        demiBossRooms.put(randomDemiBossRoom, GauntletMonsterType.DRAGON);

        randomDemiBossRoom = Utils.random(demiBossRoomOptions);
        demiBossRoomOptions.remove(randomDemiBossRoom);
        demiBossRooms.put(randomDemiBossRoom, GauntletMonsterType.DARK_BEAST);

        randomDemiBossRoom = Utils.random(demiBossRoomOptions);
        demiBossRoomOptions.remove(randomDemiBossRoom);
        demiBossRooms.put(randomDemiBossRoom, GauntletMonsterType.BEAR);

        randomDemiBossRoom = Utils.random(demiBossRoomOptions);
        demiBossRoomOptions.remove(randomDemiBossRoom);
        demiBossRooms.put(randomDemiBossRoom, GauntletMonsterType.DRAGON);

        randomDemiBossRoom = Utils.random(demiBossRoomOptions);
        demiBossRoomOptions.remove(randomDemiBossRoom);
        demiBossRooms.put(randomDemiBossRoom, GauntletMonsterType.DARK_BEAST);
    }

    @Override
    public void enter(Player player) {
        player.putBooleanTemporaryAttribute("gauntlet-started", true);
        /* Add starting items */
        var tempEquipment = player.getEquipmentTemp();
        if(!type.isNoPrep()) {
           tempEquipment.set(EquipmentSlot.WEAPON, new Item(type.isCorrupted() ? CORRUPTED_SCEPTRE : CRYSTAL_SCEPTRE));
           tempEquipment.getContainer().refresh();
        }
        generateInventory(player, type);
    }
    private void generateInventory(Player player, GauntletType type) {
        var tempInventory = player.getInventoryTemp();
        var tempEquipment = player.getEquipmentTemp();
        switch(type) {
            case STANDARD: {
                tempInventory.addItem(new Item(CRYSTAL_AXE_23862));
                tempInventory.addItem(new Item(CRYSTAL_PICKAXE_23863));
                tempInventory.addItem(new Item(CRYSTAL_HARPOON_23864));
                tempInventory.addItem(new Item(WEAPON_FRAME_23871));
                tempInventory.addItem(new Item(CRYSTAL_SHARDS, 50));

                tempInventory.addItem(new Item(PESTLE_AND_MORTAR_23865));
                tempInventory.addItem(new Item(TELEPORT_CRYSTAL));
                break;
            }
            case CORRUPTED: {
                tempInventory.addItem(new Item(CORRUPTED_AXE));
                tempInventory.addItem(new Item(CORRUPTED_PICKAXE));
                tempInventory.addItem(new Item(CORRUPTED_HARPOON));
                tempInventory.addItem(new Item(WEAPON_FRAME));
                tempInventory.addItem(new Item(CORRUPTED_SHARDS, 50));

                tempInventory.addItem(new Item(PESTLE_AND_MORTAR_23865));
                tempInventory.addItem(new Item(CORRUPTED_TELEPORT_CRYSTAL));
                break;
            }
            case STANDARD_NO_PREP: {
                tempEquipment.set(EquipmentSlot.HELMET, new Item(CRYSTAL_HELM_BASIC));
                tempEquipment.set(EquipmentSlot.PLATE, new Item(CRYSTAL_BODY_BASIC));
                tempEquipment.set(EquipmentSlot.LEGS, new Item(CRYSTAL_LEGS_BASIC));

                tempInventory.addItem(new Item(CRYSTAL_BOW_PERFECTED));
                tempInventory.addItem(new Item(CRYSTAL_HALBERD_PERFECTED));
                tempInventory.addItem(new Item(CRYSTAL_STAFF_PERFECTED));

                tempInventory.addItem(new Item(EGNIOL_POTION_4));
                tempInventory.addItem(new Item(PADDLEFISH, 24));
                break;
            }

            case CORRUPTED_NO_PREP: {
                tempEquipment.set(EquipmentSlot.HELMET, new Item(CORRUPTED_HELM_BASIC));
                tempEquipment.set(EquipmentSlot.PLATE, new Item(CORRUPTED_BODY_BASIC));
                tempEquipment.set(EquipmentSlot.LEGS, new Item(CORRUPTED_LEGS_BASIC));

                tempInventory.addItem(new Item(CORRUPTED_BOW_PERFECTED));
                tempInventory.addItem(new Item(CORRUPTED_HALBERD_PERFECTED));
                tempInventory.addItem(new Item(CORRUPTED_STAFF_PERFECTED));

                tempInventory.addItem(new Item(EGNIOL_POTION_4));
                tempInventory.addItem(new Item(CORRUPTED_PADDLEFISH, 4));
                tempInventory.addItem(new Item(PADDLEFISH, 20));
                break;
            }
        }
    }

    @Override
    public void leave(Player player, boolean logout) {
        player.putBooleanTemporaryAttribute("gauntlet-started", false);
        player.sendDeveloperMessage("Left GauntletMap");
        final Gauntlet gauntlet = GauntletPlayerAttributesKt.getGauntlet(player);
        if (gauntlet != null) {
            gauntlet.end(GauntletRewardType.NONE, true, true);
            if(logout) {
                player.getInventory().clear();
                player.getEquipment().clear();
            }
        }
        if(logout) {
            player.forceLocation(new Location(3032, 6127, 1));
        }
    }

    @Override
    public String name() {
        return "The Gauntlet";
    }

    @Override
    public void constructRegion() {
        if (constructed) {
            return;
        }
        GlobalAreaManager.add(this);
        constructed = true;
        constructed();
    }

    @Override
    public void constructed() {
        try {
            // Hunllef boss room always in the center of the grid.
            int bossChunkX = (3 * 2) + area.getChunkX();
            int bossChunkY = (3 * 2) + area.getChunkY();
            for (int plane = 0; plane <= 3; plane++) {
                MapBuilder.copySquare(area, 2, (type.isCorrupted() ? 246 : 238), 710,
                        plane, bossChunkX, bossChunkY, plane, 0);
            }

            GauntletRoom bossNode = new GauntletRoom(GauntletRoomType.MIDDLE, 3, 3, bossChunkX, bossChunkY, 0);
            grid[3][3] = bossNode;

            // Starting room on one of the four random sides of boss room, as computed in constructor above.
            int startChunkX = (getStartingRoomX() * 2) + area.getChunkX();
            int startChunkY = (getStartingRoomY() * 2) + area.getChunkY();
            for (int plane = 0; plane <= 3; plane++) {
                MapBuilder.copySquare(area, 2,
                        (type.isCorrupted() ? 246 : 238), 708,
                        plane, startChunkX, startChunkY, plane, 0);
            }

            GauntletRoom startNode = new GauntletRoom(GauntletRoomType.MIDDLE, getStartingRoomX(), getStartingRoomY()
                    , startChunkX, startChunkY, 0);
            grid[getStartingRoomX()][getStartingRoomY()] = startNode;

            WorldTasksManager.schedule(this::blockEmptyRoomEdges);
        } catch (OutOfBoundaryException e) {
            log.error("", e);
        }
    }

    private void blockEmptyRoomEdges() {
        for (int x = 0; x < ROOM_WIDTH; x++) {
            for (int y = 0; y < ROOM_HEIGHT; y++) {
                final GauntletRoom room = grid[x][y];
                if (room == null)
                    setRoomEdgeMasks(x, y, Flags.OBJECT);
            }
        }
    }

    private void setRoomEdgeMasks(int gridX, int gridY, int flag) {
        final int size = Region.CHUNK_SIZE * 2;
        final int baseX = getBaseXForNode(gridX);
        final int baseY = getBaseYForNode(gridY);

        for (int side = Region.CHUNK_SIZE-1; side < Region.CHUNK_SIZE+1; side++) {
            setObjectMask(baseX, baseY, 0, side, flag);
            setObjectMask(baseX, baseY, size -1, side, flag);
            setObjectMask(baseX, baseY, side, 0, flag);
            setObjectMask(baseX, baseY, side, size -1, flag);
        }
    }

    private static void setObjectMask(int baseX, int baseY, int x1, int y1, int flag) {

        int localX = (baseX + x1) & 63;
        int localY = (baseY + y1) & 63;
        int regionId = _Location.getRegionId(baseX + x1, baseY + y1);
//        System.out.println(regionId+": ["+localX+"]["+localY+"] = "+flag + " ("+baseX+", "+baseY+") {"+x1+", "+y1+"}");
        World.getRegion(regionId, true)
                .setMask(1, localX, localY, flag);
    }

    @Override
    public void onLogout(@NotNull Player player) {
        player.setLocation(OUTSIDE_LOCATION);
//        player.getInventory().clear();
//        player.getEquipment().clear();
        player.getVarManager().sendBit(9178, 0); // Reset world map
    }

//    @Override
//    public Location onLoginLocation() {
//        return OUTSIDE_LOCATION;
//    }

    @Override
    public void cleared() {
        if (players.isEmpty()) {
            destroyRegion();
        }
    }

    public GauntletRoomType getRequiredNodeType(int gridX, int gridY) {
        if ((gridX == 0 && gridY == 0) || (gridX == 0 && gridY == 6) || (gridX == 6 && gridY == 0) || (gridX == 6 && gridY == 6)) {
            return GauntletRoomType.CORNER;
        }

        if (gridX == 0 || gridX == 6 || gridY == 0 || gridY == 6) {
            return GauntletRoomType.EDGE;
        }

        return GauntletRoomType.MIDDLE;
    }

    public int getRotation(int gridX, int gridY) {
        // Corner nodes
        if (gridX == 6 && gridY == 6) return 0;
        if (gridX == 0 && gridY == 0) return 2;
        if (gridX == 0 && gridY == 6) return 3;
        if (gridX == 6 && gridY == 0) return 1;

        // Edge nodes
        if (gridX == 0) return 3;
        if (gridX == 6) return 1;
        if (gridY == 0) return 2;
        if (gridY == 6) return 0;

        // Node does not require a specific rotation - this is a middle room so we can just randomize it.
        return Utils.random(0, 3);
    }

    public void addRoom(Player player, GauntletRoomType type, @NonNegative int gridX, @NonNegative int gridY) {
        if (gridX > 6 || gridY > 6 || nodeExists(gridX, gridY)) {
            throw new IllegalArgumentException("Could not add Gauntlet node at grid location " + gridX + ", " + gridY);
        }

        int toChunkX = area.getChunkX() + (gridX * 2);
        int toChunkY = area.getChunkY() + (gridY * 2);

        int rotation = getRotation(gridX, gridY);

        GauntletRoom room = new GauntletRoom(type, gridX, gridY, toChunkX, toChunkY, rotation);
        grid[gridX][gridY] = room;

        try {
            setRoomEdgeMasks(gridX, gridY, 0);

            for (int plane = 0; plane <= 3; plane++) {
                MapBuilder.copySquare(area, 2,
                        this.type.isCorrupted()
                                ? room.getType().getCorruptedStaticChunkX()
                                : room.getType().getStaticChunkX(),
                        room.getStaticChunkY(),
                        plane, toChunkX, toChunkY, plane, rotation);
            }
        } catch (OutOfBoundaryException e) {
            log.error("Failed to copy map squares for room[{}][{}]", gridX, gridY, e);
        }


        // Need to explicitly load region so we can determine where resources and npcs should be spawn relative to
        // other objects.
        int regionId = (((toChunkX * 8) >> 6) << 8) + ((toChunkY * 8) >> 6);
        World.getRegion(regionId, true);

        player.getForceReloadMap().set(true);
        player.loadMapRegions(false);

        spawnResources(room);

        /* Update the map for the incoming node and surrounding ones. */
        updateMap(room);
        for (Direction direction : Direction.cardinalDirections) {
            Pair<Integer, Integer> translation = getTranslation(direction);
            int translatedX = gridX + translation.first();
            int translatedY = gridY + translation.second();
            if (validGridCoordinates(translatedX, translatedY) && nodeExists(translatedX, translatedY)) {
                updateMap(grid[translatedX][translatedY]);
            }
        }
        WorldTasksManager.schedule(this::blockEmptyRoomEdges);
    }

    public void spawnResources(GauntletRoom room) {
        room.findPossibleResourceLocations();

        final Pair<Integer, Integer> roomCoords = Pair.of(room.getGridX(), room.getGridY());

        /* Decide what resources we are spawning - ensure that room always has at least one. */
        final boolean demiBossRoom =
                demiBossRooms.containsKey(roomCoords) || (room.isDemiBossRoom() && Utils.random(100) <= 10);
        final boolean spawnObjects = Utils.random(100) >= 25;
        final boolean spawnNpcs = !demiBossRoom && (!spawnObjects || Utils.random(100) >= 25);

        final List<Location> singleTileLocations = room.getSingleTileResourceSpawnLocations();
        final List<Location> twoTileLocations = room.getTwoTileResourceSpawnLocations();

        if (singleTileLocations.isEmpty() && twoTileLocations.isEmpty()) {
            return;
        }

        /* Shuffle our locations to reduce "clumps" of resources. */
        Collections.shuffle(singleTileLocations);
        Collections.shuffle(twoTileLocations);

        final boolean corrupted = type.isCorrupted();

        if (demiBossRoom) {
            final GauntletMonsterType demiBoss = demiBossRooms.containsKey(roomCoords)
                    ? demiBossRooms.get(roomCoords)
                    : Utils.random(GauntletMonsterType.DEMI_BOSS);

            // Demi bosses seem to always spawn in the center 3x3 square
            final Location location = new Location((room.getChunkX() << 3) + 7, (room.getChunkY() << 3) + 7, 1);
            new GauntletNPC(corrupted ? demiBoss.getCorruptedNpcId() : demiBoss.getNpcId(),
                    location, Direction.SOUTH, 5, this).spawn();
            return;
        }

        if (spawnObjects) { // utils.random below was just 1 before probs should stay but want to make sue theres no
            // room with just 1 tree or something.
            int numDepleting = Utils.random(!spawnNpcs ? 2 : 1, room.isDemiBossRoom() ? 2 : 3); // Demi boss room:
            // only 2 max
            for (int i = 0; i < numDepleting; i++) {
                GauntletResource resource = Utils.random(GauntletResource.DEPLETING);

                List<Location> locations = resource == GauntletResource.FISHING_SPOT ? twoTileLocations :
                        singleTileLocations;
                Location location = locations.remove(0);

                WorldObject object = resource.toObject(location, corrupted);
                World.spawnObject(object);

                // If we spawned a two-tile location, remove all other tile locations that overlap it.
                // This prevents npcs and objects overlapping each other.
                if (locations == twoTileLocations) {
                    singleTileLocations.remove(location);

                    Predicate<Location> overlaps = (l) -> {
                        boolean translateX = location.getX() + 1 == l.getX() && location.getY() == l.getY();
                        boolean translateY = location.getX() == l.getX() && location.getY() + 1 == l.getY();
                        boolean translateXY = location.getX() + 1 == l.getX() && location.getY() + 1 == l.getY();
                        return translateX || translateY || translateXY;
                    };

                    singleTileLocations.removeIf(overlaps);
                    twoTileLocations.removeIf(overlaps);
                } else {
                    singleTileLocations.removeIf(l -> l.getTileDistance(location) <= 1);
                    twoTileLocations.removeIf(l -> l.getTileDistance(location) <= 1);
                }
                singleTileLocations.removeIf(l -> l.getX() == location.getX() || l.getY() == location.getY());
                twoTileLocations.removeIf(l -> l.getX() == location.getX() || l.getY() == location.getY());
            }
        }

        final int numGrymRoots = Utils.random(100) >= 20 ? 0 : Utils.random(6) <= 1 ? Utils.random(1, 3) : Utils.random(1, 2);
        for (int i = 0; i < numGrymRoots; i++) {
            final Location spawnTile = singleTileLocations.remove(0);
            final WorldObject object = GauntletResource.GRYM_ROOT.toObject(spawnTile, corrupted);
            World.spawnObject(object);
        }

        if (spawnNpcs) {
            final boolean tierOne = !room.isOuterRing() && Utils.random(3) <= 2;
            final GauntletMonsterType[] types = tierOne ? GauntletMonsterType.TIER_ONE : GauntletMonsterType.TIER_TWO;
            final int numNpcs = tierOne ? Utils.random(1, 4) : 2;
            for (int i = 0; i < numNpcs; i++) {
                final GauntletMonsterType npcType = Utils.random(types);
                final int npcId = corrupted ? npcType.getCorruptedNpcId() : npcType.getNpcId();
                final int npcSize = NPCDefinitions.get(npcId).getSize();
                final List<Location> locations = npcSize > 1 ? twoTileLocations : singleTileLocations;
                locations.stream().filter(loc ->
                    Stream.of(Direction.cardinalDirections)
                            .anyMatch(direction -> World.checkWalkStep(1, loc.getX(), loc.getY(), direction.getMovementDirection(), npcSize, true, false))
                ).findFirst().ifPresentOrElse((loc) -> {
                    locations.remove(loc);
                    new GauntletNPC(npcId, loc, Direction.SOUTH, 3, this).spawn();
                }, () -> {
                    log.error("Failed to find suitable spawn location for npc(id={}, size={})", npcId, npcSize);
                });
            }
        }
    }

    public void updateMap(GauntletRoom node) {
        //System.out.println(node.getLightLocations().size());
        // Lights on entrances
        for (Map.Entry<Direction, Pair<Location, Location>> entry : node.getLightLocations().entrySet()) {
            Pair<Integer, Integer> translation = getTranslation(entry.getKey());
            int translatedX = node.getGridX() + translation.first();
            int translatedY = node.getGridY() + translation.second();

            if (!validGridCoordinates(translatedX, translatedY)) {
                continue;
            }

            if (nodeExists(translatedX, translatedY)) {
                Pair<Location, Location> locations = entry.getValue();

                WorldObject first = World.getObjectWithType(locations.first(), 10);
                WorldObject second = World.getObjectWithType(locations.second(), 10);

                if (first != null) {
                    switch (first.getId()) {
                        case 36101, 36102, 35998, 35999 -> {
                            World.removeObject(first);
                            first.setId(first.getId() + 2);
                            World.spawnObject(first);
                        }
                    }
                }

                if (second != null) {
                    switch (second.getId()) {
                        case 36101, 36102, 35998, 35999 -> {
                            World.removeObject(second);
                            second.setId(second.getId() + 2);
                            World.spawnObject(second);
                        }
                    }
                }
            }
        }

        // Symbols
        for (Map.Entry<Direction, Pair<Location, Location>> entry : node.getSymbolLocations().entrySet()) {
            Pair<Integer, Integer> translation = getTranslation(entry.getKey());
            int translatedX = node.getGridX() + translation.first();
            int translatedY = node.getGridY() + translation.second();

            if (!validGridCoordinates(translatedX, translatedY) || nodeExists(translatedX, translatedY)) {
                Pair<Location, Location> locations = entry.getValue();

                WorldObject first = World.getObjectWithType(locations.first(), 10);
                WorldObject second = World.getObjectWithType(locations.second(), 10);

                if (first != null) {
                    switch (first.getId()) {
                        case 36097, 35994 -> {
                            World.removeObject(first);
                            first.setId(first.getId() - 1);
                            World.spawnObject(first);
                        }
                    }
                }

                if (second != null) {
                    switch (second.getId()) {
                        case 36097, 35994 -> {
                            World.removeObject(second);
                            second.setId(second.getId() - 1);
                            World.spawnObject(second);
                        }
                    }
                }
            }
        }
    }

    public Pair<Integer, Integer> getTranslation(Direction direction) {
        return switch (direction) {
            case NORTH -> Pair.of(0, 1);
            case SOUTH -> Pair.of(0, -1);
            case EAST -> Pair.of(1, 0);
            case WEST -> Pair.of(-1, 0);
            default -> throw new IllegalArgumentException("");
        };
    }

    public boolean validGridCoordinates(int gridX, int gridY) {
        return gridX >= 0 && gridX <= 6 && gridY >= 0 && gridY <= 6;
    }

    public boolean nodeExists(int gridX, int gridY) {
        if (gridX > 6 || gridY > 6) {
            return false;
        }

        return grid[gridX][gridY] != null;
    }

    public int getBaseXForNode(@NonNegative int nodeX) {
        return (nodeX * 2) + area.getChunkX() << 3;
    }

    public int getBaseYForNode(@NonNegative int nodeY) {
        return (nodeY * 2) + area.getChunkY() << 3;
    }

    public int getStartingRoomX() {
        return startingRoomX;
    }

    public int getStartingRoomY() {
        return startingRoomY;
    }

    public int getBossRoomX() {
        return 3;
    }

    public int getBossRoomY() {
        return 3;
    }

    public int[] chunkToRoomCoords(int chunkX, int chunkY) {
        int roomX = (chunkX - (area.getChunkX())) / 2;
        int roomY = (chunkY - (area.getChunkY())) / 2;

        return new int[]{roomX, roomY};
    }

    public boolean inSafeRoom(Location location) {
        int x = getBaseXForNode(getStartingRoomX());
        int y = getBaseYForNode(getStartingRoomY());
        return location.getX() >= x && location.getY() >= y && location.getX() <= x + 16 && location.getY() <= y + 16;
    }

    @Override
    public boolean canTeleport(Player player, Teleport teleport) {
        Gauntlet gauntlet = GauntletPlayerAttributesKt.getGauntlet(player);

        // Only allow teleporting with the Teleport Crystal while in Gauntlet.
        if (teleport instanceof GauntletCrystalTeleport)
            return true;
        if(gauntlet != null)
            gauntlet.end(GauntletRewardType.NONE, false, true);

        player.getDialogueManager().start(new PlainChat(player, "Your teleport attempt returns you to the lobby."));
        return false;
    }

    @Override
    public boolean sendDeath(Player player, Entity source) {
        player.setAnimation(Animation.STOP);
        player.lock();
        player.stopAll();
        player.getDeathMechanics().checkHardcoreIronManDeath();

        if (type.isCorrupted()) {
            player.incrementNumericAttribute("gauntletCorruptedDeathCount", 1);
            GauntletModule.updateCorruptedDeathStatistics();
        } else {
            player.incrementNumericAttribute("gauntletDeathCount", 1);
            GauntletModule.updateDeathStatistics();
        }

        if (player.getPrayerManager().isActive(Prayer.RETRIBUTION)) {
            player.getPrayerManager().applyRetributionEffect(source);
        }

        WorldTasksManager.schedule(new WorldTask() {
            int ticks;

            @Override
            public void run() {
                if (player.isFinished() || player.isNulled()) {
                    stop();
                    return;
                }
                if (ticks == 0) {
                    player.setAnimation(DEATH_ANIMATION);
                } else if (ticks == 2) {
                    player.sendMessage("Oh dear, you are dead!");

                    player.reset();

                    if (player.getVariables().isSkulled()) {
                        player.getVariables().setSkull(false);
                    }

                    final Gauntlet gauntlet = GauntletPlayerAttributesKt.getGauntlet(player);
                    if (gauntlet != null)
                        gauntlet.end(GauntletRewardType.NONE, false, false);

                    TempIntefaceHandlerKt.removeTempInterfaceBind(player, GameInterface.ORBS, "View World Map");

                    player.getVarManager().sendBit(9178, 0); // Reset world map

                } else if (ticks == 4) {
                    player.unlock();
                    player.getAppearance().resetRenderAnimation();
                    player.setAnimation(Animation.STOP);
                    stop();
                    return;
                }
                ticks++;
            }
        }, 0, 1);

        return true;
    }

    @Override
    public boolean isSafe() {
        return false;
    }

    @Override
    public String getDeathInformation() {
        return "The Gauntlet";
    }

    @Override
    public Location getRespawnLocation() {
        return OUTSIDE_LOCATION;
    }

    @Override
    public boolean isMultiwayArea(Position position) {
        return true;
    }

    @Override
    public boolean acceptDropBroadcast(@NotNull Item item, boolean guaranteedDrop) {
        return true; // Broadcast all drops within Gauntlet
    }

    @Override
    public boolean processCombat(Player player, Entity entity, String style) {
        return true;
    }

    @Override
    public void onAttack(Player player, Entity entity, String style, CombatSpell spell, boolean splash) {
        if (splash) {
            final Gauntlet gauntlet = GauntletPlayerAttributesKt.getGauntlet(player);
            if (gauntlet != null) {
                Hunllef hunllef = gauntlet.getHunllef();
                if (hunllef != null && hunllef.getType() != HunllefType.MAGIC_RESISTANT) {
                    hunllef.handleDifferentHit(HitType.MAGIC);
                }
            }
        }
    }

    @Override
    public boolean enableTempState(@NotNull Location location, @NotNull StateType type) {
        return type.equals(StateType.INVENTORY) || type.equals(StateType.EQUIPMENT);
    }
}
