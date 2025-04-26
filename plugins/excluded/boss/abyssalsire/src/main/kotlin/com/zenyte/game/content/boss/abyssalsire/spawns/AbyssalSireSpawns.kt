package com.zenyte.game.content.boss.abyssalsire.spawns

import com.zenyte.game.content.boss.abyssalsire.AbyssalSire
import com.zenyte.game.content.boss.abyssalsire.WeakReferenceHelper.invoke
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Direction
import com.zenyte.game.util.Utils
import com.zenyte.game.util.WorldUtil
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.ImmutableLocation
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.player.Player
import java.util.*

/**
 * @author Jire
 * @author Kris
 */
internal class AbyssalSireSpawns(private val sire: AbyssalSire) {

    private val collection: MutableCollection<AbyssalSireSpawn> = HashSet(SPAWN_CAP)

    fun sendSpawn(target: Player): Boolean = sire.run {
        collection.size >= SPAWN_CAP
                && Utils.random(1) == 0
                && target {
            val middle = middleLocation.transform(Direction.SOUTH)
            val destination = ImmutableLocation(it.location)
            val distance = destination.getTileDistance(middle)
            if (distance > AbyssalSire.MAXIMUM_ATTACK_DISTANCE
                || middle.plane != destination.plane
            ) return false

            if (sire.phase.animates)
                animation = shootingSpawnAnimation
            val projectile = spawnProjectile(distance)
            val delayInTicks = World.sendProjectile(middle, destination, projectile)

            val spawn = AbyssalSireSpawn(destination, this@AbyssalSireSpawns, sire)
            if (collection.add(spawn)) {
                WorldTasksManager.schedule({
                    spawn.spawn()
                    spawn.setTarget(target)
                }, delayInTicks)
            }
        } != null
    }

    fun forceSpawn(target: Player): Boolean {
        if (collection.size >= SPAWN_CAP || !sire.lair.players.contains(target)) {
            return false
        }

        val tile = WorldUtil.findEmptySquareRandom(target.middleLocation, 5, 1)
        if (!tile.isPresent) return false

        val spawn = AbyssalSireSpawn(tile.get(), this@AbyssalSireSpawns, sire)
        spawn.spawn()
        spawn.setTarget(target)
        collection.add(spawn)
        return true
    }

    fun reset() {
        collection.forEach(AbyssalSireSpawn::sendDeath)
        collection.clear()
    }

    fun removeSpawn(spawn: AbyssalSireSpawn) = collection.remove(spawn)

    private companion object {

        const val SPAWN_CAP = 15

        const val SPAWN_PROJECTILE_ID = 1274

        val shootingSpawnAnimation = Animation(4530)

        fun spawnProjectile(distance: Int) = Projectile(
            SPAWN_PROJECTILE_ID, 92, 0, 103, 15,
            120 + distance / 3 * 30, 128, 0
        )

    }

}