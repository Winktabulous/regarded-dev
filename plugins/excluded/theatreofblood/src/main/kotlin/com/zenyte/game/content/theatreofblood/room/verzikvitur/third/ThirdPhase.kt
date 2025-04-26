package com.zenyte.game.content.theatreofblood.room.verzikvitur.third

import com.zenyte.game.content.theatreofblood.awardMostDamageContributionPointsToMVPForPhase
import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikVitur
import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikViturPhase
import com.zenyte.game.content.theatreofblood.room.verzikvitur.third.passivespells.DotPassiveSpell
import com.zenyte.game.content.theatreofblood.room.verzikvitur.third.passivespells.NylocasPassiveSpell
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.CollisionUtil
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.masks.Animation

/**
 * @author Jire
 */

internal fun VerzikVitur.thirdPhase(victim: Entity) {
    if (attackCounter == 4) {
        previousTarget = combat.target
        passiveSpell = passiveSpell.nextSpell
        passiveSpell.run { cast() }
        attackCounter = 0
        if (passiveSpell != NylocasPassiveSpell) {
            return
        }
    }

    if (middleLocation.getTileDistance(victim.location) == 4 && !CollisionUtil.collides(x, y, size, victim.x, victim.y, victim.size) && Utils.randomBoolean()) {
        meleeAttack(victim)
    } else if (Utils.randomBoolean()) {
        magicAttack()
    } else {
        rangeAttack()
    }
    attackCounter++
}

internal fun VerzikVitur.processThirdPhase() {
    if (!spawnedTornados && hitpointsAsPercentage <= 20) {
        spawnedTornados = true
        spawnTornados()
    }
}

internal fun VerzikVitur.switchToThirdPhase() {
    if (crabs.isNotEmpty()) {
        for (crab in crabs) {
            crab.finish()
        }
        crabs.clear()
    }

    room.awardMostDamageContributionPointsToMVPForPhase()

    lock()
    resetWalkSteps()
    animation = transformAnimation
    phase = VerzikViturPhase.NONE
    attackCounter = 0

    WorldTasksManager.schedule({
        setTransformation(8373)
        animation = transformAnimation2

        WorldTasksManager.schedule({
            switchPhase(VerzikViturPhase.THIRD)
            animation = Animation.STOP

            setForceTalk("Behold my true nature!")

            setLocation(room.getBaseLocation(29, 23))
            setTarget(Utils.random(room.validTargets))
            unlock()
        }, 2)
    }, 1)
}

private val transformAnimation = Animation(8118)
private val transformAnimation2 = Animation(8119)
