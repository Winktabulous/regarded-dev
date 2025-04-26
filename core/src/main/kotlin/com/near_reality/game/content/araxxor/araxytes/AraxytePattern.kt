package com.near_reality.game.content.araxxor.araxytes

import com.near_reality.game.content.araxxor.AraxxorInstance
import com.near_reality.game.content.araxxor.araxytes.impl.AcidicAraxyte
import com.near_reality.game.content.araxxor.araxytes.impl.MirrorbackAraxyte
import com.near_reality.game.content.araxxor.araxytes.impl.RupturaAraxyte
import com.zenyte.game.world.entity.Location
import kotlin.random.Random

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-21
 */
class AraxytePattern(val instance: AraxxorInstance, val random: Int = Random.nextInt(3)) {

    private var eggLocations: ArrayList<Location> = arrayListOf(
        instance.getLocation(3643, 9806),
        instance.getLocation(3637, 9803),
        instance.getLocation(3631, 9803),

        instance.getLocation(3626, 9807),
        instance.getLocation(3621, 9813),
        instance.getLocation(3621, 9822),

        instance.getLocation(3631, 9826),
        instance.getLocation(3637, 9826),
        instance.getLocation(3644, 9823)
    )

    fun getRandomPattern(): ArrayList<Araxyte> =
        when (random) {
            0 -> getPatternA()
            1 -> getPatternB()
            else -> getPatternC()
        }

    private fun getPatternA(): ArrayList<Araxyte> =
        arrayListOf(
            AcidicAraxyte(instance, eggLocations[0]),
            MirrorbackAraxyte(instance, eggLocations[1]),
            RupturaAraxyte(instance, eggLocations[2]),
            AcidicAraxyte(instance, eggLocations[3]),
            MirrorbackAraxyte(instance, eggLocations[4]),
            RupturaAraxyte(instance, eggLocations[5]),
            AcidicAraxyte(instance, eggLocations[6]),
            MirrorbackAraxyte(instance, eggLocations[7]),
            RupturaAraxyte(instance, eggLocations[8])
        )

    private fun getPatternB(): ArrayList<Araxyte> =
        arrayListOf(
            MirrorbackAraxyte(instance, eggLocations[0]),
            RupturaAraxyte(instance, eggLocations[1]),
            AcidicAraxyte(instance, eggLocations[2]),
            MirrorbackAraxyte(instance, eggLocations[3]),
            RupturaAraxyte(instance, eggLocations[4]),
            AcidicAraxyte(instance, eggLocations[5]),
            MirrorbackAraxyte(instance, eggLocations[6]),
            RupturaAraxyte(instance, eggLocations[7]),
            AcidicAraxyte(instance, eggLocations[8])
        )

    private fun getPatternC(): ArrayList<Araxyte> =
        arrayListOf(
            RupturaAraxyte(instance, eggLocations[0]),
            AcidicAraxyte(instance, eggLocations[1]),
            MirrorbackAraxyte(instance, eggLocations[2]),
            RupturaAraxyte(instance, eggLocations[3]),
            AcidicAraxyte(instance, eggLocations[4]),
            MirrorbackAraxyte(instance, eggLocations[5]),
            RupturaAraxyte(instance, eggLocations[6]),
            AcidicAraxyte(instance, eggLocations[7]),
            MirrorbackAraxyte(instance, eggLocations[8])
        )
}