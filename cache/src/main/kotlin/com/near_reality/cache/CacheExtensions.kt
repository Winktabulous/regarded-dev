package com.near_reality.cache

import mgi.tools.jagcached.ArchiveType
import mgi.tools.jagcached.GroupType
import mgi.tools.jagcached.cache.Archive
import mgi.tools.jagcached.cache.Cache
import mgi.tools.jagcached.cache.Group

val Cache.configs get() = getArchive(ArchiveType.CONFIGS)
val Cache.sprites get() = getArchive(ArchiveType.SPRITES)

val Archive.hitmarks get() = findGroupByID(GroupType.HITMARK)

fun Archive.group(id: Int) = requireNotNull(findGroupByID(id)) {"Dit not find group with id $id"}
fun Archive.file(id: Int) = group(id).file(0)

fun Group.file(id: Int) = findFileByID(id)
