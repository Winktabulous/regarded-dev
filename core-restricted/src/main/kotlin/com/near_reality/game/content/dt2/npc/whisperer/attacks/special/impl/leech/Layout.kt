package com.near_reality.game.content.dt2.npc.whisperer.attacks.special.impl.leech

import com.near_reality.game.content.dt2.npc.whisperer.WhispererCombat
import com.near_reality.game.content.offset
import com.zenyte.game.world.entity.Location

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-09-25
 */
object Layout {

    fun generateSafePath(
        whisperer: WhispererCombat,
        primaryPath: Boolean,
        layout: Array<Pair<Int, Int>>
    ): ArrayList<Location> {

        val safePath = arrayListOf<Location>()
        if (layout.size == layoutOptionOne.size) {
            // Layout One
            if (primaryPath)
                primaryPathLayoutOne.map { safePath.add(whisperer.middleLocation offset it) }
            else
                secondaryPathLayoutOne.map { safePath.add(whisperer.middleLocation offset it) }
        }
        else {
            // Layout Two
            if (primaryPath)
                primaryPathLayoutTwo.map { safePath.add(whisperer.middleLocation offset it) }
            else
                secondaryPathLayoutTwo.map { safePath.add(whisperer.middleLocation offset it) }
        }
        return safePath
    }

    val layoutOptionOne = arrayOf(
        // Left side
        -4 to -1,
        -4 to 0,
        -4 to 1,
        -3 to -2,
        -3 to -1,
        -3 to 0,
        -3 to 1,
        -3 to 2,
        -2 to -1,
        -2 to 1,
        -2 to -3,
        -2 to 3,
        // Right side
        4 to -1,
        4 to 0,
        4 to 1,
        3 to -2,
        3 to -1,
        3 to 0,
        3 to 1,
        3 to 2,
        2 to -1,
        2 to 1,
        2 to -3,
        2 to 3
    )

    val layoutOptionTwo = arrayOf(
        // Left side
        -6 to 0,
        -5 to -2,
        -5 to 0,
        -5 to 2,
        -4 to 0,
        -3 to -2,
        -3 to 0,
        -3 to 2,
        -1 to -6,
        -1 to -4,
        -1 to 4,
        -1 to 6,
        // center
        0 to -6,
        0 to -4,
        0 to 4,
        0 to 6,
        // Right side
        6 to 0,
        5 to -2,
        5 to 0,
        5 to 2,
        4 to 0,
        3 to -2,
        3 to 0,
        3 to 2,
        1 to -6,
        1 to -4,
        1 to 4,
        1 to 6
    )


    val primaryPathLayoutOne = arrayOf(
        // Left Side
        -4 to -1,
        -4 to 1,
        -2 to -3,
        -2 to -1,
        -2 to 1,
        -2 to 3,
        // Right side
        4 to -1,
        4 to 1,
        2 to -3,
        2 to -1,
        2 to 1,
        2 to 3
    )

    val secondaryPathLayoutOne = arrayOf(
        // Left Side
        -4 to -1,
        -4 to 1,
        -3 to -2,
        -3 to 2,
        -2 to -3,
        -2 to -1,
        -2 to 1,
        -2 to 3,
        // Right side
        4 to -1,
        4 to 1,
        3 to -2,
        3 to 2,
        2 to -3,
        2 to -1,
        2 to 1,
        2 to 3
    )


    private val primaryPathLayoutTwo = arrayOf(
        // Left side
        -5 to -2,
        -5 to 0,
        -5 to 2,
        -3 to -2,
        -3 to 2,
        -1 to -4,
        -1 to 4,
        // Right side
        5 to -2,
        5 to 0,
        5 to 2,
        3 to -2,
        3 to 2,
        1 to -4,
        1 to 4,
    )

    private val secondaryPathLayoutTwo = arrayOf(
        // Left side
        -5 to 0,
        -3 to -2,
        -3 to 2,
        -1 to -6,
        -1 to -4,
        -1 to 4,
        -1 to 6,
        // Right side
        5 to 0,
        3 to -2,
        3 to 2,
        1 to -6,
        1 to -4,
        1 to 4,
        1 to 6,
    )
}