package com.zenyte.game.content.theatreofblood.room.verzikvitur.third

import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikVitur
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.DirectionUtil
import com.zenyte.game.world.entity.player.Player

/**
 * @author Jire
 */

internal fun VerzikVitur.swingArmAttack() {
    //animation = Animation(8121)
    WorldTasksManager.schedule(::swingArmAtTargets)
    WorldTasksManager.schedule(::swingArmAtTargets, 2)
}

private fun VerzikVitur.swingArmAtTargets() {
    if (!canDoMelee()) return

    for (p in room.validTargets)
        swingArmAt(p)
}

private fun VerzikVitur.swingArmAt(p: Player) {
    val middleLocation = middleLocation
    val logical = DirectionUtil.getDirection(middleLocation, p.location)
    if (room.isValidTarget(p) && middleLocation.transform(logical, 1).withinDistance(p.location, 3)) {
        p.applyHit(melee(p, 32))
    }
}

private fun VerzikVitur.canDoMelee() = !isDead && !isFinished && !walkSteps.isEmpty