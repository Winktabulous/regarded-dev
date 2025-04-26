package com.near_reality.plugins.area.ferox_enclave

import com.zenyte.game.util.Direction
import com.zenyte.game.world.entity.Location

internal enum class Barrier(val inXOrY: Int, val dir: Direction) {

    NORTH(inXOrY = 3639, dir = Direction.NORTH),
    SOUTH(inXOrY = 3617, dir = Direction.SOUTH),
    EAST(inXOrY = 3154, dir = Direction.EAST),
    WEST(inXOrY = 3123, dir = Direction.WEST);

    val outXOrY by lazy(LazyThreadSafetyMode.NONE) {
        when (dir) {
            Direction.EAST,
            Direction.WEST
            -> inXOrY + dir.offsetX
            Direction.NORTH,
            Direction.SOUTH
            -> inXOrY + dir.offsetY
            else -> error("Invalid direction $dir")
        }
    }

    fun goingOutside(position: Location) = when (this) {
        EAST, WEST -> position.x == inXOrY
        NORTH, SOUTH -> position.y == inXOrY
    }

    companion object {

        fun forPosition(position: Location) = values().find {
            when (it) {
                EAST, WEST -> position.x == it.inXOrY || position.x == it.outXOrY
                NORTH, SOUTH -> position.y == it.inXOrY || position.y == it.outXOrY
            }
        }
    }
}

