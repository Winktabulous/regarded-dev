package com.near_reality.cache_tool.packing.custom.mack

import com.near_reality.cache_tool.packing.assetsBase
import mgi.tools.jagcached.ArchiveType
import mgi.tools.jagcached.cache.Cache

class ClownVoteInterfacePacker(cache: Cache) : ClownInterfacePacker(cache) {

    companion object {
        const val WIDGET_VOTE_ID = 5100
    }

    override fun pack() {
        assetsBase("assets/osnr/vote_interface/") {
            "cs2" {
                val groupId = name.substringBefore('_').toInt()
                packAsFileInGroup(ArchiveType.CLIENTSCRIPTS, groupId, fileId = 0)
            }
            "if3" {
                val fileId = name.substringBefore('.').substringAfter('_').toInt()
                packAsFileInGroup(ArchiveType.INTERFACES, WIDGET_VOTE_ID, fileId)
            }
        }
    }
}
