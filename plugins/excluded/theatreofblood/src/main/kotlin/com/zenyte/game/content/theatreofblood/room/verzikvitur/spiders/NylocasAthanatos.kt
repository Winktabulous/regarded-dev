package com.zenyte.game.content.theatreofblood.room.verzikvitur.spiders

import com.zenyte.game.content.theatreofblood.room.TheatreNPC
import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikViturRoom
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Utils
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.combat.CombatScript
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat

/**
 * @author Jire
 */
internal class NylocasAthanatos(room: VerzikViturRoom, location: Location) :
    TheatreNPC<VerzikViturRoom>(room, ID, location), CombatScript {

    var hitByPoisonWeapon = false
    var ticks = 0

    override fun attack(target: Entity?): Int {
        return 0
    }

    override fun spawn(): NPC = super.spawn().apply {
        animation = Animation(8079)
        WorldTasksManager.schedule({
            sendDeath()
            val verzik = room.verzikVitur
            if (room.completed || verzik.isDead || verzik.isFinished) {
                return@schedule
            }

            verzik.heal(hitpoints)
        }, 50)
    }

    override fun processNPC() {
        super.processNPC()

        if (room.completed) {
            finish()
            return
        }

        val verzik = room.verzikVitur
        if (verzik.isDead || verzik.isFinished) {
            finish()
            return
        }

        faceEntity(verzik)
        ticks++
        if (ticks % 4 == 0) {
            val poison = hitByPoisonWeapon
            val delay = World.sendProjectile(this, verzik, if (poison) projectilePoison else projectileHeal)
            WorldTasksManager.schedule({
                if (verzik.isDead || verzik.isFinished) {
                    return@schedule
                }

                if (poison) {
                    verzik.applyHit(Hit(Utils.random(70, 75), HitType.POISON))
                    sendDeath()
                } else verzik.applyHit(Hit(Utils.random(9, 11), HitType.HEALED))
            }, delay)
        }
    }

    override fun applyHit(hit: Hit) {
        super<TheatreNPC>.applyHit(hit)

        val source = hit.source as? Player ?: return
        if (hit.hitType == HitType.POISON || PlayerCombat.hasPoisonousWeapon(source)) {
            hitByPoisonWeapon = true
            graphics = Graphics(1590)
        } else {
            hit.damage = 0
            source.sendMessage("You can only damage the ${definitions.name} with poisoned weapons.")
            source.stopAll()
        }
    }

    override fun autoRetaliate(source: Entity?) {

    }

    companion object {
        val projectileHeal = Projectile(1587, 28, 32, 0, 7, 30, 0, 0)
        val projectilePoison = Projectile(1588, 28, 32, 0, 7, 30, 0, 0)
        const val ID = 8384
    }

}