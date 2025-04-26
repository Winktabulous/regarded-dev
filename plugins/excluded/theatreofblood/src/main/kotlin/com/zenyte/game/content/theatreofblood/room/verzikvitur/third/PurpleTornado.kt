package com.zenyte.game.content.theatreofblood.room.verzikvitur.third

import com.zenyte.game.content.theatreofblood.room.TheatreNPC
import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikViturRoom
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.combat.CombatScript
import com.zenyte.game.world.entity.pathfinding.events.npc.NPCEntityEvent
import com.zenyte.game.world.entity.pathfinding.strategy.EntityStrategy
import com.zenyte.game.world.entity.player.Player

/**
 * @author Jire
 */
internal class PurpleTornado(room: VerzikViturRoom, location: Location) :
	TheatreNPC<VerzikViturRoom>(room, ID, location), CombatScript {

	var chaseTarget: Player? = null

	override fun attack(target: Entity?) = if (chaseTarget == null) 0 else 2

	override fun finish() {
		super.finish()

		chaseTarget = null
	}

	override fun processNPC() {
		super.processNPC()

		if (room.verzikVitur.isFinished || room.verzikVitur.isDead || room.completed) {
			finish()
			return
		}

		val player = chaseTarget ?: return
		if (room.isValidTarget(player)) {
			resetWalkSteps()
			calcFollow(player.location, -1, true, false, false)
            if (middleLocation.withinDistance(player.location, 0)) {
                player.graphics = Graphics(1602)

				finish()
                val damage = player.hitpoints.coerceAtMost(99) / 2
				player.applyHit(Hit(damage, HitType.REGULAR))
				room.verzikVitur.applyHit(Hit(damage, HitType.HEALED))
				player.sendMessage("Verzik Vitur saps your health and powers up!")

                WorldTasksManager.schedule({
					if (room.verzikVitur.isFinished || room.verzikVitur.isDead || room.completed) return@schedule
                    val tornadoLocation = room.verzikVitur.findTornadoLocation(player)
                    if (tornadoLocation != null) {
						PurpleTornado(room, tornadoLocation).run {
							chaseTarget = player
							spawn()
						}
					}
                }, 15)
            }
		} else {
			finish()
		}
	}

	internal companion object {
		const val ID = 8386
	}

}