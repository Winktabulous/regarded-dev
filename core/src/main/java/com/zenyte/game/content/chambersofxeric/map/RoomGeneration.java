package com.zenyte.game.content.chambersofxeric.map;

import com.google.common.collect.ImmutableList;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.zenyte.game.content.chambersofxeric.map.RaidRoom.*;
import static com.zenyte.game.content.chambersofxeric.map.RoomType.*;

/**
 * @author Kris | 8. mai 2018 : 14:47:23
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class RoomGeneration {
    /**
     * An immutable list of farming rooms.
     */
    private static final ImmutableList<RaidRoom> FARMING_ROOMS = ImmutableList.of(RESOURCES_A, RESOURCES_B);
    /**
     * An immutable list of scavenger rooms.
     */
    private static final ImmutableList<RaidRoom> SCAVENGER_ROOMS = ImmutableList.of(SMALL_SCAVENGER_RUNT, LARGE_SCAVENGER_BEAST);
    /**
     * An immutable list of puzzle rooms.
     */
    public static final ImmutableList<RaidRoom> PUZZLE_ROOMS = ImmutableList.of(ICE_DEMON, CREATURE_KEEPER, DEATHLY_ROOM, CRAB_PUZZLE);
    /**
     * A randomly picked boss pattern; the rooms will appear in the order given here.
     */
    private final BossPattern bossPattern;
    /**
     * The pattern in which the raid will flow. Determines the types of rooms and their order.
     */
    private final RaidPattern raidPattern;
    /**
     * An array containing the top floor rooms.
     */
    private final LayoutRoom[] topFloor;
    /**
     * An array containing the bottom floor rooms.
     */
    private final LayoutRoom[] bottomFloor;
    /**
     * The length of the raid - total number of rooms in both floors.
     */
    private final int length;
    /**
     * The length of one floor.
     */
    private final int floorLength;

    RoomGeneration(@NotNull final String partyOwner) {
        //Generating a random boss pattern from the available patterns.
        bossPattern = Utils.getRandomElement(BossPattern.getValues());
        //Generating a random layout pattern.
        raidPattern = RaidPattern.random(partyOwner);
        //Number of rooms in the pattern
        length = raidPattern.getPattern().size();
        //A single floor's length is all rooms combined split.
        floorLength = length / 2;
        topFloor = new LayoutRoom[floorLength];
        bottomFloor = new LayoutRoom[floorLength];
    }

    /**
     * Populates the top and bottom floors with random rooms, following a specific algorithm.
     */
    public void generate() {
        /* A list of boss combat rooms. */
        final ArrayList<RaidRoom> combatRooms = new ArrayList<RaidRoom>(bossPattern.random(raidPattern.getCombatRooms()));
        /* A list of scavenger rooms. */
        final ArrayList<RaidRoom> scavengerRooms = new ArrayList<RaidRoom>(SCAVENGER_ROOMS);
        /* A list of farming/skilling rooms. */
        final ArrayList<RaidRoom> farmingRooms = new ArrayList<RaidRoom>(FARMING_ROOMS);
        /* A list of puzzle rooms, consisting of ice demon, creature keeper, deathly room and crab room. */
        final ArrayList<RaidRoom> puzzleRooms = new ArrayList<RaidRoom>(PUZZLE_ROOMS);
        /*
         * We remove two puzzle rooms from the collection of four. This is because two of the puzzle rooms will always be different and cannot be duplicates, however in the pattern
         * with three puzzle rooms, it is possible for one of the puzzles to be duplicate of the existing ones, so therefore we force the code to generate a random puzzle out of the
         * four - regardless if it is already used or not.
         */
        for (int i = 0; i < 2; i++) {
            puzzleRooms.remove(Utils.random(puzzleRooms.size() - 1));
        }
        /* An array representing an order of room types, in which the rooms must appear. */
        final List<LayoutTypeRoom> pattern = raidPattern.getPattern();
        int count = 0;
        /* Fill the remaining rooms in with random possible options. */
        for (final LayoutTypeRoom room : pattern) {
            final RoomType type = room.getType();
            final Direction direction = room.getDirection();
            count++;
            if (type == START) {
                setNext(new LayoutRoom(count == 1 ? RAID_START : FLOOR_START_UPSTAIRS, direction));
                continue;
            }
            if (type == END) {
                setNext(new LayoutRoom(FLOOR_END_DOWNSTAIRS, direction));
                continue;
            }
            if (type == COMBAT) {
                setNext(new LayoutRoom(combatRooms.remove(0), direction));
                continue;
            }
            final ArrayList<RaidRoom> rooms = type == SCAVENGERS ? scavengerRooms : type == FARMING ? farmingRooms : puzzleRooms;
            if (!rooms.isEmpty()) {
                setNext(new LayoutRoom(rooms.remove(Utils.random(rooms.size() - 1)), direction));
            } else {
                setNext(new LayoutRoom(Utils.getRandomCollectionElement(type == SCAVENGERS ? SCAVENGER_ROOMS : type == FARMING ? FARMING_ROOMS : PUZZLE_ROOMS), direction));
            }
        }
    }

    /**
     * Sets a specific slot in the array to the room in parameters, if applicable. If the slot is already taken, which should not under any
     * circumstances happen, it'll throw a RuntimeException.
     *
     * @param index the index which to set. Values go from 0-12, automatically picks the respective floor.
     * @param room  the room to set the given slot to.
     */
    private void set(final int index, final LayoutRoom room) {
        if (index >= floorLength) {
            assert bottomFloor[index - floorLength] == null : "Attempted to override an existing room on bottom floor!";
            bottomFloor[index - floorLength] = room;
        } else {
            assert topFloor[index] == null : "Attempted to override an existing room on top floor!";
            topFloor[index] = room;
        }
    }

    /**
     * Sets the next empty slot in the floors into the room in parameters.
     *
     * @param room the room to set the next slot as.
     */
    private void setNext(final LayoutRoom room) {
        for (int i = 0; i < length; i++) {
            final boolean bottom = i >= floorLength;
            final LayoutRoom[] floor = bottom ? bottomFloor : topFloor;
            if (floor[i - (bottom ? floorLength : 0)] == null) {
                set(i, room);
                return;
            }
        }
    }

    public RaidPattern getRaidPattern() {
        return raidPattern;
    }

    public LayoutRoom[] getTopFloor() {
        return topFloor;
    }

    public LayoutRoom[] getBottomFloor() {
        return bottomFloor;
    }

    public int getFloorLength() {
        return floorLength;
    }
}
