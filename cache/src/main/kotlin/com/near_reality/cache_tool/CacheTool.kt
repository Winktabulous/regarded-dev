package com.near_reality.cache_tool

import mgi.tools.jagcached.cache.Cache

/**
 * @author Jire
 */

val cacheFrom: Cache by lazy { Cache.openCache("data/cache-211") }

val cacheTo: Cache by lazy { Cache.openCache("data/cache") }

