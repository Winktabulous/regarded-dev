package com.zenyte.game.content.boss.abyssalsire.poisonfumes

import com.zenyte.game.content.boss.abyssalsire.AbyssalNexusArea
import com.zenyte.game.content.boss.abyssalsire.AbyssalSire
import com.zenyte.game.util.Utils
import com.zenyte.game.world.WorldThread
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType

/**
 * @author Jire
 * @author Kris
 */
internal class AbyssalSirePoisonFume(
    val location: Location,
    private var lifespan: Int = POISON_FUME_LIFESPAN_TICKS,
    private var processTick: Long = WorldThread.WORLD_CYCLE + 2
) {

    fun remove(sire: AbyssalSire): Boolean {
        val area = sire.lair
        val needsRemoving = --lifespan <= 0
        if (WorldThread.WORLD_CYCLE >= processTick) {
            for (player in area.players) {
                if (player.isNulled || player.isFinished || player.isTeleported || player.isDead) continue
                val distance = location.getTileDistance(player.location)
                if (distance > 1) continue
                val damage = if (distance == 0) Utils.random(10, 30) else Utils.random(2, 8)
                player.applyHit(Hit(damage, HitType.POISON))
                sire.hitByPool = true
                sire.perfectSire = false
            }
        }
        return needsRemoving
    }

    private companion object {
        const val POISON_FUME_LIFESPAN_TICKS = 5
    }

}