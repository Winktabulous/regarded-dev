package com.zenyte.game;

import com.near_reality.game.util.HuffmanManager;
import com.near_reality.network.js5.JS5Responses;
import com.zenyte.CacheManager;
import com.zenyte.game.packet.ClientProtDecoder;
import com.zenyte.game.packet.ClientProtLoader;
import com.zenyte.game.util.ZenyteHuffman;
import mgi.tools.jagcached.cache.Cache;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

/**
 * @author Tommeh | 28 jul. 2018 | 13:03:30
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class GameLoader {

    private static final ClientProtDecoder<?>[] decoders = new ClientProtDecoder[256];

    public static ClientProtDecoder<?>[] getDecoders() {
        return decoders;
    }

    public static void load() {
        load(ForkJoinPool.commonPool());
    }

    public static void load(ExecutorService pool) {
        Cache cache = CacheManager.loadCacheFiles("./cache/data/cache/", true);
        HuffmanManager.load(cache);
        ZenyteHuffman.load(cache);
        JS5Responses.preload(cache);

        ClientProtLoader.load();
        CacheManager.loadDefinitions(pool, false);
    }
}
