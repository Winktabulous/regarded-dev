package com.near_reality.cache_tool.packing.custom

import com.near_reality.cache_tool.packing.assetsBase
import com.zenyte.CacheManager
import mgi.custom.AnimationBase
import mgi.custom.FramePacker
import mgi.custom.easter.EasterMapPacker
import mgi.tools.jagcached.ArchiveType
import mgi.tools.jagcached.cache.File
import mgi.tools.jagcached.cache.Group
import mgi.tools.parser.TypeParser
import mgi.types.config.AnimationDefinitions
import mgi.types.config.ObjectDefinitions
import mgi.types.config.items.ItemDefinitions
import mgi.types.config.npcs.NPCDefinitions

object NearRealityEaster2024Packer {


    /**
     * Custom items are packed through the standard custom item packer
     */
    @JvmStatic
    fun pack() {
        assetsBase("assets/osnr/easter_2024/map/") {
            TypeParser.packMap(
                CacheManager.getCache(),
                7504,
                assetBytes("3774.dat"),
                assetBytes("3775.dat"),
            )
        }
        // For bunny npc
        EasterMapPacker().packAll()
        assetsBase("assets/osnr/easter_2024/") {
            "items" {
                ItemDefinitions(id, mgiBuffer).pack()
            }
            "npcs" {
                NPCDefinitions(id, mgiBuffer).pack()
            }
            "objects" {
                ObjectDefinitions(id, mgiBuffer).pack()
            }
            "animations" {
                AnimationDefinitions(id, mgiBuffer).pack()
            }
            "models" {
                CacheManager.getCache().getArchive(ArchiveType.MODELS).addGroup(Group(id, File(mgiBuffer)))
            }
            "bases" {
                val groupId = file.nameWithoutExtension.toInt()
                val data = file.resolve("0.dat").readBytes()
                AnimationBase.pack(groupId, data)
            }
            FramePacker.reset()
            "frames" {
                val groupId = file.nameWithoutExtension.toInt()
                file.listFiles()!!.sorted().forEach {
                    val fileId = it.nameWithoutExtension.toInt()
                    val data = it.readBytes()
                    FramePacker.add((groupId shl 16) or fileId,  data)
                }
            }
            FramePacker.write()
        }
    }
}
