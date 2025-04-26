package com.zenyte.game.content.boss.abyssalsire.poisonfumes

import com.zenyte.game.content.boss.abyssalsire.AbyssalSire
import com.zenyte.game.content.boss.abyssalsire.AbyssalSirePhase
import com.zenyte.game.content.boss.abyssalsire.WeakReferenceHelper.invoke
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.ImmutableLocation
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics

/**
 * @author Jire
 * @author Kris
 */
internal class AbyssalSirePoisonFumes(val sire: AbyssalSire) {

    private val collection: MutableCollection<AbyssalSirePoisonFume> = HashSet()

    fun attemptClear() {
        if (collection.isNotEmpty()) {
            collection.removeIf { it.remove(sire) }
        }
    }

    fun size() = collection.size

    fun sendPoisonFume() = sire.target { target ->
//        val middle = sire.middleLocation
//        val destination = ImmutableLocation(target.location)
//        val distance = destination.getTileDistance(middle)
//
//        if (distance > AbyssalSire.MAXIMUM_ATTACK_DISTANCE
//            || middle.plane != destination.plane
//        ) return@target
//
//        if (collection.add(AbyssalSirePoisonFume(destination))) {
//            if (sire.phase.animates)
//                sire.animation = shootingPoisonFumesAnimation
//            World.sendGraphics(poisonFumesGraphics, destination)
//        }
    }

    private companion object {
        val shootingPoisonFumesAnimation = Animation(4531)
        val poisonFumesGraphics = Graphics(1275)
    }

}