package com.zenyte.game.content.chambersofxeric.map;

import com.zenyte.game.GameConstants;
import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.chambersofxeric.greatolm.OlmRoom;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kris | 05/07/2019 03:19
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class RaidMap {
    private static final Logger log = LoggerFactory.getLogger(RaidMap.class);
    /**
     * Arguments used to initiate the respective room.
     */
    private static final Class<?>[] arguments = new Class[] {RaidRoom.class, Raid.class, int.class, int.class, int.class, int.class, int.class, int.class, int.class, int.class};
    /**
     * The algorithm that calculates all-things map related.
     */
    private final MapAlgorithm algorithm;
    /**
     * The actual raid party which ties everything together.
     */
    private final Raid raid;
    @NotNull
    private final List<RaidArea> raidChunks;
    /**
     * Creates a 5x7 map piece for the boss. This map piece is never rotated.
     */
    private OlmRoom boss;
    @Nullable
    private AllocatedArea allocatedArea;

    public RaidMap(final Raid raid) {
        this.algorithm = new MapAlgorithm(raid.isChallengeMode(), raid.getParty().getPlayer());
        this.raid = raid;
        raidChunks = new ArrayList<>();
    }

    /**
     * Constructs a layout and builds a new map around that.
     *
     * @throws OutOfSpaceException if the game has ran out of free map space.
     */
    public void construct() throws OutOfSpaceException {
        if (raid.isChallengeMode()) {
            algorithm.setChallengeFloor();
        } else {
            algorithm.generate();
        }
        final Rectangle boundaries = algorithm.boundaries;
        assert boundaries != null;
        final int width = (int) (boundaries.getMaxX() - boundaries.getMinX());
        final int height = (int) (boundaries.getMaxY() - boundaries.getMinY());
        allocatedArea = MapBuilder.findEmptyChunk((width + 1) * 4, (height + 1) * 4);
        buildWrappers(allocatedArea);
        constructRooms(allocatedArea, algorithm.topFloorPalette, 3);
        constructRooms(allocatedArea, algorithm.middleFloorPalette, 2);
        constructRooms(allocatedArea, algorithm.bottomFloorPalette, 1);
        constructBoss();
        bindTeleportationPositions();
        if (GameConstants.WORLD_PROFILE.isDevelopment()) {
            System.err.println(allocatedArea.getCenterLocation());
        }
    }

    /**
     * Finds enough space to build a boss room and attempts to construct one.
     *
     * @throws OutOfSpaceException if there isn't enough space to construct a boss room.
     */
    private void constructBoss() throws OutOfSpaceException {
        final AllocatedArea bossArea = MapBuilder.findEmptyChunk(5, 7);
        boss = new OlmRoom(raid, bossArea.getChunkX() + 1, bossArea.getChunkY() + 1);
        boss.setArea(bossArea);
        boss.constructRegion();
    }

    /**
     * Erased the built map entirely.
     */
    public void erase() {
        assert allocatedArea != null;
        MapBuilder.destroy(allocatedArea);
        MapBuilder.destroy(boss.getArea());
        for (final RaidArea chunk : this.raidChunks) {
            GlobalAreaManager.remove(chunk);
        }
        GlobalAreaManager.remove(boss);
    }

    /**
     * Builds wrapper chunks on all floors of this raid; The ones allocated by actual map will be overwritten later on.
     *
     * @param allocatedArea the allocated area which we use for boundary-calculation.
     */
    private final void buildWrappers(@NotNull final AllocatedArea allocatedArea) {
        for (int z = 3; z >= 2; z--) {
            for (int x = allocatedArea.getChunkX(); x < (allocatedArea.getChunkX() + 16); x++) {
                for (int y = allocatedArea.getChunkY(); y < (allocatedArea.getChunkY() + 8); y++) {
                    final WrapperChunk wrapperChunk = new WrapperChunk(1, 1, 416, 647, x, y, 0, z, Utils.random(3));
                    wrapperChunk.setArea(allocatedArea);
                    wrapperChunk.constructRegion();
                }
            }
        }
    }

    /**
     * Constructs the rooms on the floor requested, actually overrides the wrapper chunks with real map.
     *
     * @param allocatedArea the allocated area used to check boundaries.
     * @param floorPalette  the palette which was used in floor generation.
     * @param height        the height level where the rooms are built to.
     */
    private void constructRooms(@NotNull final AllocatedArea allocatedArea, @Nullable final MapPalette floorPalette, final int height) {
        if (floorPalette == null) {
            return;
        }
        for (final MapChunk room : floorPalette) {
            final RaidArea instance = getChunk(room.room, room.chunkDirection, (room.x * 4) + allocatedArea.getChunkX(), (room.y * 4) + allocatedArea.getChunkY(), room.direction.ordinal(), height);
            instance.setArea(allocatedArea);
            instance.constructRegion();
            raidChunks.add(instance);
        }
    }

    /**
     * Binds the teleportation positions to the actual rooms, so for example, olm room will be bound to the room that takes the user to the olm room and vice versa.
     * This is just to connect the rooms together, so if an user uses the rope or the hole, they will be teleported to the correct locaction.
     */
    private final void bindTeleportationPositions() {
        boss.setEntrance(boss.getLocation(3232, 5720, 0));
        boss.setCenter(boss.getLocation(3232, 5734, 0));
        for (int index = 0; index < raidChunks.size(); index++) {
            final RaidArea chunk = raidChunks.get(index);
            final RaidRoom type = chunk.getType();
            if (!(type == RaidRoom.RAID_START || type == RaidRoom.FLOOR_START_UPSTAIRS || type == RaidRoom.FLOOR_END_DOWNSTAIRS)) {
                continue;
            }
            if (type == RaidRoom.RAID_START) {
                chunk.setBoundTile(chunk.getRespectiveTile(chunk.getLocation(3278, 5187, chunk.getToPlane()), chunk.getLocation(3298, 5188, 0), chunk.getLocation(3330, 5188, chunk.getToPlane())));
            } else if (type == RaidRoom.FLOOR_START_UPSTAIRS) {
                final RaidArea previousRoom = raidChunks.get(index - 1);
                chunk.setBoundTile(previousRoom.getRespectiveTile(previousRoom.getLocation(3278, 5168, previousRoom.getToPlane()), previousRoom.getLocation(3308, 5173, previousRoom.getToPlane()), null));
            } else {
                final RaidArea nextRoom = index == raidChunks.size() - 1 ? boss : raidChunks.get(index + 1);
                chunk.setBoundTile(nextRoom == boss ? boss.getEntrance() : nextRoom.getRespectiveTile(nextRoom.getLocation(3280, 5711, nextRoom.getToPlane()), nextRoom.getLocation(3312, 5711, nextRoom.getToPlane()), nextRoom.getLocation(3344, 5711, nextRoom.getToPlane())));
                if (nextRoom == boss) {
                    boss.setBoundTile(chunk.getRespectiveTile(chunk.getLocation(3278, 5168, chunk.getToPlane()), chunk.getLocation(3308, 5173, chunk.getToPlane()), null));
                }
            }
        }
    }

    /**
     * @param room      the room being constructed.
     * @param direction the direction of the room; Raids rooms can either go west, north or east. Never south.
     * @param x         the chunk x coordinate where the room will be placed.
     * @param y         the chunk y coordinate where the room will be placed.
     * @param rotation  the rotation of the room as done by the map packet.
     * @param toPlane   the height level to which the area is constructed.
     * @return the area that is to be constructed.
     */
    private RaidArea getChunk(final RaidRoom room, final int direction, final int x, final int y, final int rotation, final int toPlane) {
        try {
            return room.getParentClass().getDeclaredConstructor(arguments).newInstance(room, raid, rotation, 4, 408 + (4 * (direction)), room.getStaticChunkY(), x, y, room.getHeight(), toPlane);
        } catch (final Exception e) {
            throw new RuntimeException("Failure constructing a chunk: Mismatching constructor parameters", e);
        }
    }

    public MapAlgorithm getAlgorithm() {
        return algorithm;
    }

    public List<RaidArea> getRaidChunks() {
        return raidChunks;
    }

    public OlmRoom getBoss() {
        return boss;
    }
}
