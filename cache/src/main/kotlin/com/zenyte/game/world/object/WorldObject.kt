package com.zenyte.game.world.`object`

import com.zenyte.game.util.Direction
import com.zenyte.game.world.entity.InteractableEntity
import com.zenyte.game.world.entity.Location
import mgi.types.config.ObjectDefinitions

/**
 * @author Jire
 */
open class WorldObject private constructor(
    x: Int, y: Int, plane: Int,
    private var objectKey: Int
) : Location(x, y, plane),
    InteractableEntity {

    constructor(id: Int, type: Int = DEFAULT_TYPE, rotation: Int = DEFAULT_ROTATION, x: Int, y: Int, plane: Int)
            : this(x, y, plane, objectKey(id, type, rotation))

    constructor(id: Int, type: Int = DEFAULT_TYPE, rotation: Int = DEFAULT_ROTATION, tile: Location)
            : this(tile.x, tile.y, tile.plane, objectKey(id, type, rotation))

    constructor(from: WorldObject) : this(from.x, from.y, from.plane, from.objectKey)

    var id: Int
        get() = id(objectKey)
        set(id) {
            objectKey = objectKey(id, type, rotation, isLocked)
        }
    var type: Int
        get() = type(objectKey)
        set(type) {
            objectKey = objectKey(id, type, rotation, isLocked)
        }
    var rotation: Int
        get() = rotation(objectKey)
        set(rotation) {
            objectKey = objectKey(id, type, rotation, isLocked)
        }

    val definitions: ObjectDefinitions? get() = ObjectDefinitions.get(id)
    val name: String? get() = definitions?.name

    val faceDirection: Direction
        get() = when (rotation) {
            0 -> Direction.WEST
            1 -> Direction.NORTH
            2 -> Direction.EAST
            else -> Direction.SOUTH
        }

    var isLocked: Boolean
        get() = locked(objectKey)
        set(isLocked) {
            objectKey = objectKey(id, type, rotation, isLocked)
        }

    override val location get() = this
    override val width get() = (if ((rotation and 1) == 1) definitions?.sizeY else definitions?.sizeX) ?: 1
    override val length get() = (if ((rotation and 1) == 1) definitions?.sizeX else definitions?.sizeY) ?: 1

    fun transform(newID: Int) = WorldObject(this).apply { id = newID }

    override fun toString() =
        "$name: $id, $type, $rotation, $isLocked" +
                "\nTile: $x, $y, $plane, region[$regionId, $regionX, $regionY], chunk[$chunkX, $chunkY], hash [$positionHash]"

    override fun equals(other: Any?) = other is WorldObject
            && other.objectKey == objectKey
            && other.positionHash == positionHash

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + objectKey
        return result
    }

    companion object {

        const val DEFAULT_TYPE = 10
        const val DEFAULT_ROTATION = 0

        @JvmStatic
        @JvmOverloads
        fun objectKey(id: Int, type: Int, rotation: Int, locked: Boolean = false) =
            (id and 0xFF_FF_FF) or
                    ((type and 0b1_1111) shl 24) or
                    ((rotation and 0b11) shl 29) or
                    ((if (locked) 1 else 0) shl 31)

        @JvmStatic
        fun id(objectKey: Int) = objectKey and 0xFF_FF_FF

        @JvmStatic
        fun type(objectKey: Int) = (objectKey ushr 24) and 0b1_1111

        @JvmStatic
        fun rotation(objectKey: Int) = (objectKey ushr 29) and 0b11

        @JvmStatic
        fun locked(objectKey: Int) = ((objectKey ushr 31) and 1) == 1

    }

}