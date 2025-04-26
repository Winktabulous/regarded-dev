package com.zenyte.game.content.theatreofblood.room.verzikvitur.spiders

import com.zenyte.game.content.theatreofblood.room.TheatreNPC
import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikViturRoom
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Direction
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.combat.CombatScript
import com.zenyte.game.world.entity.player.Player

/**
 * @author Jire
 */
internal abstract class NylocasChaseCrab(room: VerzikViturRoom, id: Int, location: Location) :
    TheatreNPC<VerzikViturRoom>(room, id, location, Direction.SOUTH), CombatScript {

    var playerToChase: Player? = null
    abstract val styleToAttack: HitType

    override fun attack(target: Entity?) = 0

    override fun applyHit(hit: Hit) {
        super<TheatreNPC>.applyHit(hit)

        if (hit.hitType == styleToAttack) return
        val source = hit.source as? Player ?: return
        hit.damage = 0
        source.sendMessage("You can only damage the ${definitions.name} with ${styleToAttack.name.lowercase()}.")
    }

    override fun spawn(): NPC = super.spawn().apply {
        playerToChase = Utils.random(room.validTargets) ?: return@apply

        WorldTasksManager.schedule({
            if (room.verzikVitur.isFinished || room.verzikVitur.isDead || room.completed) {
                finish()
                return@schedule
            }

            if (isDead || isFinished) {
                return@schedule
            }

            death()
        }, 25)
    }

    override fun processNPC() {
        super.processNPC()

        if (room.verzikVitur.isFinished || room.verzikVitur.isDead || room.completed) {
            finish()
            return
        }

        if (isDead || isFinished) {
            return
        }

        val player = playerToChase ?: return
        resetWalkSteps()
        calcFollow(player, -1, true, false, false)
        setFaceEntity(player)
        if (middleLocation.withinDistance(player.location, 2) && room.isValidTarget(player)) {
            death()
        }
    }

    private fun death() {
        playerToChase = null
        lock()
        resetWalkSteps()
        setFaceEntity(null)
        WorldTasksManager.schedule {
            val spawnDefinitions = combatDefinitions.spawnDefinitions
            setAnimation(spawnDefinitions.deathAnimation)
            hitpoints = 0
            WorldTasksManager.schedule {
                onFinish(null)
            }
        }
    }

    override fun autoRetaliate(source: Entity?) {
    }

    override fun sendDeath() {

    }

    override fun checkAggressivity(): Boolean {
        return false
    }

    override fun onFinish(source: Entity?) {
        super.onFinish(source)

        for (p in room.validTargets) {
            val dist = p.location.getTileDistance(middleLocation)
            if (dist < 4) {
                val damage = distanceDamage[0.coerceAtLeast(dist - 1)]
                if (damage > 0) {
                    p.applyHit(Hit(this, damage, HitType.REGULAR))
                }
            }
        }
    }

    override fun isValidAnimation(animID: Int) = true

    companion object {
        val distanceDamage = arrayOf(63, 26, 8, 0)
    }

}