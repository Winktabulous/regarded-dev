package com.near_reality.api.dao

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.id.EntityID

abstract class ModelEntity<T>(id: EntityID<Long>) : LongEntity(id) {
    abstract fun toModel(): T
}
