package com.near_reality.cache_tool.packing.custom

import com.near_reality.cache_tool.packing.assetsBase
import com.zenyte.CacheManager
import mgi.tools.parser.TypeParser

/**
 * Packs custom map files of near-reality.
 *
 * @author Stan van der Bend
 */
object NearRealityCustomMapsPacker {
    @JvmStatic
    fun pack() {
        assetsBase("assets/osnr/custom_maps/") {
            fun packMap(id: Int, prefix: String, old: Boolean = true) {
                val landScapeData = assetBytes("${prefix}_landscape.dat")
                val objData = assetBytes("${prefix}_objects.dat")
                if (old) {
                    TypeParser.packMapRawPre209(
                        CacheManager.getCache(),
                        id,
                        objData,
                        landScapeData
                    )
                } else {
                    TypeParser.packMap(
                        CacheManager.getCache(),
                        id,
                        landScapeData,
                        objData,
                    )
                }
            }
            packMap(13420, "gamble")    // flower poker area
            packMap(12145, "mm")        // middle man area
            packMap(13909, "barrows")
            packMap(6729, "pvm_arena", old = false) // Not really custom but converted for post rev 209
        }
    }
}
