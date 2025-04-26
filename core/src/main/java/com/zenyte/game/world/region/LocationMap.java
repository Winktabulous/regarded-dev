package com.zenyte.game.world.region;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.NetworkConstants;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;

import java.util.LinkedList;
import java.util.function.Predicate;

/**
 * @author Kris | 09/11/2018 14:27
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LocationMap {
    private static final LinkedList<ObjectLinkedOpenHashSet<Player>> emptySets = new LinkedList<>();
    private static final Int2ObjectOpenHashMap<ObjectLinkedOpenHashSet<Player>> tiles = new Int2ObjectOpenHashMap<>(NetworkConstants.PLAYER_CAP);

    static {
        for (int i = 0; i < NetworkConstants.PLAYER_CAP; i++) {
            emptySets.add(new ObjectLinkedOpenHashSet<>());
        }
    }

    public static void add(final Player player) {
        final int bitpacked = player.getLocation().getPositionHash();
        ObjectLinkedOpenHashSet<Player> set = tiles.get(bitpacked);
        if (set == null) {
            set = emptySets.poll();
            tiles.put(bitpacked, set);
        }
        assert set != null;
        set.add(player);
    }

    public static void remove(final Player player) {
        final int bitpacked = player.getLocation().getPositionHash();
        ObjectLinkedOpenHashSet<Player> set = tiles.get(bitpacked);
        if (set == null) return;
        set.remove(player);
        if (set.isEmpty()) {
            emptySets.add(set);
            tiles.remove(bitpacked);
        }
    }

    public static void iterate(final Player player, Predicate<ObjectLinkedOpenHashSet<Player>> consumer) {
        final Location location = player.getLocation();
        final int px = location.getX();
        final int py = location.getY();
        final int plane = location.getPlane();
        final ObjectLinkedOpenHashSet<Player> set = tiles.get(location.getPositionHash());
        if (set != null) {
            if (!consumer.test(set)) return;
        }
        int radius = 0;
        while (++radius <= 15) {
            for (int y = py - radius + 1; y < py + radius; y++) {
                if (!process(px - radius, y, plane, consumer)) return;
            }
            for (int x = px - radius; x < px + radius; x++) {
                if (!process(x, py + radius, plane, consumer)) return;
            }
            for (int y = py + radius; y >= py - radius; y--) {
                if (!process(px + radius, y, plane, consumer)) return;
            }
            for (int x = px + radius - 1; x >= px - radius; x--) {
                if (!process(x, py - radius, plane, consumer)) return;
            }
        }
    }

    private static boolean process(final int x, final int y, final int plane, Predicate<ObjectLinkedOpenHashSet<Player>> consumer) {
        final ObjectLinkedOpenHashSet<Player> set = tiles.get(Location.getHash(x, y, plane));
        if (set != null) {
            return consumer.test(set);
        }
        return true;
    }
}
