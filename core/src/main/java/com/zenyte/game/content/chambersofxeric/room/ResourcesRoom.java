package com.zenyte.game.content.chambersofxeric.room;

import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.chambersofxeric.RoomController;
import com.zenyte.game.content.chambersofxeric.map.RaidArea;
import com.zenyte.game.content.chambersofxeric.map.RaidRoom;
import com.zenyte.game.content.chambersofxeric.npc.Bat;
import com.zenyte.game.content.chambersofxeric.npc.Bat.BatType;
import com.zenyte.game.content.chambersofxeric.npc.CaveSnake;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;
import com.zenyte.utils.IntArray;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.Arrays;

/**
 * @author Kris | 16. nov 2017 : 2:36.50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class ResourcesRoom extends RaidArea implements RoomController, CycleProcessPlugin {
    /**
     * The locations where the bats spawn. There may only be up to three bats per room.
     */
    private static final Location[][] bats = new Location[][] {new Location[] {new Location(3282, 5463, 1), new Location(3288, 5461, 1), new Location(3285, 5457, 1)}, new Location[] {new Location(3308, 5455, 1), new Location(3301, 5458, 1), new Location(3305, 5462, 1)}, new Location[] {new Location(3348, 5455, 1), new Location(3344, 5459, 1), new Location(3343, 5464, 1)}};
    /**
     * The locations where the fish objects are.
     */
    private static final Location[][] fishObjects = new Location[][] {new Location[] {new Location(3274, 5457, 0), new Location(3280, 5451, 0), new Location(3281, 5454, 0)}, new Location[] {new Location(3302, 5459, 0), new Location(3308, 5449, 0), new Location(3306, 5461, 0)}, new Location[] {new Location(3337, 5453, 0), new Location(3345, 5445, 0), new Location(3343, 5461, 0)}};
    /**
     * The tiles where the snakes spawn, or rather the central positions of them.
     */
    private static final Location[][] snakeCenterTiles = new Location[][] {new Location[] {new Location(3275, 5457, 0), new Location(3279, 5451, 0), new Location(3280, 5454, 0)}, new Location[] {new Location(3302, 5458, 0), new Location(3309, 5449, 0), new Location(3306, 5460, 0)}, new Location[] {new Location(3338, 5453, 0), new Location(3345, 5446, 0), new Location(3343, 5460, 0)}};
    /**
     * The coordinates to the static storage units found in the A type room.
     */
    private static final Location[] resourcesAStorageUnits = new Location[] {new Location(3275, 5465, 0), new Location(3312, 5463, 0), new Location(3335, 5458, 0)};
    /**
     * The coordinates to the static storage units found in the B type room.
     */
    private static final Location[] resourcesBStorageUnits = new Location[] {new Location(3276, 5465, 1), new Location(3317, 5456, 1), new Location(3342, 5456, 1)};
    /**
     * The location where the cave snake is currently positioned.
     */
    private Location fishingSpotPosition;
    /**
     * The center tile of the cave snake.
     */
    private Location caveSnakeCenter;
    /**
     * The index of the fishing spot element in arrays.
     */
    private int elementIndex;
    /**
     * A mutable integer containing the number of attempts players have done to fish from the spot.
     */
    private final MutableInt attempts = new MutableInt();
    /**
     * WHether or not a snake is currently available.
     */
    private boolean containsSnake;
    /**
     * The number of ticks the room has been active for, resetting at every 100 ticks to 0. Used for spawning the snake, as it only lasts for a minute at a time.
     */
    private int ticks;

    public ResourcesRoom(final RaidRoom type, final Raid raid, final int rotation, final int size, final int regionX, final int regionY, final int chunkX, final int chunkY, final int fromPlane, final int toPlane) {
        super(type, raid, rotation, size, regionX, regionY, chunkX, chunkY, fromPlane, toPlane);
    }

    @Override
    public Location[] getStorageUnitLocations() {
        return getType() == RaidRoom.RESOURCES_A ? resourcesAStorageUnits : resourcesBStorageUnits;
    }

    @Override
    public String name() {
        return "Chambers of Xeric: Resource room";
    }

    @Override
    public void loadRoom() {
        if (getType() == RaidRoom.RESOURCES_B) {
            final int level = getHighestLevel(SkillConstants.HUNTER);
            final ObjectArrayList<BatType> bats = new ObjectArrayList<>();
            final BatType bestBat = getBestBat(level);
            for (int i = bestBat.ordinal(); i >= 0; i--) {
                bats.add(BatType.values[i]);
                if (bats.size() >= 3) {
                    break;
                }
            }
            final ObjectArrayList<Location> locations = new ObjectArrayList<>(Arrays.asList(ResourcesRoom.bats[index]));
            final int count = Math.min(6, 2 + (raid.getOriginalPlayers().size() >> 3));
            final BatType type = bats.get(Utils.random(bats.size() - 1));
            for (int i = 0; i < count; i++) {
                try {
                    final Bat bat = new Bat(raid, this, type.getId(), getLocation(locations.get(Utils.random(locations.size() - 1))));
                    bat.spawn();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            elementIndex = Utils.random(2);
            this.fishingSpotPosition = getLocation(fishObjects[index][elementIndex]);
            this.caveSnakeCenter = getLocation(snakeCenterTiles[index][elementIndex]);
        }
    }

    /**
     * Gets the best bat type available for the level provided.
     *
     * @param level the level for which we iterate the bats.
     * @return the best bat type available for the level.
     */
    private final BatType getBestBat(final int level) {
        BatType bat = null;
        for (final BatType batType : BatType.values) {
            if (batType.getLevel() > level) {
                break;
            }
            bat = batType;
        }
        assert bat != null;
        return bat;
    }

    public final void riseSnake() {
        if (containsSnake) {
            return;
        }
        attempts.increment();
        containsSnake = true;
        final CaveSnake snake = new CaveSnake(raid, this, caveSnakeCenter.transform(-1, -1, 0), fishingSpotPosition);
        snake.setFaceLocation(fishingSpotPosition);
        snake.executeSequence(() -> containsSnake = false);
    }

    @Override
    public void process() {
        if (getType() == RaidRoom.RESOURCES_B) {
            return;
        }
        if (++ticks >= 50 || attempts.intValue() >= 10) {
            ticks = 0;
            attempts.setValue(0);
            final IntArrayList list = new IntArrayList(IntArray.of(0, 1, 2));
            list.rem(elementIndex);
            elementIndex = list.getInt(Utils.random(list.size() - 1));
            this.fishingSpotPosition = getLocation(fishObjects[index][elementIndex]);
            this.caveSnakeCenter = getLocation(snakeCenterTiles[index][elementIndex]);
        }
    }

    public Location getFishingSpotPosition() {
        return fishingSpotPosition;
    }

    public Location getCaveSnakeCenter() {
        return caveSnakeCenter;
    }

    public MutableInt getAttempts() {
        return attempts;
    }
}
