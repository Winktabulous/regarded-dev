package com.near_reality.cache_tool.dumping

import net.runelite.cache.Cache

/**
 * @author William Fuhrman | 7/27/2022 4:01 PM
 * @since 05/07/2022
 */
object SpriteDumper {

    @JvmStatic
    fun main(args: Array<String>) {
        Cache.main(arrayOf("-c", "cache/data/cache", "-sprites", "/Users/stanvanderbend/IdeaProjects/near-reality/server-production/cache/dump/sprites"))
    }

}
