package com.zenyte.game.content.theatreofblood.room.verzikvitur.second

import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikVitur
import com.zenyte.game.content.theatreofblood.room.verzikvitur.spiders.NylocasAthanatos
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Utils
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.NPC

/**
 * @author Jire
 */

internal fun VerzikVitur.spawnPurpleCrab() {
    val randomPlayer = Utils.random(room.validTargets) ?: return
    val projectileLocation = randomPlayer.location.copy()
    val ticks = World.sendProjectile(this, projectileLocation, projectile)
    WorldTasksManager.schedule({
        NylocasAthanatos(room, projectileLocation).apply(NPC::spawn)
        if (randomPlayer.location.matches(projectileLocation) && room.isValidTarget(randomPlayer)) {
            randomPlayer.applyHit(Hit(Utils.random(78), HitType.REGULAR))
        }
    }, ticks)
}

private val projectile = Projectile(1586, 54, 26, 20, 0, 160, 0, 0)