package com.zenyte.game.content.chambersofxeric.map;

import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * @author Kris | 28/06/2019 14:25
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MapAlgorithm {
    @NotNull
    final MapPalette topFloorPalette;
    @NotNull
    final MapPalette middleFloorPalette;
    @Nullable
    final MapPalette bottomFloorPalette;
    @NotNull
    private final RoomGeneration generation;
    @Nullable
    Rectangle boundaries;

    MapAlgorithm(final boolean challenge, @NotNull final String partyOwner) {
        this.topFloorPalette = new MapPalette(challenge);
        this.middleFloorPalette = new MapPalette(challenge);
        this.bottomFloorPalette = challenge ? new MapPalette(true) : null;
        this.generation = new RoomGeneration(partyOwner);
    }

    /**
     * Sets the floor layout to match the static pattern that OS uses in challenge raids.
     */
    void setChallengeFloor() {
        topFloorPalette.add(new MapChunk(RaidRoom.RAID_START), ChunkDirection.WEST);
        topFloorPalette.add(new MapChunk(RaidRoom.TEKTON), ChunkDirection.WEST);
        topFloorPalette.add(new MapChunk(RaidRoom.CRAB_PUZZLE), ChunkDirection.NORTH);
        topFloorPalette.add(new MapChunk(RaidRoom.SMALL_SCAVENGER_RUNT), ChunkDirection.EAST);
        topFloorPalette.add(new MapChunk(RaidRoom.ICE_DEMON), ChunkDirection.EAST);
        topFloorPalette.add(new MapChunk(RaidRoom.LIZARDMEN_SHAMAN), ChunkDirection.EAST);
        topFloorPalette.add(new MapChunk(RaidRoom.RESOURCES_B), ChunkDirection.SOUTH);
        topFloorPalette.add(new MapChunk(RaidRoom.FLOOR_END_DOWNSTAIRS), ChunkDirection.SOUTH);
        topFloorPalette.crop();
        middleFloorPalette.add(new MapChunk(RaidRoom.FLOOR_START_UPSTAIRS), ChunkDirection.WEST);
        middleFloorPalette.add(new MapChunk(RaidRoom.VANGUARD), ChunkDirection.WEST);
        middleFloorPalette.add(new MapChunk(RaidRoom.CREATURE_KEEPER), ChunkDirection.WEST);
        middleFloorPalette.add(new MapChunk(RaidRoom.SMALL_SCAVENGER_RUNT), ChunkDirection.NORTH);
        middleFloorPalette.add(new MapChunk(RaidRoom.VESPULA), ChunkDirection.EAST);
        middleFloorPalette.add(new MapChunk(RaidRoom.RESOURCES_A), ChunkDirection.EAST);
        middleFloorPalette.add(new MapChunk(RaidRoom.DEATHLY_ROOM), ChunkDirection.EAST);
        middleFloorPalette.add(new MapChunk(RaidRoom.FLOOR_END_DOWNSTAIRS), ChunkDirection.EAST);
        middleFloorPalette.crop();
        assert bottomFloorPalette != null;
        bottomFloorPalette.add(new MapChunk(RaidRoom.FLOOR_START_UPSTAIRS), ChunkDirection.WEST);
        bottomFloorPalette.add(new MapChunk(RaidRoom.GUARDIANS), ChunkDirection.WEST);
        bottomFloorPalette.add(new MapChunk(RaidRoom.VASA_NISTIRIO), ChunkDirection.WEST);
        bottomFloorPalette.add(new MapChunk(RaidRoom.SMALL_SCAVENGER_RUNT), ChunkDirection.SOUTH);
        bottomFloorPalette.add(new MapChunk(RaidRoom.DARK_ALTAR_ROOM), ChunkDirection.EAST);
        bottomFloorPalette.add(new MapChunk(RaidRoom.MUTTADILES), ChunkDirection.EAST);
        bottomFloorPalette.add(new MapChunk(RaidRoom.RESOURCES_B), ChunkDirection.EAST);
        bottomFloorPalette.add(new MapChunk(RaidRoom.FLOOR_END_DOWNSTAIRS), ChunkDirection.EAST);
        bottomFloorPalette.crop();
        calculateBoundaries();
    }

    /**
     * Generates a random raid floor set and calculates the boundaries around it.
     */
    void generate() {
        generation.generate();
        generatePath(generation.getTopFloor(), topFloorPalette, false);
        generatePath(generation.getBottomFloor(), middleFloorPalette, true);
        calculateBoundaries();
    }

    /**
     * Generates a path in the palette based on the array of rooms passed.
     *
     * @param rooms   the rooms in the raid.
     * @param palette the palette on which they'll be laid out.
     */
    private void generatePath(@NotNull final LayoutRoom[] rooms, @NotNull final MapPalette palette, final boolean forceLastRoom) {
        int index = 0;
        final LayoutRoom secondRoom = rooms[1];
        assert secondRoom != null;
        final Direction secondRoomEntryPos = secondRoom.getDirection();
        palette.setLastDirection(secondRoomEntryPos == Direction.WEST ? ChunkDirection.WEST : secondRoomEntryPos == Direction.NORTH ? ChunkDirection.NORTH : ChunkDirection.EAST);
        for (final LayoutRoom room : rooms) {
            final Direction dir = index + 1 >= rooms.length ? Direction.SOUTH : rooms[index + 1].getDirection();
            ChunkDirection direction = dir == Direction.NORTH ? ChunkDirection.NORTH : dir == Direction.SOUTH ? ChunkDirection.SOUTH : dir == Direction.WEST ? ChunkDirection.WEST : ChunkDirection.EAST;
            palette.add(new MapChunk(room), direction, forceLastRoom && index == rooms.length - 1);
            index++;
        }
        palette.crop();
    }

    /**
     * Calculates the boundaries between the two or three floors.
     */
    private void calculateBoundaries() {
        int minX = 0;
        int minY = 0;
        int maxX = 0;
        int maxY = 0;
        for (final MapPalette floor : Utils.concatenate(topFloorPalette, middleFloorPalette, bottomFloorPalette)) {
            if (floor == null) {
                continue;
            }
            for (final MapChunk room : floor) {
                assert room.x >= minX;
                assert room.y >= minY;
                if (room.x > maxX) {
                    maxX = room.x;
                }
                if (room.y > maxY) {
                    maxY = room.y;
                }
            }
        }
        boundaries = new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    public RoomGeneration getGeneration() {
        return generation;
    }
}
