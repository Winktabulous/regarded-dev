package com.zenyte.game.world.flooritem;

import com.zenyte.game.item.Item;
import com.zenyte.game.packet.out.ObjAdd;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.Chunk;
import com.zenyte.game.world.region.Region;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Kris | 30. mai 2018 : 01:44:39
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class GlobalItem extends FloorItem {
    private static final Logger log = LoggerFactory.getLogger(GlobalItem.class);
    private static final Int2ObjectMap<Set<GlobalItem>> globalItems = new Int2ObjectOpenHashMap<>();
    private static final Queue<GlobalItem> pendingGlobalItems = new ConcurrentLinkedQueue<>();
    private int respawnTime;
    private int originalAmount;
    private transient int ticks;

    public GlobalItem(final Item item, final Location location, final int respawnTime) {
        super(item, location, null, null, -1, -1);
        this.respawnTime = respawnTime;
        originalAmount = item.getAmount();
    }

    public static void createPersistentGlobalItemSpawn(final GlobalItem item) {
        final int regionId = item.getLocation().getRegionId();
        if (!globalItems.containsKey(regionId)) {
            globalItems.put(regionId, new ObjectOpenHashSet<>());
        }
        final Set<GlobalItem> set = globalItems.get(regionId);
        set.add(item);
    }

    public static Set<GlobalItem> getGlobalItems(final int regionId) {
        return globalItems.get(regionId);
    }

    public static void load() {
        WorldTasksManager.schedule(() -> {
            try {
                if (pendingGlobalItems.isEmpty()) {
                    return;
                }
                final Iterator<GlobalItem> iterator = pendingGlobalItems.iterator();
                while (iterator.hasNext()) {
                    try {
                        final GlobalItem item = iterator.next();
                        if (--item.ticks == 0) {
                            item.spawn();
                            iterator.remove();
                        }
                    } catch (Exception e) {
                        log.error("", e);
                    }
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }, 0, 0);
    }

    public void schedule() {
        ticks = respawnTime;
        setAmount(originalAmount);
        pendingGlobalItems.add(this);
    }

    public void spawn() {
        final int regionId = location.getRegionId();
        final Region region = World.getRegion(regionId);
        if (region == null) {
            return;
        }
        final int chunkId = Chunk.getChunkHash(location.getX() >> 3, location.getY() >> 3, location.getPlane());
        final Chunk chunk = World.getChunk(chunkId);
        chunk.addFloorItem(this);
        for (final Player player : World.getPlayers()) {
            if (player == null || !player.getMapRegionsIds().contains(regionId)) {
                continue;
            }
            player.sendZoneUpdate(location.getX(), location.getY(), new ObjAdd(this));
        }
    }

    public int getRespawnTime() {
        return respawnTime;
    }

    public void setRespawnTime(int respawnTime) {
        this.respawnTime = respawnTime;
    }

    public int getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(int originalAmount) {
        this.originalAmount = originalAmount;
    }

}
