package com.near_reality.cache

import com.near_reality.cache.TableEncoding.encodeToBuffer
import com.zenyte.CacheManager
import mgi.tools.jagcached.ArchiveType
import mgi.tools.jagcached.cache.File
import mgi.tools.jagcached.cache.Group

class DBTableIndexDefinitionGroup {
    var tableId: Int = 0
    var masterIndex = DBTableIndexDefinition()
    var columnIndexes = mutableListOf<DBTableIndexDefinition>()

    fun pack() {
        val group = Group()
        group.id = tableId
        group.addFile(masterIndex.toFile(true))
        for (columnIndexDefinition in columnIndexes) {
            group.addFile(columnIndexDefinition.toFile(false))
        }
        CacheManager.getCache().getArchive(ArchiveType.DBTABLEINDEX).addGroup(group)
    }
}

private fun DBTableIndexDefinition.toFile(isMaster: Boolean): File =
    if(isMaster) File(0, this.encodeToBuffer())
            else File(this.columnId + 1, this.encodeToBuffer())
