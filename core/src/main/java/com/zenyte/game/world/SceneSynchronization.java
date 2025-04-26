package com.zenyte.game.world;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.packet.out.*;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.flooritem.FloorItem;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.CharacterLoop;
import com.zenyte.game.world.region.Chunk;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntPredicate;

/**
 * @author Kris | 07/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SceneSynchronization {
    /**
     * The maximum number of chunks that are loaded in scene at once in a row.
     */
    private static final int SCENE_CHUNKS_DIAMETER = Player.SCENE_DIAMETER >> 3;
    /**
     * The number of chunks that around the player that are being synchronized.
     */
    private static final int CHUNK_SYNCHRONIZATION_RADIUS = 3;
    /**
     * The maximum number of chunks that can be synchronized by a single player at a time.
     */
    public static final int CHUNK_SYNCHRONIZATION_MAX_COUNT = (int) Math.pow((CHUNK_SYNCHRONIZATION_RADIUS << 1) + 1, 2);
    /**
     * The maximum distance how far from a player an event can be synchronized.
     */
    private static final int MAXIMUM_SYNCHRONIZATION_DISTANCE = (CHUNK_SYNCHRONIZATION_RADIUS + 1) << 3;

    /**
     * Iterates the surrounding chunks from the event and fires the event to all of the players who have currently synchronized said chunk, while keeping in mind the boundaries of the scene.
     * @param tile the tile upon which the event is happening.
     * @param synchronizablePacketFunction the function used to construct a new packet per-player basis.
     */
    public static final void forEach(@NotNull final Location tile, @NotNull final Function<Player, GamePacketEncoder> synchronizablePacketFunction) {
        final int tileX = tile.getX();
        final int tileY = tile.getY();
        final int chunkHashCode = Chunk.getChunkHash(tile.getX() >> 3, tile.getY() >> 3, tile.getPlane());
        CharacterLoop.forEach(tile, MAXIMUM_SYNCHRONIZATION_DISTANCE, Player.class, player -> {
            if (player.getChunksInScope().contains(chunkHashCode)) {
                player.sendZoneUpdate(tileX, tileY, synchronizablePacketFunction.apply(player));
            }
        });
    }

    /**
     * Iterates the surrounding chunks from the event and fires the event to all of the players who have currently synchronized said chunk, while keeping in mind the boundaries of the scene.
     * @param tile the tile upon which the event is happening.
     * @param consumer the consumer that accepts said player.
     */
    public static final void forEachFunctional(@NotNull final Location tile, @NotNull final Consumer<Player> consumer) {
        final int chunkHashCode = Chunk.getChunkHash(tile.getX() >> 3, tile.getY() >> 3, tile.getPlane());
        CharacterLoop.forEach(tile, MAXIMUM_SYNCHRONIZATION_DISTANCE, Player.class, player -> {
            if (player.getChunksInScope().contains(chunkHashCode)) {
                consumer.accept(player);
            }
        });
    }

    /**
     * Updates the scene scope of the player with new chunks and discards all of those which are now out of boundaries.
     * @param player the player whom to update.
     */
    public static final void updateScopeInScene(@NotNull final Player player) {
        final int baseChunk = player.getSceneBaseChunkId();
        final int baseX = baseChunk & 2047;
        final int baseY = baseChunk >> 11 & 2047;
        final int plane = player.getPlane();
        final Location tile = player.getLocation();
        final int tileX = tile.getChunkX();
        final int tileY = tile.getChunkY();
        final int startX = Math.max(tileX - CHUNK_SYNCHRONIZATION_RADIUS, baseX) - baseX;
        final int endX = Math.min(tileX + CHUNK_SYNCHRONIZATION_RADIUS, baseX + SCENE_CHUNKS_DIAMETER - 1) - baseX;
        final int startY = Math.max(tileY - CHUNK_SYNCHRONIZATION_RADIUS, baseY) - baseY;
        final int endY = Math.min(tileY + CHUNK_SYNCHRONIZATION_RADIUS, baseY + SCENE_CHUNKS_DIAMETER - 1) - baseY;
        final int baseGlobalX = baseX + startX;
        final int baseGlobalY = baseY + startY;
        final int maxGlobalX = baseX + endX;
        final int maxGlobalY = baseY + endY;
        final IntSet chunksInScope = player.getChunksInScope();
        //First lets remove all out-of-boundaries chunks.
        chunksInScope.removeIf((IntPredicate) chunk -> {
            final int chunkZ = chunk >> 22;
            if (chunkZ != plane) {
                return true;
            }
            final int chunkX = chunk & 2047;
            final int chunkY = chunk >> 11 & 2047;
            return chunkX < baseGlobalX || chunkX > maxGlobalX || chunkY < baseGlobalY || chunkY > maxGlobalY;
        });
        //Now let's fill all the new ones that just came into boundaries.
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                final int chunkX = baseX + x;
                final int chunkY = baseY + y;
                final Chunk chunk = World.getChunk(Chunk.getChunkHash(chunkX, chunkY, plane));
                if (!chunksInScope.add(chunk.getChunkId())) {
                    continue;
                }
                final Short2ObjectMap<WorldObject> spawnedObjects = chunk.getSpawnedObjects();
                final Short2ObjectMap<WorldObject> originalObjects = chunk.getOriginalObjects();
                final Set<FloorItem> floorItems = chunk.getFloorItems();
                player.send(new UpdateZoneFullFollows(x << 3, y << 3));
                if (!originalObjects.isEmpty()) {
                    final ObjectCollection<WorldObject> objects = originalObjects.values();
                    for (final WorldObject removedObject : objects) {
                        player.sendZoneUpdate(removedObject.getX(), removedObject.getY(), new LocDel(removedObject));
                    }
                }
                if (!spawnedObjects.isEmpty()) {
                    final ObjectCollection<WorldObject> objects = spawnedObjects.values();
                    for (final WorldObject spawnedObject : objects) {
                        player.sendZoneUpdate(spawnedObject.getX(), spawnedObject.getY(), new LocAdd(spawnedObject));
                    }
                }
                if (!floorItems.isEmpty()) {
                    for (final FloorItem item : floorItems) {
                        if (!item.isVisibleTo(player) || !player.isFloorItemDisplayed(item)) {
                            continue;
                        }
                        final Location location = item.getLocation();
                        player.sendZoneUpdate(location.getX(), location.getY(), new ObjAdd(item));
                    }
                }
            }
        }
    }

    /**
     * Refreshes the ground items to reflect on the {@link com.zenyte.game.world.entity.player.GameSetting#HIDE_ITEMS_YOU_CANT_PICK} option.
     * @param player the player whom to refresh.
     * @param add whether or not to add or remove the hidden elements.
     */
    public static void refreshScopedGroundItems(@NotNull final Player player, final boolean add) {
        final int baseChunk = player.getSceneBaseChunkId();
        final int baseX = baseChunk & 2047;
        final int baseY = baseChunk >> 11 & 2047;
        final int plane = player.getPlane();
        final Location tile = player.getLocation();
        final int tileX = tile.getChunkX();
        final int tileY = tile.getChunkY();
        final int startX = Math.max(tileX - CHUNK_SYNCHRONIZATION_RADIUS, baseX) - baseX;
        final int endX = Math.min(tileX + CHUNK_SYNCHRONIZATION_RADIUS, baseX + SCENE_CHUNKS_DIAMETER - 1) - baseX;
        final int startY = Math.max(tileY - CHUNK_SYNCHRONIZATION_RADIUS, baseY) - baseY;
        final int endY = Math.min(tileY + CHUNK_SYNCHRONIZATION_RADIUS, baseY + SCENE_CHUNKS_DIAMETER - 1) - baseY;
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                final int chunkX = baseX + x;
                final int chunkY = baseY + y;
                final Chunk chunk = World.getChunk(Chunk.getChunkHash(chunkX, chunkY, plane));
                final Set<FloorItem> floorItems = chunk.getFloorItems();
                if (!floorItems.isEmpty()) {
                    for (final FloorItem item : floorItems) {
                        if (!item.isVisibleTo(player) || !item.hasOwner() || (player.isIronman() && item.hasOwner() && item.isOwner(player))) {
                            continue;
                        }
                        final Location location = item.getLocation();
                        player.sendZoneUpdate(location.getX(), location.getY(), add ? new ObjAdd(item) : new ObjDel(item));
                    }
                }
            }
        }
    }
}
