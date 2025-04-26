package com.near_reality.cache_tool.packing.custom.mack

import com.near_reality.cache_tool.packing.Asset
import mgi.tools.jagcached.ArchiveType
import mgi.tools.jagcached.GroupType
import mgi.tools.jagcached.cache.Cache
import mgi.tools.jagcached.cache.File
import mgi.tools.jagcached.cache.Group
import mgi.utilities.ByteBuffer

abstract class ClownInterfacePacker(protected val cache: Cache) {

    abstract fun pack()

    internal fun Asset.packAsFileInGroup(archive: ArchiveType, group: GroupType, fileId: Int) =
        packAsFileInGroup(archive, group.id, fileId)

    internal fun Asset.packAsFileInGroup(archive: ArchiveType, groupId: Int, fileId: Int) =
        cache
            .getArchive(archive).let {
                it.findGroupByID(groupId) ?: Group(groupId).apply(it::addGroup)
            }
            .addFile(File(fileId, ByteBuffer(bytes)))
}
